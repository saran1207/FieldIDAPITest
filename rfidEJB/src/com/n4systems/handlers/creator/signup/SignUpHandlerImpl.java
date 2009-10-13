package com.n4systems.handlers.creator.signup;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.handlers.creator.signup.exceptions.BillingValidationException;
import com.n4systems.handlers.creator.signup.exceptions.CommunicationErrorException;
import com.n4systems.handlers.creator.signup.exceptions.SignUpCompletionException;
import com.n4systems.handlers.creator.signup.exceptions.SignUpSoftFailureException;
import com.n4systems.handlers.creator.signup.exceptions.TenantNameUsedException;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.handlers.creator.signup.model.SignUpRequest;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.PersistenceProvider;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.BillingInfoException;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.SubscriptionAgent;

public class SignUpHandlerImpl implements SignUpHandler {

	private final BaseSystemStructureCreateHandler baseSystemCreator;
	private final SubscriptionAgent subscriptionAgent;
	private final AccountPlaceHolderCreateHandler accountPlaceHolderCreateHandler;
	private final SignUpFinalizationHandler signUpFinalizationHandler;
	
	private PersistenceProvider persistenceProvider;
	private AccountPlaceHolder placeHolder;
	private SignUpTenantResponse subscriptionApproval;
	
	
	public SignUpHandlerImpl(AccountPlaceHolderCreateHandler accountPlaceHolderCreateHandler, BaseSystemStructureCreateHandler baseSystemCreator, SubscriptionAgent subscriptionAgent, SignUpFinalizationHandler signUpFinalizationHandler) {
		super();
		this.accountPlaceHolderCreateHandler = accountPlaceHolderCreateHandler;
		this.baseSystemCreator = baseSystemCreator;
		this.subscriptionAgent = subscriptionAgent;
		this.signUpFinalizationHandler = signUpFinalizationHandler;
	}

	
	//FIXME this method is big!
	public void signUp(SignUpRequest signUp, PrimaryOrg referrerOrg) throws SignUpCompletionException, SignUpSoftFailureException {
		guard();
		
		Transaction transaction = persistenceProvider.startTransaction();
		try { 
			placeHolder = createPlaceHolder(signUp, transaction);
			
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw new TenantNameUsedException("tenant name could not be saved", e);
		}
		
		
		try {
			subscriptionApproval = subscriptionAgent.buy(signUp, signUp, signUp);
		} catch (Exception e) {
			processSubscriptionApprovalError(e, placeHolder);
		}
		
		transaction = persistenceProvider.startTransaction();
		try {
			finalizeAccount(signUp, referrerOrg, placeHolder, transaction, subscriptionApproval);
			
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw new SignUpCompletionException("The final step of activating the account has failed. The subscription information been entered.", e);
		}
	}


	private void guard() {
		if (persistenceProvider == null) {
			throw new InvalidArgumentException("You must give a persistence provider");
		}
	}

	private void processSubscriptionApprovalError(Exception e, AccountPlaceHolder placeHolder) {
		undoAccountPlaceHolder(placeHolder);
		convertExecption(e);
	}


	private void convertExecption(Exception e) {
		if (CommunicationException.class.isAssignableFrom(e.getClass())) {
			throw new CommunicationErrorException("could not complete process", e);
		} else if (BillingInfoException.class.isAssignableFrom(e.getClass())) {
			throw new BillingValidationException("could not complete process", e);
		} else {
			throw new ProcessFailureException("could not complete process", e);
		}
	}
	
	private void finalizeAccount(SignUpRequest signUp, PrimaryOrg referrerOrg, AccountPlaceHolder placeHolder, Transaction transaction, SignUpTenantResponse subscriptionApproval) {
		baseSystemCreator.forTenant(placeHolder.getTenant()).create(transaction);
		
		signUpFinalizationHandler.setAccountPlaceHolder(placeHolder).setSubscriptionApproval(subscriptionApproval).setAccountInformation(signUp);
		signUpFinalizationHandler.setReferrerOrg(referrerOrg);
		signUpFinalizationHandler.finalizeSignUp(transaction);
	}


	private AccountPlaceHolder createPlaceHolder(SignUpRequest signUp, Transaction transaction) {
		AccountPlaceHolder placeHolder = null;
		
		
		placeHolder = createAccountPlaceHolder(signUp, transaction);
		signUp.setCompanyN4Id(placeHolder.getPrimaryOrg().getId());
		signUp.setUserN4Id(placeHolder.getAdminUser().getId());
		signUp.setExternalPassword(placeHolder.getPrimaryOrg().getExternalPassword());
		
		return placeHolder;
	}

	private AccountPlaceHolder createAccountPlaceHolder(SignUpRequest signUp, Transaction transaction) {
		return accountPlaceHolderCreateHandler.forAccountInfo(signUp).createWithUndoInformation(transaction);
	}

	
	private void undoAccountPlaceHolder(AccountPlaceHolder placeHolder) {
		Transaction transaction = persistenceProvider.startTransaction();
		try {
			accountPlaceHolderCreateHandler.undo(transaction, placeHolder);
			persistenceProvider.finishTransaction(transaction);
		} catch (Exception e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw new SignUpSoftFailureException("failed to destory account place holder", e);
		}
	}
	
	
	
	

	public SignUpHandler withPersistenceProvider(PersistenceProvider persistenceProvider) {
		this.persistenceProvider = persistenceProvider;
		return this;
	}
	
	
	
}
