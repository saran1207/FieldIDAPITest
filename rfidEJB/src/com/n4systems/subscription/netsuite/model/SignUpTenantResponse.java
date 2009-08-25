package com.n4systems.subscription.netsuite.model;

public class SignUpTenantResponse extends AbstractResponse {

	private Tenant tenant;
	private Client client;
	private Subscription subscription;

	public Tenant getTenant() {
		return tenant;
	}
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Subscription getSubscription() {
		return subscription;
	}
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}	
}
