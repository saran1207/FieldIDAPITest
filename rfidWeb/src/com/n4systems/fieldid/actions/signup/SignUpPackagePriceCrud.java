package com.n4systems.fieldid.actions.signup;

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

	
	private SignUpRequest subscription = new SignUpRequest();
	
	
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
		
		return SUCCESS;
	}
	
	// FIXME should this exception be eaten? 
	public Long getPrice() {
		SubscriptionAgent agent = getCreateHandlerFactory().getSubscriptionAgent();
		try {
			PriceCheckResponse price = agent.priceCheck(getSignUp());
			return price.getPricing().getFirstPaymentTotal().longValue();
		} catch (CommunicationException e) {
			
		}
		return 0L;
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
