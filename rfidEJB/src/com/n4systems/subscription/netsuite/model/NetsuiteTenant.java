package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.AddressInfo;
import com.n4systems.subscription.CreditCard;
import com.n4systems.subscription.ExternalIdResponse;
import com.n4systems.subscription.Company;

public class NetsuiteTenant implements Company, ExternalIdResponse {
	
	private String companyName;
	private String phone;
	private String email;	
	private AddressInfo billingAddress;
	private AddressInfo shippingAddress;
	private String n4Id;
	private String url;
	private boolean usingCreditCard;
	private CreditCard creditCard;
	private Long nsrecordid;
	private String purchaseOrderNumber;
	private String externalPassword;

	public Long getExternalId() {
		return getNsrecordid();
	}

	public Long getNsrecordid() {
		return nsrecordid;
	}

	public void setNsrecordid(Long nsrecordid) {
		this.nsrecordid = nsrecordid;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AddressInfo getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(AddressInfo billingAddress) {
		this.billingAddress = billingAddress;
	}

	public AddressInfo getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(AddressInfo shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getCompanyN4Id() {
		return n4Id;
	}

	public void setN4Id(String n4Id) {
		this.n4Id = n4Id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getExternalPassword() {
		return externalPassword;
	}

	public void setExternalPassword(String externalPassword) {
		this.externalPassword = externalPassword;
	}
}
