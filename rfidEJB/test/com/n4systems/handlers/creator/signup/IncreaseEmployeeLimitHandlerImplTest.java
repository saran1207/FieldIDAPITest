package com.n4systems.handlers.creator.signup;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.subscription.UpgradeResponse;
import com.n4systems.subscription.UpgradeSubscription;


public class IncreaseEmployeeLimitHandlerImplTest  extends TestUsesTransactionBase {

	
	private UpgradeRequest upgradeRequest = new UpgradeRequest();

	@Before
	public void setUp() {
		mockTransaction();
	}

	
	@Test
	public void should_response_with_price_for_employee_upgrade() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().build();
		SubscriptionAgent subscriptionAgent = createSubscriptionAgentForUpgrade(new UpgradeCost(300.0F, 4000.0F, "DEC. 10 2009"));
		
		
		UpgradeAccountHandler sut = new IncreaseEmployeeLimitHandlerImpl(primaryOrg, subscriptionAgent, null);
		
		UpgradeCost expectedCost = new UpgradeCost(300.0F, 4000.0F, "DEC. 10 2009");
		
		assertEquals(expectedCost, sut.priceForUpgrade(validUpgradeRequest()));
 	}
	
	@Test
	public void should_response_with_price_from_subscription_agent() throws Exception {
		
		PrimaryOrg primaryOrg = aPrimaryOrg().build();
		SubscriptionAgent subscriptionAgent = createSubscriptionAgentForUpgrade(new UpgradeCost(300.0F, 4000.0F, "DEC. 10 2009"));
		
		new IncreaseEmployeeLimitHandlerImpl(primaryOrg, subscriptionAgent, null).priceForUpgrade(validUpgradeRequest());
		
		verify(subscriptionAgent);
 	}
	
	
	@Test
	public void should_call_subscription_agent_to_increase_number_of_users() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().build();
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		
		
		UpgradeAccountHandler sut = new IncreaseEmployeeLimitHandlerImpl(primaryOrg, subscriptionAgent, successfulOrgSaver(primaryOrg));
		
		sut.upgradeTo(validUpgradeRequest(), mockTransaction);
		
		verify(subscriptionAgent);
 	}
	
	
	@Test
	public void should_call_subscription_agent_to_increase_the_user_limit_on_successful_upgrade() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withEmployeeLimit(4L).build();
		Long initialUserLimit = primaryOrg.getLimits().getUsers();
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		OrgSaver saver = successfulOrgSaver(primaryOrg);
		
		
		UpgradeAccountHandler sut = new IncreaseEmployeeLimitHandlerImpl(primaryOrg, subscriptionAgent, saver);
		
		sut.upgradeTo(validUpgradeRequest(), mockTransaction);
		assertEquals(new Long(initialUserLimit + 1L), primaryOrg.getLimits().getUsers());
 	}
	
	@Test
	public void should_save_the_primary_org_on_a_successful_upgrade() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().withEmployeeLimit(4L).build();
		SubscriptionAgent subscriptionAgent = subScriptionAgentForSuccessfulUpgrade();
		OrgSaver saver = successfulOrgSaver(primaryOrg);
		
		UpgradeAccountHandler sut = new IncreaseEmployeeLimitHandlerImpl(primaryOrg, subscriptionAgent, saver);
		
		sut.upgradeTo(validUpgradeRequest(), mockTransaction);
		
		verify(saver);
	}



	private UpgradeRequest validUpgradeRequest() {
		
		upgradeRequest.setNewUsers(1);
		return upgradeRequest;
	}
	
	
	
	
	@Test(expected=IllegalArgumentException.class)
	public void should_response_throw_invaild_argument_exception_if_there_is_no_user_increase_set_on_price_check() throws Exception {
		upgradeRequest.setNewUsers(null);
		new IncreaseEmployeeLimitHandlerImpl(null, null, null).priceForUpgrade(upgradeRequest);
 	}
	
	@Test(expected=IllegalArgumentException.class)
	public void should_response_throw_invaild_argument_exception_if_the_user_increase_is_less_than_1_on_price_check() throws Exception {
		upgradeRequest.setNewUsers(0);
		new IncreaseEmployeeLimitHandlerImpl(null, null, null).priceForUpgrade(upgradeRequest);
 	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void should_response_throw_invaild_argument_exception_if_there_is_no_user_increase_set_on_upgrade() throws Exception {
		upgradeRequest.setNewUsers(null);
		new IncreaseEmployeeLimitHandlerImpl(null, null, null).upgradeTo(upgradeRequest, null);
 	}
	
	@Test(expected=IllegalArgumentException.class)
	public void should_response_throw_invaild_argument_exception_if_the_user_increase_is_less_than_1_on_upgrade() throws Exception {
		upgradeRequest.setNewUsers(0);
		new IncreaseEmployeeLimitHandlerImpl(null, null, null).upgradeTo(upgradeRequest, null);
 	}
	
	private SubscriptionAgent subScriptionAgentForSuccessfulUpgrade() throws CommunicationException {
		return createSubscriptionAgentForUpgrade(true);
	}
	


	private SubscriptionAgent createSubscriptionAgentForUpgrade(boolean upgradeResult) throws CommunicationException {
		SubscriptionAgent subscriptionAgent = createMock(SubscriptionAgent.class);
		UpgradeResponse upgradeResponse = upgradeResult ? new UpgradeResponse(new UpgradeCost(1L, 1L, ""), 1L) : null;
		expect(subscriptionAgent.upgrade((UpgradeSubscription)anyObject())).andReturn(upgradeResponse);
		replay(subscriptionAgent);
		return subscriptionAgent;
	}
	
	private SubscriptionAgent createSubscriptionAgentForUpgrade(UpgradeCost upgradeCost) throws CommunicationException {
		SubscriptionAgent subscriptionAgent = createMock(SubscriptionAgent.class);
		expect(subscriptionAgent.costToUpgradeTo((UpgradeSubscription)anyObject())).andReturn(upgradeCost);
		replay(subscriptionAgent);
		return subscriptionAgent;
	}
	
	private OrgSaver successfulOrgSaver(PrimaryOrg primaryOrg) {
		OrgSaver orgSaver = createMock(OrgSaver.class);
		expect(orgSaver.update(mockTransaction, primaryOrg)).andReturn(primaryOrg);
		replay(orgSaver);
		return orgSaver;
	}
}
