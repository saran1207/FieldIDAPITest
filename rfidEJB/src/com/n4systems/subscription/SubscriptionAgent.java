package com.n4systems.subscription;


public abstract class SubscriptionAgent {

	public abstract SignUpTenantResponse buy(Subscription subscription, Company company, Person person) throws CommunicationException, BillingInfoException;
	
	public abstract ValidatePromoCodeResponse validatePromoCode(String code) throws CommunicationException; 
}
