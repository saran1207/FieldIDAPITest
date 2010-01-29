package com.n4systems.handlers.creator.signup;

import com.n4systems.handlers.creator.signup.exceptions.SignUpCompletionException;
import com.n4systems.handlers.creator.signup.exceptions.SignUpSoftFailureException;
import com.n4systems.handlers.creator.signup.model.SignUpRequest;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.TransactionManager;

public interface SignUpHandler {

	public void signUp(SignUpRequest signUpRequest, PrimaryOrg referrerOrg, String portalUrl, String referralCode) throws SignUpCompletionException, SignUpSoftFailureException;
	
	public SignUpHandler withTransactionManager(TransactionManager transactionManager);

}