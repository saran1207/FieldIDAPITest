package com.n4systems.fieldid.actions.signup;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.SignUpPackageBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.signuppackage.SignUpPackageListLoader;
import com.n4systems.subscription.CurrentSubscription;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.test.helpers.FluentArrayList;


public class AccountHelperTest {

	private static final long SOME_CONTRACT_PRICE_ID = 160L;


	@Test
	public void should_request_accounts_current_package_from_subscription_agent() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().build(); 
		List<SignUpPackage> packages = new FluentArrayList<SignUpPackage>(createSignUpPackageWithSimpleContract(SignUpPackageDetails.Free, SOME_CONTRACT_PRICE_ID).build());
		CurrentSubscription currentSubscription = new CurrentSubscription(SOME_CONTRACT_PRICE_ID, false, false, false);
		
		
		SignUpPackageListLoader packageLoader = createMock(SignUpPackageListLoader.class);
		expect(packageLoader.load()).andReturn(packages);
		replay(packageLoader);
		
		SubscriptionAgent subscriptionAgent = createMock(SubscriptionAgent.class);
		expect(subscriptionAgent.currentSubscriptionFor(anyLong())).andReturn(currentSubscription);
		replay(subscriptionAgent);
		
		
		
		AccountHelper sut = new AccountHelper(subscriptionAgent, primaryOrg, packageLoader);
		
		assertEquals(SignUpPackageDetails.Free.getName(), sut.currentPackageFilter().getPackageName());
		verify(subscriptionAgent);
		verify(packageLoader);
	}
	
	
	


	@Test
	public void should_request_accounts_current_package_from_subscription_agent_only_once() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().build(); 
		CurrentSubscription currentSubscription = new CurrentSubscription(SOME_CONTRACT_PRICE_ID, false, false, false);
		
		SubscriptionAgent subscriptionAgent = createMock(SubscriptionAgent.class);
		expect(subscriptionAgent.currentSubscriptionFor(anyLong())).andReturn(currentSubscription);
		expectLastCall().once();
		replay(subscriptionAgent);
		
		List<SignUpPackage> packages = new FluentArrayList<SignUpPackage>(createSignUpPackageWithSimpleContract(SignUpPackageDetails.Free, SOME_CONTRACT_PRICE_ID).build());
		
		SignUpPackageListLoader packageLoader = createMock(SignUpPackageListLoader.class);
		expect(packageLoader.load()).andReturn(packages);
		replay(packageLoader);
		
		AccountHelper sut = new AccountHelper(subscriptionAgent, primaryOrg, packageLoader);
		
		sut.currentPackageFilter();
		sut.currentPackageFilter();
		
		
		verify(subscriptionAgent);
	}
}
