package com.n4systems.handlers.creator.signup;

import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.subscription.CreditCard;
import com.n4systems.subscription.PaymentFrequency;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.UpgradeSubscription;

public class UpgradeRequest implements UpgradeSubscription, Subscription {

	private Long tenantExternalId;
	private Long contractExternalId;
	private Integer newUsers = 0;
	private Integer storageIncrement = 0;
	
	private boolean usingCreditCard;
	private CreditCard creditCard;
	private String purchaseOrderNumber = "";
	
	private SignUpPackageDetails upgradePackage;
	
	private PaymentFrequency frequency;
	
	private Integer months;
	
	private String promoCode;
	
	private Integer users;
	
	private boolean purchasingPhoneSupport;
	
	private boolean updatedBillingInformation;
	
	
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
	public Subscription getSubscription() {
		return this;
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
		
		this.purchaseOrderNumber = (purchaseOrderNumber != null) ? purchaseOrderNumber : "";
	}
	public SignUpPackageDetails getUpgradePackage() {
		return upgradePackage;
	}
	public void setUpgradePackage(SignUpPackageDetails upgradePackage) {
		this.upgradePackage = upgradePackage;
	}
	public void setUpdatedBillingInformation(boolean updatedBillingInformation) {
		this.updatedBillingInformation = updatedBillingInformation;
	}
	public boolean isUpdatedBillingInformation() {
		return updatedBillingInformation;
	}
	public PaymentFrequency getFrequency() {
		return frequency;
	}
	public void setFrequency(PaymentFrequency frequency) {
		this.frequency = frequency;
	}
	public Integer getMonths() {
		return months;
	}
	public void setMonths(Integer months) {
		this.months = months;
	}
	public String getPromoCode() {
		return promoCode;
	}
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	public Integer getUsers() {
		return users;
	}
	public void setUsers(Integer users) {
		this.users = users;
	}
	public boolean isPurchasingPhoneSupport() {
		return purchasingPhoneSupport;
	}
	public void setPurchasingPhoneSupport(boolean purchasingPhoneSupport) {
		this.purchasingPhoneSupport = purchasingPhoneSupport;
	}
	
	
}
