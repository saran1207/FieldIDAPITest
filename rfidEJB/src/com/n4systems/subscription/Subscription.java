package com.n4systems.subscription;


public interface Subscription {
	
	public String getSyncId();
	public Integer getMonths();
	public Integer getUsers();
	public PaymentFrequency getFrequency();
	public String getPromoCode();
	public boolean isPurchasingPhoneSupport();
}
