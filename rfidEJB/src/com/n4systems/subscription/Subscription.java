package com.n4systems.subscription;


public interface Subscription {
	
	public Long getExternalId();
	public int getMonths();
	public int getUsers();
	public PaymentFrequency getFrequency();
	public String getPromoCode();
	public boolean isPurchasingPhoneSupport();
}
