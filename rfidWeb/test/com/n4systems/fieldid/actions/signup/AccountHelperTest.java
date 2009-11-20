package com.n4systems.fieldid.actions.signup;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.subscription.SubscriptionAgent;


public class AccountHelperTest {

	@Test
	public void should_request_accounts_current_package_from_subscription_agent() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().build(); 
		
		SubscriptionAgent subscriptionAgent = createMock(SubscriptionAgent.class);
		expect(subscriptionAgent.currentPackageFor(anyLong())).andReturn(SignUpPackageDetails.Free.getSyncId());
		replay(subscriptionAgent);
		
		AccountHelper sut = new AccountHelper(subscriptionAgent, primaryOrg);
		
		assertEquals(SignUpPackageDetails.Free.getName(), sut.currentPackageFilter().getPackageName());
		verify(subscriptionAgent);
	}
	
	
	@Test
	public void should_request_accounts_current_package_from_subscription_agent_only_once() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().build(); 
		
		SubscriptionAgent subscriptionAgent = createMock(SubscriptionAgent.class);
		expect(subscriptionAgent.currentPackageFor(anyLong())).andReturn(SignUpPackageDetails.Free.getSyncId());
		expectLastCall().once();
		replay(subscriptionAgent);
		
		AccountHelper sut = new AccountHelper(subscriptionAgent, primaryOrg);
		
		sut.currentPackageFilter();
		sut.currentPackageFilter();
		
		
		verify(subscriptionAgent);
	}
}
