package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.ExternalIdResponse;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;

public class NetsuiteSignUpTenantResponse extends AbstractResponse implements SignUpTenantResponse {

	private NetsuiteTenant tenant;
	private NetsuiteClient client;
	private NetsuiteSubscription subscription;	

	public ExternalIdResponse getTenant() {
		return tenant;
	}
	public void setTenant(NetsuiteTenant tenant) {
		this.tenant = tenant;
	}
	public ExternalIdResponse getClient() {
		return client;
	}
	public void setClient(NetsuiteClient client) {
		this.client = client;
	}
	public Subscription getSubscription() {
		return subscription;
	}
	public void setSubscription(NetsuiteSubscription subscription) {
		this.subscription = subscription;
	}	
}
