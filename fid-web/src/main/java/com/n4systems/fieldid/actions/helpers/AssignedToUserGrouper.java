package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import rfid.web.helper.SessionUser;

import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.UserFilteredLoader;

public class AssignedToUserGrouper {

	private List<Listable<Long>> employees;
	private SessionUser sessionUser;
	UserFilteredLoader userLoader;

	public AssignedToUserGrouper(SecurityFilter filter, List<Listable<Long>> employees, SessionUser sessionUser) {
		this.employees = employees;
		userLoader = new UserFilteredLoader(filter);
		this.sessionUser = sessionUser;
	}

	public List<String> getUserOwners() {
		List<BaseOrg> owners = new ArrayList<BaseOrg>();
		BaseOrg baseOrg;

		for (Listable<Long> user : employees) {
			userLoader.setId(user.getId());

			if (userLoader.load() != null && !owners.contains(userLoader.load().getOwner())) {
				baseOrg = userLoader.load().getOwner();
				// If user is read-only customer, check that this baseOrg is the customer org
				if (sessionUser.isReadOnlyCustomerUser() && baseOrg.isExternal()) {
					
					//Ensure customer read-only users only view the customer org they belong to.
					if (baseOrg.equals(sessionUser.getOwner())) {
						owners.add(baseOrg);
					}
				// Else must be a primaryOrg.
				} else {
					owners.add(baseOrg);
				}
			}
		}
		return sortByPrimaryOrgFirst(new ArrayList<BaseOrg>(owners), new ArrayList<String>());
	}

	public List<Listable<Long>> getUsersForOwner(String owner) {
		List<Listable<Long>> users = new ArrayList<Listable<Long>>();

		for (Listable<Long> user : employees) {
			
			userLoader.setId(user.getId());
			if (!(userLoader.load() == null) && owner.equals(userLoader.load().getOwner().getDisplayName())) {
				users.add(user);
			}
		}
		return users;
	}

	public List<String> sortByPrimaryOrgFirst(ArrayList<BaseOrg> orgs, ArrayList<String> resultSet) {
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
