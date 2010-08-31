package com.n4systems.model.utils;

import static com.n4systems.model.builders.OrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;



/**
 * Naming:
 * primary_org
 * 		secondary_org
 * 			secondary_customer
 * 				secondary_division
 * 		primary_customer
 * 			primary_division
 * 		
 * @author aaitken
 *
 */
public class UserComparatorTest {

	private BaseOrg primaryOrg;
	
	@Before
	public void setUp() {
		primaryOrg = aPrimaryOrg().build();
	}


	@Test
	public void should_order_users_case_insensitive_alphabetically_inside_the_same_owner() {
		
		User user1 = anEmployee().withOwner(primaryOrg).withFirstName("A").build();
		User user2 = anEmployee().withOwner(primaryOrg).withFirstName("z").build();
		
		executeCompareAndVerify(user1, user2);
	}
	
	
	@Test
	public void should_order_users_in_primary_org_above_users_in_secondary_org() {
		BaseOrg secondaryOrg = aSecondaryOrg().withParent(primaryOrg).build();
		
		User user1 = anEmployee().withOwner(primaryOrg).build();
		User user2 = anEmployee().withOwner(secondaryOrg).build();
		
		executeCompareAndVerify(user1, user2);
	}
	
	@Test
	public void should_order_users_in_each_orgs_at_the_same_level_case_insensitive_alphabetically_by_org_name() {
		BaseOrg secondaryOrg1 = aSecondaryOrg().withParent(primaryOrg).withName("A").build();
		BaseOrg secondaryOrg2 = aSecondaryOrg().withParent(primaryOrg).withName("z").build();
		
		User user1 = anEmployee().withOwner(secondaryOrg1).build();
		User user2 = anEmployee().withOwner(secondaryOrg2).build();
		
		executeCompareAndVerify(user1, user2);
	}
	
	@Test
	public void should_order_users_in_each_secondary_org_above_the_user_in_a_primary_customer() {
		BaseOrg secondaryOrg = aSecondaryOrg().withParent(primaryOrg).build();
		BaseOrg customerOrg = aCustomerOrg().withParent(primaryOrg).build();
		
		User user1 = anEmployee().withOwner(secondaryOrg).build();
		User user2 = anEmployee().withOwner(customerOrg).build();
		
		executeCompareAndVerify(user1, user2);
	}

	
	@Test 
	public void should_order_users_in_primary_division_below_primary_customer_directly_above_in_the_hierarchy() {
		BaseOrg customerOrg = aCustomerOrg().withParent(primaryOrg).build();
		BaseOrg divisionOrg = aDivisionOrg().withParent(customerOrg).build();
		
		User user1 = anEmployee().withOwner(customerOrg).build();
		User user2 = anEmployee().withOwner(divisionOrg).build();
		
		executeCompareAndVerify(user1, user2);
	}
	
	@Test 
	public void should_order_users_in_primary_customer_below_secondary_org_directly_above_in_the_hierarchy() {
		BaseOrg secondaryOrg = aSecondaryOrg().withParent(primaryOrg).build();
		BaseOrg customerOrg = aCustomerOrg().withParent(secondaryOrg).build();
		
		
		User user1 = anEmployee().withOwner(secondaryOrg).build();
		User user2 = anEmployee().withOwner(customerOrg).build();
		
		executeCompareAndVerify(user1, user2);
	}
	
	@Test 
	public void should_order_users_in_secondary_division_below_secondary_customer_directly_above_in_the_hierarchy() {
		BaseOrg secondaryOrg = aSecondaryOrg().withParent(primaryOrg).build();
		BaseOrg customerOrg = aCustomerOrg().withParent(secondaryOrg).build();
		BaseOrg divisionOrg = aDivisionOrg().withParent(customerOrg).build();
		
		User user1 = anEmployee().withOwner(customerOrg).build();
		User user2 = anEmployee().withOwner(divisionOrg).build();
		
		executeCompareAndVerify(user1, user2);
	}
	
	public void should_order_users_in_primary_customer_below_secondary_customer() {
		
		BaseOrg secondaryOrg = aSecondaryOrg().withParent(primaryOrg).build();
		BaseOrg secondaryCustomerOrg = aCustomerOrg().withParent(secondaryOrg).build();
		BaseOrg primaryCustomer = aCustomerOrg().withParent(primaryOrg).build();
		
		User user1 = anEmployee().withOwner(primaryCustomer).build();
		User user2 = anEmployee().withOwner(secondaryCustomerOrg).build();
		
		executeCompareAndVerify(user1, user2);
	}

	public void should_order_users_in_primary_customer_below_secondary_division() {
		BaseOrg secondaryOrg = aSecondaryOrg().withParent(primaryOrg).build();
		BaseOrg secondaryDivision = aDivisionOrg().withParent(aCustomerOrg().withParent(secondaryOrg).build()).build();
		BaseOrg primaryCustomer = aCustomerOrg().withParent(primaryOrg).build();
		
		User user1 = anEmployee().withOwner(primaryCustomer).build();
		User user2 = anEmployee().withOwner(secondaryDivision).build();
		
		executeCompareAndVerify(user1, user2);
	}
	

	private void executeCompareAndVerify(User userCloserToTheBottom, User userCloserToTheTop) {
		int actualCompareValue = new UserComparator().compare(userCloserToTheBottom, userCloserToTheTop);
		
		assertTrue(new Integer(actualCompareValue).toString() + " is not less than 0", actualCompareValue < 0);
	}
	
}
