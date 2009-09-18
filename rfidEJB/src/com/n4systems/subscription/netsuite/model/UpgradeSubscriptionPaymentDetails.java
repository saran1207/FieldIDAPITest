package com.n4systems.subscription.netsuite.model;

public class UpgradeSubscriptionPaymentDetails {

	private float upgrade_cost;
	private float next_payment;

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
}
