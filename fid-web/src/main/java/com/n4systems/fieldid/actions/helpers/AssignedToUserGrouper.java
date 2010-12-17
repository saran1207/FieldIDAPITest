package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.api.Listable;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.UserFilteredLoader;

public class AssignedToUserGrouper {

	private List<Listable<Long>> employees;
	UserFilteredLoader userLoader; 
	
	public AssignedToUserGrouper (SecurityFilter filter, List<Listable<Long>> employees){
		this.employees=employees;
		userLoader = new UserFilteredLoader(filter);
	}
	
	public List<String> getUserOwners(){
		List<String> owners = new ArrayList<String>();
		
		for (Listable<Long> user : employees){
			userLoader.setId(user.getId());
			if(!owners.contains(userLoader.load().getOwner().getDisplayName())){
				owners.add(userLoader.load().getOwner().getDisplayName());	
			}
		}
		return owners;
	}
	
	public List<Listable<Long>> getUsersForOwner(String owner){
		List<Listable<Long>> users = new ArrayList<Listable<Long>>();
		
		for (Listable<Long> user : employees){
			userLoader.setId(user.getId());
			if(owner.equals(userLoader.load().getOwner().getDisplayName())){
				users.add(user);	
			}
		}
		return users;
	}
}
