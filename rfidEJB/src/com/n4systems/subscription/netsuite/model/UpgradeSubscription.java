package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.CreditCard;


public class UpgradeSubscription {

	private Long tenantExternalId;
	private Long contractExternalId;
	private Integer newUsers = 0;
	private Integer storageIncrement = 0;
	private boolean showPriceOnly;
	
	private NetsuiteSubscription subscription;
	private boolean usingCreditCard;
	private CreditCard creditCard;
	
	public Long getTenantExternalId() {
		return tenantExternalId;
	}
	public void setTenantExternalId(Long tenantExternalId) {
		this.tenantExternalId = tenantExternalId;
	}
	public Long getContractExternalId() {
		return contractExternalId;
	}
	public void setContractExternalId(Long contractExternalId) {
		this.contractExternalId = contractExternalId;
	}
	public Integer getNewUsers() {
		return newUsers;
	}
	public void setNewUsers(Integer newUsers) {
		this.newUsers = newUsers;
	}
	public Integer getStorageIncrement() {
		return storageIncrement;
	}
	public void setStorageIncrement(Integer storageIncrement) {
		this.storageIncrement = storageIncrement;
	}
	public boolean isShowPriceOnly() {
		return showPriceOnly;
	}
	public void setShowPriceOnly(boolean showPriceOnly) {
		this.showPriceOnly = showPriceOnly;
	}
	public NetsuiteSubscription getSubscription() {
		return subscription;
	}
	public void setSubscription(NetsuiteSubscription subscription) {
		this.subscription = subscription;
	}
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	public boolean isUsingCreditCard() {
		return usingCreditCard;
	}
	public void setUsingCreditCard(boolean usingCreditCard) {
		this.usingCreditCard = usingCreditCard;
	}

}
