package com.n4systems.subscription;

public abstract class SubscriptionAgent {

	public abstract SignUpTenantResponse buy(SignUpPriceModifier priceModifier, AccountCreationInformation accountInformation, BillingInformation billingInformation);
	
	
}
