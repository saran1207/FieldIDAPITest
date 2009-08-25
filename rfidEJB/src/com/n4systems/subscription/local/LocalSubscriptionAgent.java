package com.n4systems.subscription.local;

import com.n4systems.subscription.AccountCreationInformation;
import com.n4systems.subscription.BillingInformation;
import com.n4systems.subscription.SignUpPriceModifier;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.SubscriptionAgent;

public class LocalSubscriptionAgent extends SubscriptionAgent {

	@Override
	public SignUpTenantResponse buy(SignUpPriceModifier priceModifier, AccountCreationInformation accountInformation, BillingInformation billingInformation) {
		
		return null;
	}


}
