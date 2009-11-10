package com.n4systems.handlers.creator.signup;

import org.apache.log4j.Logger;

import com.n4systems.ejb.MailManager;
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
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.TemplateMailMessage;

public class SignUpHandlerImpl implements SignUpHandler {
	private static final Logger logger = Logger.getLogger(SignUpHandlerImpl.class);
	
	
	private final BaseSystemStructureCreateHandler baseSystemCreator;
	private final SubscriptionAgent subscriptionAgent;
	private final AccountPlaceHolderCreateHandler accountPlaceHolderCreateHandler;
	private final SignUpFinalizationHandler signUpFinalizationHandler;
	private final MailManager mailManager;
	
	
	private PersistenceProvider persistenceProvider;
	private AccountPlaceHolder placeHolder;
	private SignUpTenantResponse subscriptionApproval;
	
	
	public SignUpHandlerImpl(AccountPlaceHolderCreateHandler accountPlaceHolderCreateHandler, BaseSystemStructureCreateHandler baseSystemCreator, SubscriptionAgent subscriptionAgent, SignUpFinalizationHandler signUpFinalizationHandler, MailManager mailManager) {
		super();
		this.accountPlaceHolderCreateHandler = accountPlaceHolderCreateHandler;
		this.baseSystemCreator = baseSystemCreator;
		this.subscriptionAgent = subscriptionAgent;
		this.signUpFinalizationHandler = signUpFinalizationHandler;
		this.mailManager = mailManager;
	}

	
	public void signUp(SignUpRequest signUp, PrimaryOrg referrerOrg, String portalUrl) throws SignUpCompletionException, SignUpSoftFailureException {
		guard();
		holdNamesForSignUp(signUp);
		confirmSubscription(signUp);
		activateAccount(signUp, referrerOrg, portalUrl);
	}


	private void activateAccount(SignUpRequest signUp, PrimaryOrg referrerOrg, String portalUrl) throws SignUpCompletionException {
		Transaction transaction = persistenceProvider.startTransaction();
		try {
			finalizeAccount(signUp, referrerOrg, placeHolder, transaction, subscriptionApproval, portalUrl);
			
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw new SignUpCompletionException("The final step of activating the account has failed. The subscription information been entered.", e);
		}
	}


	private void confirmSubscription(SignUpRequest signUp) {
		try {
			subscriptionApproval = subscriptionAgent.buy(signUp, signUp, signUp);
		} catch (Exception e) {
			processSubscriptionApprovalError(e, placeHolder);
		}
	}


	private void holdNamesForSignUp(SignUpRequest signUp) {
		Transaction transaction = persistenceProvider.startTransaction();
		try { 
			placeHolder = createPlaceHolder(signUp, transaction);
			
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw new TenantNameUsedException("tenant name could not be saved", e);
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
	
	private void finalizeAccount(SignUpRequest signUp, PrimaryOrg referrerOrg, AccountPlaceHolder placeHolder, Transaction transaction, SignUpTenantResponse subscriptionApproval, String portalUrl) {
		baseSystemCreator.forTenant(placeHolder.getTenant())
					.create(transaction);
		
		signUpFinalizationHandler.setAccountPlaceHolder(placeHolder)
					.setSubscriptionApproval(subscriptionApproval)
					.setAccountInformation(signUp)
					.setReferrerOrg(referrerOrg)
					.finalizeSignUp(transaction);
		
		sendWelcomeEmail(placeHolder, portalUrl);
	}


	private void sendWelcomeEmail(AccountPlaceHolder placeHolder, String portalUrl) {
		try {
			MailMessage message = createWelcomeMessage(placeHolder, portalUrl);
			mailManager.sendMessage(message);
		} catch (Exception e) {
			logger.warn("could not send welcome email", e);
		}
	}


	private MailMessage createWelcomeMessage(AccountPlaceHolder placeHolder, String portalUrl) {
		TemplateMailMessage invitationMessage = new TemplateMailMessage("Welcome to Field ID", "welcomeMessage");
	
		
		invitationMessage.getToAddresses().add(placeHolder.getAdminUser().getEmailAddress());
		invitationMessage.getBccAddresses().add("sales@n4systems.com");
		invitationMessage.getTemplateMap().put("companyId", placeHolder.getTenant().getName());
		invitationMessage.getTemplateMap().put("portalUrl", portalUrl);
		invitationMessage.getTemplateMap().put("username", placeHolder.getAdminUser().getUserID());
		
		
		return invitationMessage;
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
