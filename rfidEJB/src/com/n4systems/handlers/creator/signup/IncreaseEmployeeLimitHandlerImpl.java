
package com.n4systems.handlers.creator.signup;

import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.BillingInfoException;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.subscription.UpgradeResponse;

public class IncreaseEmployeeLimitHandlerImpl implements UpgradeAccountHandler {

	
	private final PrimaryOrg primaryOrg;
	private final SubscriptionAgent subscriptionAgent;
	private final OrgSaver saver;

	public IncreaseEmployeeLimitHandlerImpl(PrimaryOrg primaryOrg, SubscriptionAgent subscriptionAgent, OrgSaver saver) {
		super();
		this.primaryOrg = primaryOrg;
		this.subscriptionAgent = subscriptionAgent;
		this.saver = saver;
	}
	

	public UpgradeCost priceForUpgrade(UpgradeRequest upgradeRequest) throws CommunicationException {
		gaurd(upgradeRequest);
		
		return subscriptionAgent.costToUpgradeTo(upgradeRequest);
	}


	


	@Override
	public UpgradeResponse upgradeTo(UpgradeRequest upgradeRequest, Transaction transaction) throws CommunicationException, UpgradeCompletionException, BillingInfoException {
		gaurd(upgradeRequest);
		UpgradeResponse upradeResponse = subscriptionAgent.upgrade(upgradeRequest);
		
		try {
			applyEmployeeIncrease(upgradeRequest, transaction);
		} catch (Exception e) {
			throw new UpgradeCompletionException(upradeResponse, "Could not increase the number of users.", e);
		}
		
		return upradeResponse;
	}


	private void applyEmployeeIncrease(UpgradeRequest upgradeRequest, Transaction transaction) {
		primaryOrg.getLimits().setUsers(primaryOrg.getLimits().getUsers() + upgradeRequest.getNewUsers());
		
		saver.update(transaction, primaryOrg);
	}


	private void gaurd(UpgradeRequest upgradeRequest) {
		if (isRequestValid(upgradeRequest)) {
			throw new IllegalArgumentException("the increase of users is required");
		}
	}
	
	private boolean isRequestValid(UpgradeRequest upgradeRequest) {
		return upgradeRequest.getNewUsers() == null || upgradeRequest.getNewUsers() < 1;
	}


}
