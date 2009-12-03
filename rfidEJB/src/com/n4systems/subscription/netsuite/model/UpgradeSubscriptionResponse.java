package com.n4systems.subscription.netsuite.model;

public class UpgradeSubscriptionResponse extends BaseResponse {
	
	private UpgradeSubscriptionPaymentDetails upgradesubscription;

	public UpgradeSubscriptionPaymentDetails getUpgradesubscription() {
		return upgradesubscription;
	}
	public void setUpgradesubscription(UpgradeSubscriptionPaymentDetails upgradesubscription) {
		this.upgradesubscription = upgradesubscription;
	}
}
