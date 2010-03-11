package com.n4systems.notifiers.notifications;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.SecondaryOrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;


public class UserWelcomeEmailTest {

	@Test
	public void should_respond_to_user_name_from_the_user_being_notified() throws Exception {
		UserBean user = aUser().withUserId("alex").build();
		UserWelcomeEmail sut = new UserWelcomeEmail(user);
		
		assertEquals("alex", sut.getUserName());
	}
	
	
	@Test
	public void should_respond_to_tenant_name_from_the_primary_org_of_the_user_being_notified() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withName("Primary Org").build();
		SecondaryOrg secondaryOrg = aSecondaryOrg().withPrimaryOrg(primaryOrg).build();
		UserBean user = aUser().withOwner(secondaryOrg).build();
		
		UserWelcomeEmail sut = new UserWelcomeEmail(user);
		
		
		assertEquals("Primary Org", sut.getTenantName());
	}
	
	@Test
	public void should_have_the_teants_name_at_the_begining_of_the_subject() {
		UserBean user = aUser().build();
		
		UserWelcomeEmail sut = new UserWelcomeEmail(user);
		
		assertTrue(sut.subject().startsWith(sut.getTenantName()));
	}
	
	
	
	@Test
	public void should_find_reset_password_should_be_sent_user_in_the_notification() throws Exception {
		UserBean user = aUser().withResetPasswordKey().build();
		
		UserWelcomeEmail sut = new UserWelcomeEmail(user);
		
		assertTrue(sut.isResetPasswordSet());
	}
	
	
	@Test
	public void should_find_reset_password_should_be_not_sent_user_in_the_notification() throws Exception {
		UserBean user = aUser().withOutResetPasswordKey().build();
		
		UserWelcomeEmail sut = new UserWelcomeEmail(user);
		
		assertFalse(sut.isResetPasswordSet());
	}
	
}
