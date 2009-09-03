package com.n4systems.subscription;


public interface Subscription {
	
	public Long getContractExternalId();
	public Integer getMonths();
	public Integer getUsers();
	public PaymentFrequency getFrequency();
	public String getPromoCode();
	public boolean isPurchasingPhoneSupport();
}
