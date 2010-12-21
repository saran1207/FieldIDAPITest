package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
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

			if (user.getId() != 0) {
				
				userLoader.setId(user.getId());
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
		return sortByPrimaryOrgFirst(owners, new ArrayList<String>());
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

	public List<String> sortByPrimaryOrgFirst(List<BaseOrg> orgs, ArrayList<String> resultSet) {
		ListIterator<BaseOrg> it = orgs.listIterator();
		BaseOrg org;

		if (!it.hasNext()) {
			return resultSet;
		} else {
			org = it.next();
			orgs.remove(org);

			// Group primaryOrgs toward the top of the list.
			if (org.isPrimary()) {
				resultSet.add(0, org.getDisplayName());
			} else {
				resultSet.add(org.getDisplayName());
			}
			return sortByPrimaryOrgFirst(orgs, resultSet);
		}
	}
}
