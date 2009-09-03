package com.n4systems.handlers.creator.signup;

import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.SignUpTenantResponse;

public interface SignUpFinalizationHandler {

	public void finalizeSignUp(Transaction transaction);
	
	public SignUpFinalizationHandler setAccountInformation(AccountCreationInformation accountInformation);
	
	public SignUpFinalizationHandler setAccountPlaceHolder(AccountPlaceHolder accountPlaceHolder);
	
	public SignUpFinalizationHandler setSubscriptionApproval(SignUpTenantResponse subscriptionApproval);
}
