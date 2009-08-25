package com.n4systems.subscription;

import com.n4systems.subscription.netsuite.model.PaymentFrequency;

public abstract class Subscription {

	private int months;
	private int users;
	private PaymentFrequency frequency;
	private String referralCode;
	private boolean purchasingPhoneSupport;
	
	public abstract Long getExternalId();
	public abstract void setExternalId(Long externalId);
	
	public int getMonths() {
		return months;
	}
	public void setMonths(int months) {
		this.months = months;
	}
	public int getUsers() {
		return users;
	}
	public void setUsers(int users) {
		this.users = users;
	}
	public PaymentFrequency getFrequency() {
		return frequency;
	}
	public void setFrequency(PaymentFrequency frequency) {
		this.frequency = frequency;
	}
	public String getReferralCode() {
		return referralCode;
	}
	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}
	public boolean isPurchasingPhoneSupport() {
		return purchasingPhoneSupport;
	}
	public void setPurchasingPhoneSupport(boolean purchasingPhoneSupport) {
		this.purchasingPhoneSupport = purchasingPhoneSupport;
	}
}
