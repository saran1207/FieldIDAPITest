package com.n4systems.subscription.netsuite.client;


public class UpgradeSubscriptionCostClient extends UpgradeSubscriptionClient {

	public UpgradeSubscriptionCostClient() {
		super();
	}

	@Override
	protected void addUpgradePriceOnlyFlag() {
		addRequestParameter("showpriceonly", "T");
	}
	
	@Override
	protected void applyBillingInformation() {
	}
	

}
