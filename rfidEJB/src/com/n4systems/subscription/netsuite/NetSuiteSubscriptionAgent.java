package com.n4systems.subscription.netsuite;

import java.io.IOException;

import com.n4systems.subscription.BillingInfoException;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.Company;
import com.n4systems.subscription.Person;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.ValidatePromoCodeResponse;
import com.n4systems.subscription.netsuite.client.SignUpTenantClient;
import com.n4systems.subscription.netsuite.client.ValidatePromoCodeClient;
import com.n4systems.subscription.netsuite.model.NetSuiteValidatePromoCodeResponse;

public class NetSuiteSubscriptionAgent extends SubscriptionAgent {

	private final SignUpTenantClient signUpTenantClient;
	private final ValidatePromoCodeClient validatePromoCodeClient;
	
	public NetSuiteSubscriptionAgent() {
		signUpTenantClient = new SignUpTenantClient();
		validatePromoCodeClient = new ValidatePromoCodeClient();
	}
	
	@Override
	public SignUpTenantResponse buy(Subscription subscription, Company company,Person person) throws CommunicationException, BillingInfoException {
		signUpTenantClient.setSubscription(subscription);
		signUpTenantClient.setCompany(company);
		signUpTenantClient.setPerson(person);
		
		SignUpTenantResponse response = null;
		
		try {
			response = signUpTenantClient.execute();
		} catch (IOException e) {
			throw new CommunicationException();
		}
		
		return response;
	}

	@Override
	public ValidatePromoCodeResponse validatePromoCode(String code)	throws CommunicationException {
		validatePromoCodeClient.setCode(code);
		
		NetSuiteValidatePromoCodeResponse response = null;
		
		try {
			response = validatePromoCodeClient.execute();
		} catch (IOException e) {
			throw new CommunicationException();
		}
		
		return response;
	}

}
