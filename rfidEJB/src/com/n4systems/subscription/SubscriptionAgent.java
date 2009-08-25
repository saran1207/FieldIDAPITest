package com.n4systems.subscription;


public abstract class SubscriptionAgent {

	public abstract SignUpTenantResponse buy(Subscription subscription, Company company, Person client);
	
	
}
