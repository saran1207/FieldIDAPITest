package com.n4systems.subscription.netsuite.client;

import com.n4systems.subscription.Company;
import com.n4systems.subscription.Person;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.netsuite.model.NetsuiteSignUpTenantResponse;

public class SignUpTenantClient extends AbstractNetsuiteClient<NetsuiteSignUpTenantResponse> {
	private final boolean TESTING_MODE = true;
	
	private Company company;
	private Person person;
	private Subscription subscription;
	
	public SignUpTenantClient() {
		super(NetsuiteSignUpTenantResponse.class, "signuptenant");
	}
	
	@Override
	protected void addRequestParameters() {
		addRequestParameter("companyname", company.getCompanyName());
		addRequestParameter("n4id", company.getCompanyN4Id());
		
		if (company.getPhone() != null) {
			addRequestParameter("phone", company.getPhone());
		}
		
		addRequestParameter("email", company.getEmail());		
		addRequestParameter("billingaddressline1", company.getBillingAddress().getAddressLine1());
		
		if (company.getBillingAddress().getAddressLine2() != null) {
			addRequestParameter("billingaddressline2", company.getBillingAddress().getAddressLine2());
		}
		
		addRequestParameter("billingcity", company.getBillingAddress().getCity());
		addRequestParameter("billingstate", company.getBillingAddress().getState());
		addRequestParameter("billingcountry", company.getBillingAddress().getCountry());
		addRequestParameter("billingpostal", company.getBillingAddress().getPostal());
		addRequestParameter("shippingaddressline1", company.getShippingAddress().getAddressLine1());
		
		if (company.getShippingAddress().getAddressLine2() != null) {
			addRequestParameter("shippingaddressline2", company.getShippingAddress().getAddressLine2());
		}
		
		addRequestParameter("shippingcity", company.getShippingAddress().getCity());
		addRequestParameter("shippingstate", company.getShippingAddress().getState());
		addRequestParameter("shippingcountry", company.getShippingAddress().getCountry());
		addRequestParameter("shippingpostal", company.getShippingAddress().getPostal());
		
		if (company.getUrl() != null) {
			addRequestParameter("url", company.getUrl());
		}
				
		addRequestParameter("prefercc", company.isUsingCreditCard() ? "T" : "F");
		if (company.isUsingCreditCard()) {
			addRequestParameter("ccnumber", company.getCreditCard().getNumber());
			addRequestParameter("cctype", company.getCreditCard().getType().getCode());
			addRequestParameter("ccexp", company.getCreditCard().getExpiry());
			addRequestParameter("ccname", company.getCreditCard().getName());
			
		}
		
		addRequestParameter("firstname", person.getFirstName());
		addRequestParameter("lastname", person.getLastName());
		addRequestParameter("clientn4id", person.getUserN4Id());
		
		addRequestParameter("itemid", String.valueOf(subscription.getContractExternalId()));
		addRequestParameter("months", String.valueOf(subscription.getMonths()));
		addRequestParameter("frequency", subscription.getFrequency().getCode());
		addRequestParameter("numusers", String.valueOf(subscription.getUsers()));
		addRequestParameter("phonesupport", subscription.isPurchasingPhoneSupport() ? "T" : "F");
		if (subscription.getPromoCode() != null) {
			addRequestParameter("promocode", subscription.getPromoCode());
		}		
		
		if (TESTING_MODE) {
			addRequestParameter("testingsignup", "T");
		}
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
}
