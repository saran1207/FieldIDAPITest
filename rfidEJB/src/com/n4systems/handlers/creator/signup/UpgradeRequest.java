package com.n4systems.handlers.creator.signup;

import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.subscription.CreditCard;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.UpgradeSubscription;

public class UpgradeRequest implements UpgradeSubscription {

	private Long tenantExternalId;
	private Long contractExternalId;
	private Integer newUsers = 0;
	private Integer storageIncrement = 0;
	private boolean showPriceOnly;
	
	private Subscription subscription;
	private boolean usingCreditCard;
	private CreditCard creditCard;
	private String purchaseOrderNumber;
	
	private SignUpPackageDetails upgradePackage;
	
	
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
	public Subscription getSubscription() {
		return subscription;
	}
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	public boolean isUsingCreditCard() {
		return usingCreditCard;
	}
	public void setUsingCreditCard(boolean usingCreditCard) {
		this.usingCreditCard = usingCreditCard;
	}
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}
	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}
	public SignUpPackageDetails getUpgradePackage() {
		return upgradePackage;
	}
	public void setUpgradePackage(SignUpPackageDetails upgradePackage) {
		this.upgradePackage = upgradePackage;
	}
	
}
