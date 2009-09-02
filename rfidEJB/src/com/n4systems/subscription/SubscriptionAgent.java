package com.n4systems.subscription;

import java.util.List;


public abstract class SubscriptionAgent {

	public abstract SignUpTenantResponse buy(Subscription subscription, Company company, Person person) throws CommunicationException, BillingInfoException;
	
	public abstract ValidatePromoCodeResponse validatePromoCode(String code) throws CommunicationException;
	
	public abstract PriceCheckResponse priceCheck(Subscription subscription) throws CommunicationException;
	
	public abstract List<ContractPrice> retrieveContractPrices() throws CommunicationException;
}
