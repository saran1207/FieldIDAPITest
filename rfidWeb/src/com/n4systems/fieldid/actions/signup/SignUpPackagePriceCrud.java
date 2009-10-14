package com.n4systems.fieldid.actions.signup;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.handlers.creator.signup.model.SignUpRequest;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.signuppackage.SignUpPackageLoader;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.PriceCheckResponse;
import com.n4systems.subscription.SubscriptionAgent;

public class SignUpPackagePriceCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SignUpPackagePriceCrud.class);
	
	private SignUpRequest subscription = new SignUpRequest();
	private Long price;
	
	public SignUpPackagePriceCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	
	@Override
	protected void loadMemberFields(Long uniqueId) {
	}
	
	
	private void testRequiredEntities() {
		if (subscription.getSignUpPackage() == null) {
			addActionErrorText("error.no_sign_up_package");
			throw new MissingEntityException("you need a valid sign up package");
		}
	}

	
	
	public String doShow() {
		testRequiredEntities();
		try {
			findPrice();
			return SUCCESS;
		
		} catch (CommunicationException e) {
			addActionErrorText("error.could_not_contact_billing_provider");
			logger.error(getLogLinePrefix() + "could not contact billing provider (netsuite)", e);
		} catch (Exception e) {
			addActionErrorText("error.could_not_calculate_price");
			logger.error(getLogLinePrefix() + "could not contact billing provider (netsuite)", e);
			
		}
		return ERROR;
		
	}

	private void findPrice() throws CommunicationException {
		SubscriptionAgent subscriptionAgent = getCreateHandlerFactory().getSubscriptionAgent();
		PriceCheckResponse priceResponse = subscriptionAgent.priceCheck(getSignUp());
		price = priceResponse.getPricing().getFirstPaymentTotal().longValue();
	}
	
	public Long getPrice() {
		return price;
	}
	
	public SignUpRequest getSignUp() {
		return subscription;
	}


	public String getSignUpPackageId() {
		return  subscription.getSignUpPackage().getName();
	}

	public void setSignUpPackageId(String signUpPackageId) {
		SignUpPackageDetails targetPackage = SignUpPackageDetails.valueOf(signUpPackageId);
		SignUpPackageLoader loader = getNonSecureLoaderFactory().createSignUpPackageLoader();
		loader.setSignUpPackageTarget(targetPackage);
		subscription.setSignUpPackage(loader.load());
	}
	

}
