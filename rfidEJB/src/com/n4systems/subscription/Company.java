package com.n4systems.subscription;


public interface Company {

	public String getCompanyName();
	public String getPhone();
	public String getEmail();
	public AddressInfo getBillingAddress();
	public AddressInfo getShippingAddress();
	public String getN4Id();
	public String getUrl();
	public boolean isUsingCreditCard();
	public CreditCard getCreditCard();
}
