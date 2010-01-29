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
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
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
	private final SignupReferralHandler signUpReferralHandler;
	private final MailManager mailManager;

	private TransactionManager transactionManager;
	protected AccountPlaceHolder placeHolder;
	private SignUpTenantResponse subscriptionApproval;	
	
	public SignUpHandlerImpl(AccountPlaceHolderCreateHandler accountPlaceHolderCreateHandler, BaseSystemStructureCreateHandler baseSystemCreator, SubscriptionAgent subscriptionAgent, SignUpFinalizationHandler signUpFinalizationHandler, SignupReferralHandler signUpReferralHandler, MailManager mailManager) {
		super();
		this.accountPlaceHolderCreateHandler = accountPlaceHolderCreateHandler;
		this.baseSystemCreator = baseSystemCreator;
		this.subscriptionAgent = subscriptionAgent;
		this.signUpFinalizationHandler = signUpFinalizationHandler;
		this.signUpReferralHandler = signUpReferralHandler;
		this.mailManager = mailManager;
	}
	
	public void signUp(SignUpRequest signUp, PrimaryOrg referrerOrg, String portalUrl, String referralCode) throws SignUpCompletionException, SignUpSoftFailureException {
		guard();
		holdNamesForSignUp(signUp);
		confirmSubscription(signUp);
		activateAccount(signUp, referrerOrg, portalUrl);
		processReferral(referrerOrg, referralCode);
	}

	private void processReferral(PrimaryOrg referrerOrg, String referralCode) {
		try {
			signUpReferralHandler.processReferral(referrerOrg.getTenant(), placeHolder.getTenant(), referralCode);
		} catch(Exception e) {
			// nothing should ever come out of the referral handler, but we 
			// don't want to fail the whole process if it does.  Just log it and move on
			logger.error("Failed to process signup referral", e);
		}
	}

	protected void activateAccount(SignUpRequest signUp, PrimaryOrg referrerOrg, String portalUrl) throws SignUpCompletionException {
		Transaction transaction = transactionManager.startTransaction();
		try {
			finalizeAccount(signUp, referrerOrg, placeHolder, transaction, subscriptionApproval, portalUrl);
			
			transactionManager.finishTransaction(transaction);
		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw new SignUpCompletionException("The final step of activating the account has failed. The subscription information been entered.", e);
		}
	}

	protected void confirmSubscription(SignUpRequest signUp) {
		try {
			subscriptionApproval = subscriptionAgent.buy(signUp, signUp, signUp);
		} catch (Exception e) {
			processSubscriptionApprovalError(e, placeHolder);
		}
	}

	protected void holdNamesForSignUp(SignUpRequest signUp) {
		Transaction transaction = transactionManager.startTransaction();
		try { 
			placeHolder = createPlaceHolder(signUp, transaction);
			
			transactionManager.finishTransaction(transaction);
		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw new TenantNameUsedException("tenant name could not be saved", e);
		}
	}

	protected void guard() {
		if (transactionManager == null) {
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
	
		invitationMessage.setSubjectPrefix("");
		invitationMessage.getToAddresses().add(placeHolder.getAdminUser().getEmailAddress());
		invitationMessage.getBccAddresses().add("sales@fieldid.com");
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
		Transaction transaction = transactionManager.startTransaction();
		try {
			accountPlaceHolderCreateHandler.undo(transaction, placeHolder);
			transactionManager.finishTransaction(transaction);
		} catch (Exception e) {
			transactionManager.rollbackTransaction(transaction);
			throw new SignUpSoftFailureException("failed to destory account place holder", e);
		}
	}

	public SignUpHandler withTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
		return this;
	}
	
}
