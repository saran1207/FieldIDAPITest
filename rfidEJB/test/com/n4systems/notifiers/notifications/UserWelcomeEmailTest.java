package com.n4systems.notifiers.notifications;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.SecondaryOrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.junit.Assert.*;

import org.junit.Test;


import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.user.User;


public class UserWelcomeEmailTest {

	private String signInUrl = "https://somedomain.com/fieldid/login.action";
	private String forgotPassword = "https://somedomain.com/fieldid/forgotPassword.action";




	@Test
	public void should_respond_to_user_name_from_the_user_being_notified() throws Exception {
		User user = aUser().withUserId("alex").build();
		UserWelcomeEmail sut = new UserWelcomeEmail(user, signInUrl, forgotPassword);
		
		assertEquals("alex", sut.getUserName());
	}
	
	
	@Test
	public void should_respond_to_tenant_name_from_the_primary_org_of_the_user_being_notified() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withName("Primary Org").build();
		SecondaryOrg secondaryOrg = aSecondaryOrg().withPrimaryOrg(primaryOrg).build();
		User user = aUser().withOwner(secondaryOrg).build();
		
		UserWelcomeEmail sut = new UserWelcomeEmail(user, signInUrl, forgotPassword);
		
		
		assertEquals("Primary Org", sut.getTenantName());
	}
	
	@Test
	public void should_have_the_teants_name_at_the_begining_of_the_subject() {
		User user = aUser().build();
		
		UserWelcomeEmail sut = new UserWelcomeEmail(user, signInUrl, null);
		
		assertTrue(sut.subject().startsWith(sut.getTenantName()));
	}
	
	
	
}
