package com.n4systems.subscription;

public interface SubscriptionDetails {

	public Long getContractId();
	public String getAccountType();
	public String getStartDate();
	public String getEndDate();
	public String getNextBillingDate();
	public String getStorage();
	public String getPhoneSupport();
	
}
