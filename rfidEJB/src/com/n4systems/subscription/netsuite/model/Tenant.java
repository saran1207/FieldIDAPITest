package com.n4systems.subscription.netsuite.model;

public class Tenant {
	private String companyName;
	private String phone;
	private String email;	
	private AddressInfo billingAddress;
	private AddressInfo shippingAddress;
	private String fieldId;
	private String url;
	private boolean usingCreditCard;
	private CreditCard creditCard;
	private String nsrecordid;

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
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getNsrecordid() {
		return nsrecordid;
	}
	public void setNsrecordid(String nsrecordid) {
		this.nsrecordid = nsrecordid;
	}
	
}
