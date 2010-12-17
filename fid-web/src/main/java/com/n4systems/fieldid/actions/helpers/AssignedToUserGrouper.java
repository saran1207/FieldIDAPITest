package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;

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
		List<String> owners = new ArrayList<String>();

		for (Listable<Long> user : employees) {
			userLoader.setId(user.getId());
			if (!(userLoader.load()==null) && !owners.contains(userLoader.load().getOwner().getDisplayName())) {
			
				BaseOrg baseOrg = userLoader.load().getOwner();
				if (sessionUser.isReadOnlyCustomerUser() && baseOrg.isExternal()){
					if (baseOrg.equals(sessionUser.getOwner())){
						owners.add(baseOrg.getDisplayName());	
					}
				}else{
					owners.add(userLoader.load().getOwner().getDisplayName());	
				}
			}
		}
		return owners;
	}

	public List<Listable<Long>> getUsersForOwner(String owner) {
		List<Listable<Long>> users = new ArrayList<Listable<Long>>();

		for (Listable<Long> user : employees) {
			userLoader.setId(user.getId());
			if (!(userLoader.load()==null) && owner.equals(userLoader.load().getOwner().getDisplayName())) {
				users.add(user);
			}
		}
		return users;
	}
}
