package com.n4systems.subscription;


public interface UpgradeSubscription {

	public Long getTenantExternalId();

	public Long getContractExternalId();

	public Integer getNewUsers();

	public Integer getStorageIncrement();

	public Subscription getSubscription();

	public CreditCard getCreditCard();

	public boolean isUsingCreditCard();

	public String getPurchaseOrderNumber();
	
	public boolean isUpdatedBillingInformation();

}