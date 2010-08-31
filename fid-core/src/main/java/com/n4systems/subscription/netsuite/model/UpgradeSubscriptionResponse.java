package com.n4systems.subscription.netsuite.model;

public class UpgradeSubscriptionResponse extends BaseResponse {
	
	
	private float upgrade_cost;
	private float next_payment;
	private String next_payment_date;
	private UpgradeSubscriptionPaymentDetails upgradesubscription;
	
	public UpgradeSubscriptionPaymentDetails getUpgradesubscription() {
		return upgradesubscription;
	}
	
	public void setUpgradesubscription(UpgradeSubscriptionPaymentDetails upgradesubscription) {
		this.upgradesubscription = upgradesubscription;
	}

	public float getUpgrade_cost() {
		return upgrade_cost;
	}

	public void setUpgrade_cost(float upgradeCost) {
		upgrade_cost = upgradeCost;
	}

	public float getNext_payment() {
		return next_payment;
	}

	public void setNext_payment(float nextPayment) {
		next_payment = nextPayment;
	}

	public String getNext_payment_date() {
		return next_payment_date;
	}

	public void setNext_payment_date(String nextPaymentDate) {
		next_payment_date = nextPaymentDate;
	}
	
	
	
}
