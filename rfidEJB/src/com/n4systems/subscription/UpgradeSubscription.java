package com.n4systems.subscription;


public interface UpgradeSubscription {

	public Long getTenantExternalId();

	public Long getContractExternalId();

	public Integer getNewUsers();

	public Integer getStorageIncrement();

	public boolean isShowPriceOnly();

	public Subscription getSubscription();

	public CreditCard getCreditCard();

	public boolean isUsingCreditCard();

	public String getPurchaseOrderNumber();

}