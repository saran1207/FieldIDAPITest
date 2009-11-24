package com.n4systems.subscription.netsuite.model;

public class UpgradeSubscriptionResponse extends BaseResponse {
	
	private String next_payment_date;
	private UpgradeSubscriptionPaymentDetails upgradesubscription;

	public String getNext_payment_date() {
		return next_payment_date;
	}
	public void setNext_payment_date(String nextPaymentDate) {
		next_payment_date = nextPaymentDate;
	}
	public UpgradeSubscriptionPaymentDetails getUpgradesubscription() {
		return upgradesubscription;
	}
	public void setUpgradesubscription(UpgradeSubscriptionPaymentDetails upgradesubscription) {
		this.upgradesubscription = upgradesubscription;
	}
}
