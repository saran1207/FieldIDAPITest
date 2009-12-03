package com.n4systems.subscription;

import java.util.List;


public abstract class SubscriptionAgent {

	
	public abstract SignUpTenantResponse buy(Subscription subscription, Company company, Person person) throws CommunicationException, BillingInfoException;
	
	public abstract ValidatePromoCodeResponse validatePromoCode(String code) throws CommunicationException;
	
	public abstract PriceCheckResponse priceCheck(Subscription subscription) throws CommunicationException;
	
	public abstract List<ContractPrice> retrieveContractPrices() throws CommunicationException;
	
	public abstract Response attachNote(Long tenantExternalId, String title, String note) throws CommunicationException;
	
	public abstract UpgradeResponse upgrade(UpgradeSubscription upgradeSubscription) throws CommunicationException;
	
	public abstract CurrentSubscription currentSubscriptionFor(Long tenantExternalId) throws CommunicationException;
	
	public abstract UpgradeCost costToUpgradeTo(UpgradeSubscription upgradeSubscription) throws CommunicationException;
		
	
}
