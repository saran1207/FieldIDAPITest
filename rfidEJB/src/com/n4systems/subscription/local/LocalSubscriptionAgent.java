package com.n4systems.subscription.local;

import com.n4systems.subscription.Person;
import com.n4systems.subscription.Company;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.SubscriptionAgent;

public class LocalSubscriptionAgent extends SubscriptionAgent {

	@Override
	public SignUpTenantResponse buy(Subscription subscription, Company company, Person client) {
		return null;
	}

	

}
