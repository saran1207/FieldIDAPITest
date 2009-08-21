package com.n4systems.netsuite.client;

import com.n4systems.netsuite.model.Client;
import com.n4systems.netsuite.model.SignUpTenantResponse;
import com.n4systems.netsuite.model.Subscription;
import com.n4systems.netsuite.model.Tenant;

public class SignUpTenantClient extends AbstractNetsuiteClient<SignUpTenantResponse>{

	private Tenant tenant;
	private Client client;
	private Subscription subscription;
	
	public SignUpTenantClient() {
		super(SignUpTenantResponse.class, "signuptenant");
	}
	
	@Override
	protected void addRequestParameters() {
		addRequestParameter("companyname", tenant.getCompanyName());
		addRequestParameter("fieldid", tenant.getFieldId());
		
		if (tenant.getPhone() != null) {
			addRequestParameter("phone", tenant.getPhone());
		}
		
		addRequestParameter("email", tenant.getEmail());		
		addRequestParameter("billingaddressline1", tenant.getBillingAddress().getAddressLine1());
		
		if (tenant.getBillingAddress().getAddressLine2() != null) {
			addRequestParameter("billingaddressline2", tenant.getBillingAddress().getAddressLine2());
		}
		
		addRequestParameter("billingcity", tenant.getBillingAddress().getCity());
		addRequestParameter("billingstate", tenant.getBillingAddress().getState());
		addRequestParameter("billingcountry", tenant.getBillingAddress().getCountry());
		addRequestParameter("billingpostal", tenant.getBillingAddress().getPostal());
		addRequestParameter("shippingaddressline1", tenant.getShippingAddress().getAddressLine1());
		
		if (tenant.getShippingAddress().getAddressLine2() != null) {
			addRequestParameter("shippingaddressline2", tenant.getShippingAddress().getAddressLine2());
		}
		
		addRequestParameter("shippingcity", tenant.getShippingAddress().getCity());
		addRequestParameter("shippingstate", tenant.getShippingAddress().getState());
		addRequestParameter("shippingcountry", tenant.getShippingAddress().getCountry());
		addRequestParameter("shippingpostal", tenant.getShippingAddress().getPostal());
		
		if (tenant.getUrl() != null) {
			addRequestParameter("url", tenant.getUrl());
		}
				
		addRequestParameter("prefercc", tenant.isUsingCreditCard() ? "T" : "F");
		if (tenant.isUsingCreditCard()) {
			addRequestParameter("ccnumber", tenant.getCreditCard().getNumber());
			addRequestParameter("cctype", tenant.getCreditCard().getType().getCode());
			addRequestParameter("ccexp", tenant.getCreditCard().getExpiry());
			addRequestParameter("ccname", tenant.getCreditCard().getName());
			
		}
		
		addRequestParameter("firstname", client.getFirstName());
		addRequestParameter("lastname", client.getLastName());
		addRequestParameter("clientfieldid", client.getFieldId());
		
		addRequestParameter("itemid", String.valueOf(subscription.getNetsuiteRecordId()));
		addRequestParameter("months", String.valueOf(subscription.getMonths()));
		addRequestParameter("frequency", subscription.getFrequency().getCode());
		addRequestParameter("numusers", String.valueOf(subscription.getUsers()));
		addRequestParameter("phonesupport", subscription.isPurchasingPhoneSupport() ? "T" : "F");
		if (subscription.getReferralCode() != null) {
			addRequestParameter("referralcode", subscription.getReferralCode());
		}		
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

}
