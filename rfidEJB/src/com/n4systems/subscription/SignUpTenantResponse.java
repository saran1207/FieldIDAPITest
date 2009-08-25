package com.n4systems.subscription;

public interface SignUpTenantResponse extends Response {
	
	public Tenant getTenant();
	public Client getClient();
	public Subscription getSubscription();
	
}
