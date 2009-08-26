package com.n4systems.handlers.creator.signup;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
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
	private final SubscriptionApprovalHandler subscriptionApprovalHandler;
	
	private PersistenceProvider persistenceProvider;
	
	
	public SignUpHandlerImpl(AccountPlaceHolderCreateHandler accountPlaceHolderCreateHandler, BaseSystemStructureCreateHandler baseSystemCreator, SubscriptionAgent subscriptionAgent, SubscriptionApprovalHandler subscriptionApprovalHandler) {
		super();
		this.accountPlaceHolderCreateHandler = accountPlaceHolderCreateHandler;
		this.baseSystemCreator = baseSystemCreator;
		this.subscriptionAgent = subscriptionAgent;
		this.subscriptionApprovalHandler = subscriptionApprovalHandler;
	}

	public void signUp(SignUpRequest signUp) {
		if (persistenceProvider == null) {
			throw new InvalidArgumentException("You must give a persistence provider");
		}
		AccountPlaceHolder placeHolder = null;
		Transaction transaction = persistenceProvider.startTransaction();
		try {
			placeHolder = createAccountPlaceHolder(signUp, transaction);
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw e;
		}
		
		SignUpTenantResponse subscriptionApproval = null;
		try {
			
			//TODO copy primary org and admin user id into the signUp
			subscriptionApproval = subscriptionAgent.buy(signUp, signUp, signUp);
			
		} catch (CommunicationException e) {
			undoAccountPlaceHolder(placeHolder);
			throw new ProcessFailureException("could not complete process", e);
		} catch (BillingInfoException e) {
			undoAccountPlaceHolder(placeHolder);
			throw new ProcessFailureException("could not complete process", e);
		}
		
		transaction = persistenceProvider.startTransaction();
		try {
			subscriptionApprovalHandler.forAccountPlaceHolder(placeHolder).forSubscriptionApproval(subscriptionApproval).applyApproval(transaction);
			baseSystemCreator.forTenant(placeHolder.getTenant()).create(transaction);
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw e;
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
			throw e;
		}
	}
	
	
	
	

	public SignUpHandler withPersistenceProvider(PersistenceProvider persistenceProvider) {
		this.persistenceProvider = persistenceProvider;
		return this;
	}
	
	
	
}
