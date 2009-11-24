package com.n4systems.subscription.netsuite;

import java.io.IOException;
import java.util.List;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.subscription.BillingInfoException;
import com.n4systems.subscription.BillingInfoField;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.Company;
import com.n4systems.subscription.ContractPrice;
import com.n4systems.subscription.Person;
import com.n4systems.subscription.PriceCheckResponse;
import com.n4systems.subscription.Response;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.subscription.UpgradeSubscription;
import com.n4systems.subscription.ValidatePromoCodeResponse;
import com.n4systems.subscription.netsuite.client.PricingDetailsClient;
import com.n4systems.subscription.netsuite.client.ProductDetailsClient;
import com.n4systems.subscription.netsuite.client.SignUpTenantClient;
import com.n4systems.subscription.netsuite.client.SubscriptionDetailsClient;
import com.n4systems.subscription.netsuite.client.UpgradeSubscriptionClient;
import com.n4systems.subscription.netsuite.client.UploadNoteClient;
import com.n4systems.subscription.netsuite.client.ValidatePromoCodeClient;
import com.n4systems.subscription.netsuite.model.GetPricingDetailsResponse;
import com.n4systems.subscription.netsuite.model.GetSubscriptionDetailsResponse;
import com.n4systems.subscription.netsuite.model.NetSuiteValidatePromoCodeResponse;
import com.n4systems.subscription.netsuite.model.NetsuiteSignUpTenantResponse;
import com.n4systems.subscription.netsuite.model.UpgradeSubscriptionResponse;

public class NetSuiteSubscriptionAgent extends SubscriptionAgent {

	private final SignUpTenantClient signUpTenantClient;
	private final ValidatePromoCodeClient validatePromoCodeClient;
	private final PricingDetailsClient pricingDetailsClient;
	private final ProductDetailsClient productDetailsClient;
	
	public NetSuiteSubscriptionAgent() {
		signUpTenantClient = new SignUpTenantClient();
		validatePromoCodeClient = new ValidatePromoCodeClient();
		pricingDetailsClient = new PricingDetailsClient();
		productDetailsClient = new ProductDetailsClient();
	}
	
	@Override
	public SignUpTenantResponse buy(Subscription subscription, Company company,Person person) throws CommunicationException, BillingInfoException {
		signUpTenantClient.setSubscription(subscription);
		signUpTenantClient.setCompany(company);
		signUpTenantClient.setPerson(person);
		
		NetsuiteSignUpTenantResponse response = null;
		
		try {
			response = signUpTenantClient.execute();
		} catch (IOException e) {
			throw new CommunicationException();
		}
				
		BillingInfoField problemField = BillingInfoField.UNKOWN;
		if (response.getResult().equals("NOTOK")) {
			if (response.getDetails() != null) {
				if (response.getDetails().contains("(email)")) {
					problemField = BillingInfoField.EMAIL; 
				} else if (response.getDetails().contains("ccnumber")) {
					problemField = BillingInfoField.CC_NUMBER;
				}
			}
			
			throw new BillingInfoException(problemField);
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

	@Override
	public PriceCheckResponse priceCheck(Subscription subscription)	throws CommunicationException {
		pricingDetailsClient.setSubscription(subscription);
		
		GetPricingDetailsResponse response = null;
		
		try {
			response = pricingDetailsClient.execute();
		} catch (IOException e) {
			throw new CommunicationException();
		}
		
		return response;
	}

	@Override
	public List<ContractPrice> retrieveContractPrices()	throws CommunicationException {
		NetSuiteContractPriceHandler contractPriceHandler = new NetSuiteContractPriceHandler(productDetailsClient, pricingDetailsClient);
		return contractPriceHandler.retrieveContractPrices();
	}

	@Override
	public Response attachNote(Long tenantExternalId, String title, String note) throws CommunicationException {
		UploadNoteClient uploadNoteClient = new UploadNoteClient();
		uploadNoteClient.setTenantId(tenantExternalId);
		uploadNoteClient.setTitle(title);
		uploadNoteClient.setNote(note);
		
		Response response = null;
		try {
			response = uploadNoteClient.execute();
		} catch (IOException e) {
			throw new CommunicationException();
		}
		
		return response;
	}

	
	
	@Override
	public boolean upgrade(UpgradeSubscription upgradeSubscription) throws CommunicationException {
		UpgradeSubscriptionClient upgradeClient = new UpgradeSubscriptionClient();
		upgradeClient.setUpgradeSubscription(upgradeSubscription);
		
		UpgradeSubscriptionResponse response = null;
		
		try {
			response = upgradeClient.execute();
		} catch (IOException e) {
			throw new CommunicationException();
		}
		
		
		return response != null ? response.getResult().equals("OK") : false;
	}

	@Override
	public Long contractIdFor(Long tenantExternalId) throws CommunicationException {
		SubscriptionDetailsClient detailsClient = new SubscriptionDetailsClient();
		detailsClient.setTenantExternalId(tenantExternalId);
				
		GetSubscriptionDetailsResponse response = null;
		try {
			response = detailsClient.execute();
		} catch (IOException e) {
			throw new CommunicationException();
		}
		
		Long contractId = response.getSubscription() != null ? response.getSubscription().getContractId() : null;
		
		return contractId;
	}

	@Override
	public UpgradeCost costToUpgradeTo(UpgradeSubscription upgradeSubscription) throws CommunicationException {
		throw new NotImplementedException();
	}


	
	
	
}
