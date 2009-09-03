package com.n4systems.handlers.creator.signup;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.creator.signup.exceptions.BillingValidationException;
import com.n4systems.handlers.creator.signup.exceptions.CommunicationErrorException;
import com.n4systems.handlers.creator.signup.exceptions.SignUpCompletionException;
import com.n4systems.handlers.creator.signup.exceptions.SignUpSoftFailureException;
import com.n4systems.handlers.creator.signup.exceptions.TenantNameUsedException;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.handlers.creator.signup.model.SignUpRequest;
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
	
	
	public SignUpHandlerImpl(AccountPlaceHolderCreateHandler accountPlaceHolderCreateHandler, BaseSystemStructureCreateHandler baseSystemCreator, SubscriptionAgent subscriptionAgent, SignUpFinalizationHandler signUpFinalizationHandler) {
		super();
		this.accountPlaceHolderCreateHandler = accountPlaceHolderCreateHandler;
		this.baseSystemCreator = baseSystemCreator;
		this.subscriptionAgent = subscriptionAgent;
		this.signUpFinalizationHandler = signUpFinalizationHandler;
	}

	public void signUp(SignUpRequest signUp) throws SignUpCompletionException, SignUpSoftFailureException {
		if (persistenceProvider == null) {
			throw new InvalidArgumentException("You must give a persistence provider");
		}
		AccountPlaceHolder placeHolder = null;
		Transaction transaction = persistenceProvider.startTransaction();
		try {
			placeHolder = createAccountPlaceHolder(signUp, transaction);
			//FIXME this is the wrong spot for this.
			signUp.setCompanyN4Id(placeHolder.getPrimaryOrg().getId());
			signUp.setUserN4Id(placeHolder.getAdminUser().getId());
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw new TenantNameUsedException("tenant name could not be saved", e);
		}
		
		SignUpTenantResponse subscriptionApproval = null;
		try {
			
			//TODO copy primary org and admin user id into the signUp
			subscriptionApproval = subscriptionAgent.buy(signUp, signUp, signUp);
			
		} catch (CommunicationException e) {
			undoAccountPlaceHolder(placeHolder);
			throw new CommunicationErrorException("could not complete process", e);
		} catch (BillingInfoException e) {
			undoAccountPlaceHolder(placeHolder);
			throw new BillingValidationException("could not complete process", e);
		}
		
		transaction = persistenceProvider.startTransaction();
		try {
			baseSystemCreator.forTenant(placeHolder.getTenant()).create(transaction);
			
			signUpFinalizationHandler.setAccountPlaceHolder(placeHolder).setSubscriptionApproval(subscriptionApproval).setAccountInformation(signUp);
			signUpFinalizationHandler.finalizeSignUp(transaction);
			
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw new SignUpCompletionException("The final step of activating the account has failed. The subscription information been entered.", e);
		}
	}

	private AccountPlaceHolder createAccountPlaceHolder(SignUpRequest signUp, Transaction transaction) {
		return accountPlaceHolderCreateHandler.forAccountInfo(signUp).createWithUndoInformation(transaction);
	}

	private void undoAccountPlaceHolder(AccountPlaceHolder placeHolder) {
		Transaction transaction = persistenceProvider.startTransaction();
		try {
			accountPlaceHolderCreateHandler.undo(transaction, placeHolder);
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw new SignUpSoftFailureException("failed to destory account place holder", e);
		}
	}
	
	
	
	

	public SignUpHandler withPersistenceProvider(PersistenceProvider persistenceProvider) {
		this.persistenceProvider = persistenceProvider;
		return this;
	}
	
	
	
}
