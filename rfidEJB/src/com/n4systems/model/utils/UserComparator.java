package com.n4systems.model.utils;

import com.n4systems.util.NullHandlingComparator;

import rfid.ejb.entity.UserBean;

import java.util.Comparator;

public class UserComparator implements Comparator<UserBean> {
	private NullHandlingComparator<Long> longComp = new NullHandlingComparator<Long>();
	
	public int compare(UserBean user1, UserBean user2) {
		
		int custComp = compareCustomers(user1.getCustomerId(), user2.getCustomerId());
		int diviComp = compareDivisions(user1.getDivisionId(), user2.getDivisionId());
		
		// if the customers were different, that is higher priority then division (aka only use the division as a tie breaker)
	    return (custComp != 0) ? custComp : diviComp;
    }

	private int compareCustomers(Long customer1, Long customer2) {
		return longComp.compare(customer1, customer2);
	}
	
	private int compareDivisions(Long division1, Long division2) {
		return longComp.compare(division1, division2);
	}
}
