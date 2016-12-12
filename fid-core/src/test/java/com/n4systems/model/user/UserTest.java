package com.n4systems.model.user;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UserTest {
	
	@Test
	public void pre_persist_ensures_referral_key_is_set() {
		User user = new User();
		user.setReferralKey(null);
		
		user.onCreate();
		
		assertNotNull(user.getReferralKey());
	}
	
	@Test
	public void pre_merge_ensures_referral_key_is_set() {
		User user = new User();
		user.setReferralKey(null);
		
		user.onUpdate();
		
		
		assertNotNull(user.getReferralKey());
	}
	
}
