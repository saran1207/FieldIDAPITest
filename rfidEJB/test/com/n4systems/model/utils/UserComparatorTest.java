package com.n4systems.model.utils;

import static com.n4systems.model.builders.OrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.orgs.BaseOrg;




public class UserComparatorTest {

	@Test
	public void should_order_users_case_insensitive_alphabetically_inside_the_same_owner() {
		BaseOrg owner = aPrimaryOrg().build();
		
		UserBean user1 = anEmployee().withOwner(owner).withFirstName("A").build();
		UserBean user2 = anEmployee().withOwner(owner).withFirstName("z").build();
		
		int actualCompareValue = new UserComparator().compare(user1, user2);
		
		assertTrue(new Integer(actualCompareValue).toString() + " is not less than 0", actualCompareValue < 0);
	}
	
	
	@Test
	public void should_order_users_in_primary_org_above_users_in_secondary_org() {
		BaseOrg primary = aPrimaryOrg().build();
		BaseOrg secondaryOrg = aSecondaryOrg().withParent(primary).build();
		
		UserBean user1 = anEmployee().withOwner(primary).build();
		UserBean user2 = anEmployee().withOwner(secondaryOrg).build();
		
		int actualCompareValue = new UserComparator().compare(user1, user2);
		
		assertTrue(new Integer(actualCompareValue).toString() + " is not less than 0", actualCompareValue < 0);
	}
	
	@Test
	public void should_order_users_in_each_secondary_org_case_insensitive_alphabetically_by_secondary_org_name() {
		BaseOrg primary = aPrimaryOrg().build();
		BaseOrg secondaryOrg1 = aSecondaryOrg().withParent(primary).withName("A").build();
		BaseOrg secondaryOrg2 = aSecondaryOrg().withParent(primary).withName("z").build();
		
		UserBean user1 = anEmployee().withOwner(secondaryOrg1).build();
		UserBean user2 = anEmployee().withOwner(secondaryOrg2).build();
		
		int actualCompareValue = new UserComparator().compare(user1, user2);
		
		assertTrue(new Integer(actualCompareValue).toString() + " is not less than 0", actualCompareValue < 0);
	}
	
	@Test
	public void should_order_users_in_each_secondary_org_above_the_user_in_a_customer_directly_under_the_primary() {
		BaseOrg primary = aPrimaryOrg().build();
		BaseOrg secondaryOrg = aSecondaryOrg().withParent(primary).build();
		BaseOrg customerOrg = aCustomerOrg().withParent(primary).build();
		
		UserBean user1 = anEmployee().withOwner(secondaryOrg).build();
		UserBean user2 = anEmployee().withOwner(customerOrg).build();
		
		int actualCompareValue = new UserComparator().compare(user1, user2);
		
		assertTrue(new Integer(actualCompareValue).toString() + " is not less than 0", actualCompareValue < 0);
	}

}
