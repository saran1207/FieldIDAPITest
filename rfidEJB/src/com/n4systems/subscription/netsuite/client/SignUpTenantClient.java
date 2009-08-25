package com.n4systems.subscription.netsuite.client;

import com.n4systems.subscription.netsuite.model.NetsuiteClient;
import com.n4systems.subscription.netsuite.model.NetsuiteSubscription;
import com.n4systems.subscription.netsuite.model.NetsuiteTenant;
import com.n4systems.subscription.netsuite.model.NetsuiteSignUpTenantResponse;

public class SignUpTenantClient extends AbstractNetsuiteClient<NetsuiteSignUpTenantResponse> implements com.n4systems.subscription.SignUpTenantClient {

	private NetsuiteTenant tenant;
	private NetsuiteClient client;
	private NetsuiteSubscription subscription;
	
	public SignUpTenantClient() {
		super(NetsuiteSignUpTenantResponse.class, "signuptenant");
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

	public void setTenant(NetsuiteTenant tenant) {
		this.tenant = tenant;
	}

	public void setClient(NetsuiteClient client) {
		this.client = client;
	}

	public void setSubscription(NetsuiteSubscription subscription) {
		this.subscription = subscription;
	}

}
