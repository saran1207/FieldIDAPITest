package com.n4systems.subscription;


public interface Subscription {
	
	public Long getContractId();
	public Integer getMonths();
	public Integer getUsers();
	public PaymentFrequency getFrequency();
	public String getPromoCode();
	public boolean isPurchasingPhoneSupport();
}
