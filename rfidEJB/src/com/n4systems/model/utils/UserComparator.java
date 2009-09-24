package com.n4systems.model.utils;

import java.util.Comparator;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.orgs.BaseOrg;
/**
 * sorts the users into a list so that
 * it goes 
 * primary org 
 * 		users
 * 		secondary orgs
 * 			users
 * 			customer
 * 				users
 * 				divisions
 * 					users
 * 		customers
 * 			users
 * 			divisions
 * 				users
 * 
 *
 */
public class UserComparator implements Comparator<UserBean> {
	
	private static final int CLOSER_TO_THE_BOTTOM = 1;
	private static final int EQUAL = 0;
	private static final int CLOSER_TO_THE_TOP = -1;

	public int compare(UserBean user1, UserBean user2) {
		int result = compareOwners(user1.getOwner(), user2.getOwner());
		if (result == EQUAL) {
			result = user1.getUserLabel().compareToIgnoreCase(user2.getUserLabel());
		} 
		
		return result;	
    }

	private int compareOwners(BaseOrg owner1, BaseOrg owner2) {
		if (owner1 == null) {
			return CLOSER_TO_THE_TOP;
		}
		if (owner2 == null) {
			return CLOSER_TO_THE_BOTTOM;
		}
		if (owner1.equals(owner2)) {
			return EQUAL;
		}
		
		int result = compareOwners(owner1.getParent(), owner2.getParent());
		
		if (result == EQUAL) {
			result = compareOwnerTypes(owner1, owner2);
			if (result == EQUAL) {
				result = owner1.getName().compareToIgnoreCase(owner2.getName());
			}
		}
		
		return result;
	}

	private int compareOwnerTypes(BaseOrg owner1, BaseOrg owner2) {
		if (owner1.sameTypeAs(owner2)) {
			return EQUAL;
		} else if (owner1.isInternal()) {
			return CLOSER_TO_THE_TOP;
		}
		
		return CLOSER_TO_THE_BOTTOM;
	}
	
	
	
}
