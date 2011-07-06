package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import rfid.web.helper.SessionUser;

import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.UserFilteredLoader;

public class AssignedToUserGrouper {

	private List<Listable<Long>> employees;
	private SessionUser sessionUser;
	UserFilteredLoader userLoader;
	private Map<String, List<Listable<Long>>> ownerToUserMap = new HashMap<String, List<Listable<Long>>>();

	public AssignedToUserGrouper(SecurityFilter filter, List<Listable<Long>> employees, SessionUser sessionUser) {
		this.employees = employees;
		userLoader = new UserFilteredLoader(filter);
		this.sessionUser = sessionUser;
	}

	public List<String> getUserOwners() {
		BaseOrg baseOrg;
		List<BaseOrg> owners = new ArrayList<BaseOrg>();

		for (Listable<Long> user : employees) {
			
			userLoader.setId(user.getId());
			
			if (user.getId() != 0 && userLoader.load() != null) {
		
				baseOrg = userLoader.load().getOwner();

				// If user is read-only customer, check that this baseOrg is the customer org
				if (sessionUser.isReadOnlyCustomerUser() && baseOrg.isExternal()) {

					// Ensure customer read-only users only view the customer org they belong to.
					if (baseOrg.equals(sessionUser.getOwner())) {
						addToMap(baseOrg, user);

						// Maintain a list of owners in order to sort the dropdown list later.
						if (!owners.contains(baseOrg)) {
							owners.add(baseOrg);
						}
					}

					// Else must be a primaryOrg.
				} else {
					addToMap(baseOrg, user);
					if (!owners.contains(baseOrg)) {
						owners.add(baseOrg);
					}
				}
			}
		}
		return sortByPrimaryOrgFirst(owners);
	}

	private void addToMap(BaseOrg baseOrg, Listable<Long> user) {
		List<Listable<Long>> employeesForThisOwner;

		if (ownerToUserMap.containsKey(baseOrg.getDisplayName())) {
			employeesForThisOwner = ownerToUserMap.get(baseOrg.getDisplayName());
			employeesForThisOwner.add(user);
			ownerToUserMap.put(baseOrg.getDisplayName(), employeesForThisOwner);
		} else {
			employeesForThisOwner = new ArrayList<Listable<Long>>();
			employeesForThisOwner.add(user);
			ownerToUserMap.put(baseOrg.getDisplayName(), employeesForThisOwner);
		}
	}

	public List<Listable<Long>> getUsersForOwner(String owner) {
		return ownerToUserMap.get(owner);
	}

	public List<BaseOrg> sortByPrimaryOrgsFirstObj(List<BaseOrg> orgs) {
        List<BaseOrg> sortedOrgs = new ArrayList<BaseOrg>();
        sortedOrgs.addAll(orgs);
        Collections.sort(sortedOrgs, new Comparator<BaseOrg>() {
            @Override
            public int compare(BaseOrg org1, BaseOrg org2) {
                Long org1Value = org1.isPrimary() ? 0L : 1L;
                Long org2Value = org2.isPrimary() ? 0L : 1L;
                return org1Value.compareTo(org2Value);
            }
        });
        return sortedOrgs;
	}

	public List<String> sortByPrimaryOrgFirst(List<BaseOrg> orgs) {
        orgs = sortByPrimaryOrgsFirstObj(orgs);
        List<String> orgNames = new ArrayList<String>();

        for (BaseOrg org : orgs) {
            orgNames.add(org.getDisplayName());
        }

        return orgNames;
	}
}
