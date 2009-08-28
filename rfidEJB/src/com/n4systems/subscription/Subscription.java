package com.n4systems.subscription;


public interface Subscription {
	
	public Long getExternalId();
	public Integer getMonths();
	public Integer getUsers();
	public PaymentFrequency getFrequency();
	public String getPromoCode();
	public boolean isPurchasingPhoneSupport();
}
