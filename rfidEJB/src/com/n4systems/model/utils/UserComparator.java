package com.n4systems.model.utils;

import java.util.Comparator;

import rfid.ejb.entity.UserBean;

import com.n4systems.util.NullHandlingComparator;

public class UserComparator implements Comparator<UserBean> {
	private NullHandlingComparator<Long> longComp = new NullHandlingComparator<Long>();
	
	public int compare(UserBean user1, UserBean user2) {
	    return compareCustomers(user1.getOwner().getId(), user2.getOwner().getId());
    }

	private int compareCustomers(Long customer1, Long customer2) {
		return longComp.compare(customer1, customer2);
	}
}
