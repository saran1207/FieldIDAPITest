package com.n4systems.subscription;

import static org.junit.Assert.*;

import org.junit.Test;


public class CurrentSubscriptionTest {
	
	
	@Test
	public void should_do_equality_by_value() throws Exception {
		CurrentSubscription currentSubscription = new CurrentSubscription(1L, true, true, true);
		
		assertTrue(currentSubscription.equals(new CurrentSubscription(1L, true, true, true)));
		
		assertFalse(currentSubscription.equals(new CurrentSubscription(2L, true, true, true)));
		assertFalse(currentSubscription.equals(new CurrentSubscription(1L, false, true, true)));
		assertFalse(currentSubscription.equals(new CurrentSubscription(1L, true, false, false)));
	}
	
	
	@Test
	public void should_find_that_an_upgrade_from_the_current_subscription_requires_billing_information() throws Exception {
		CurrentSubscription currentSubscription = new CurrentSubscription(1L, true, false, false);
		
		assertTrue(currentSubscription.isUpgradeRequiresBillingInformation()); 
	}

	@Test
	public void should_find_that_an_upgrade_from_the_current_subscription_does_not_requires_billing_information() throws Exception {
		CurrentSubscription currentSubscription = new CurrentSubscription(1L, true, true, true);
		
		assertFalse(currentSubscription.isUpgradeRequiresBillingInformation()); 
	}
	

}
