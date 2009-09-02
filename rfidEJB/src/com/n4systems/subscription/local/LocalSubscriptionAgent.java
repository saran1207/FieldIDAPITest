package com.n4systems.subscription.local;

import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.Company;
import com.n4systems.subscription.Person;
import com.n4systems.subscription.PriceCheckResponse;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.ValidatePromoCodeResponse;

public class LocalSubscriptionAgent extends SubscriptionAgent {

	@Override
	public SignUpTenantResponse buy(Subscription subscription, Company company, Person client) {
		return new LocalSignUpTenantResponse();
	}

	@Override
	public ValidatePromoCodeResponse validatePromoCode(String code)	throws CommunicationException {
		return new LocalValidatePromoCodeResponse();
	}

	
	@Override
	public PriceCheckResponse priceCheck(Subscription subscription)	throws CommunicationException {
		if (subscription.getFrequency() == null || subscription.getMonths() == null || 
				subscription.getContractId() == null || subscription.getUsers() < 1) {
			throw new RuntimeException();
		}
		
		
		LocalPriceCheckResponse priceCheckResponse = new LocalPriceCheckResponse(new LocalPricing());
		return priceCheckResponse;
	}

}
