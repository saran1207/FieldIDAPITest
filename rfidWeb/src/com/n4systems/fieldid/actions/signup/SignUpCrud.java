package com.n4systems.fieldid.actions.signup;

import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.actions.signup.view.model.SignUpRequestDecorator;
import com.n4systems.handlers.creator.signup.exceptions.BillingValidationException;
import com.n4systems.handlers.creator.signup.exceptions.CommunicationErrorException;
import com.n4systems.handlers.creator.signup.exceptions.PromoCodeValidationException;
import com.n4systems.handlers.creator.signup.exceptions.SignUpCompletionException;
import com.n4systems.handlers.creator.signup.exceptions.TenantNameUsedException;
import com.n4systems.handlers.creator.signup.model.SignUpRequest;
import com.n4systems.model.api.Listable;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.signuppackage.SignUpPackageLoader;
import com.n4systems.persistence.PersistenceProvider;
import com.n4systems.persistence.StandardPersistenceProvider;
import com.n4systems.subscription.AddressInfo;
import com.n4systems.subscription.CreditCard;
import com.n4systems.subscription.PriceCheckResponse;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.util.timezone.TimeZoneSelectionHelper;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

public class SignUpCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SignUpCrud.class);

	private SignUpRequestDecorator signUpRequest = new SignUpRequestDecorator();
	public SignUpCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		SignUpRequest sessionSignUp = getSession().getSignUpRequest();
		if (sessionSignUp == null) {
			sessionSignUp = new SignUpRequest();
		}
		signUpRequest = new SignUpRequestDecorator(sessionSignUp, getNonSecureLoaderFactory().createTenantUniqueAvailableNameLoader(), getCreateHandlerFactory().getSubscriptionAgent()); 
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		initMemberFields();
	}

	private void testRequiredEntities(boolean exists) {
		if (exists && signUpRequest.isNew()) {
			addFlashErrorText("error.you_must_go_through_sign_up");
			throw new MissingEntityException("you must go through the sign up process");
		}

		if (signUpRequest.getSignUpPackage() == null) {
			addFlashErrorText("error.no_sign_up_package");
			throw new MissingEntityException("you must select a package");
		}
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
		getSession().clearSignUpRequest();
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		logger.info(getLogLinePrefix() + "signing up for an account tenant [" + signUpRequest.getTenantName() + "]  package [" + signUpRequest.getSignUpPackage().getName() + "]");
		
		getSession().setSignUpRequest(signUpRequest.getSignUpRequest());
		
		return proccessSignUp();
	}

	private String proccessSignUp() {
		String result;
		try {
			createAccount();
			
			addFlashMessageText("message.your_account_has_been_created");
			logger.info(signUpLogLine("signed up"));
			
			result = SUCCESS;
			
		} catch (SignUpCompletionException e) {
			addActionErrorText("error.your_account_has_been_created_but_not_activated_N4_has_been_notified_to_complete_the_process.");
			logger.error(signUpLogLine("signing up"), e);
			
			result = "serious_error";
			
		} catch (TenantNameUsedException e) {
			addFieldError("signUp.tenant_name", getText("error.name_already_used"));
			
			result = INPUT;
			
		} catch (BillingValidationException e) {
			addFieldError("creditCard", getText("error.credit_card_information_is_incorrect"));
			logger.debug(signUpLogLine("billing information incorrect"), e);
			
			result = INPUT;
			
		} catch (CommunicationErrorException e) {
			addActionErrorText("error.could_not_contact_billing_provider");
			logger.error(signUpLogLine("signing up"), e);
			
			result = ERROR;
		
		} catch (Exception e) {
			addActionErrorText("error.could_not_create_account");
			logger.error(signUpLogLine("signing up"), e);
			
			result = ERROR;
		}

		return result;
	}

	private String signUpLogLine(String action) {
		return getLogLinePrefix() + action + ("for an account tenant [" + signUpRequest.getTenantName() + "]  package [" + signUpRequest.getSignUpPackage().getName() + "]");
	}
	

	private void createAccount() throws BillingValidationException, PromoCodeValidationException, CommunicationErrorException, TenantNameUsedException, ProcessFailureException, SignUpCompletionException {
		PersistenceProvider persistenceProvider = new StandardPersistenceProvider();
		
		getCreateHandlerFactory().getSignUpHandler().withPersistenceProvider(persistenceProvider).signUp(signUpRequest.getSignUpRequest());
	}

	public SortedSet<? extends Listable<String>> getCountries() {
		return TimeZoneSelectionHelper.getCountries();
	}

	public SortedSet<? extends Listable<String>> getTimeZones() {
		return TimeZoneSelectionHelper.getTimeZones(signUpRequest.getCountry());
	}

	public SignUpPackage getSignUpPackage() {
		return signUpRequest.getSignUpPackage();
	}

	public String getSignUpPackageId() {
		return  signUpRequest.getSignUpPackage().getName();
	}
	
	public boolean isUsingCreditCard() {
		return signUpRequest.isUsingCreditCard();		
	}
	
	public void setUsingCreditCard(boolean usingCreditCard) {
		signUpRequest.setUsingCreditCard(usingCreditCard);
	}

	public void setSignUpPackageId(String signUpPackageId) {
		SignUpPackageDetails targetPackage = SignUpPackageDetails.valueOf(signUpPackageId);
		SignUpPackageLoader loader = getNonSecureLoaderFactory().createSignUpPackageLoader();
		loader.setSignUpPackageTarget(targetPackage);
		signUpRequest.setSignUpPackage(loader.load());
		
	}

	@VisitorFieldValidator(message = "")
	public SignUpRequestDecorator getSignUp() {
		return signUpRequest;
	}
	
	@CustomValidator(type = "conditionalVisitorFieldValidator", message = "", parameters = { @ValidationParameter(name = "expression", value = "aNeedToCheckCreditCard == true") })
	public CreditCard getCreditCard() {
		return signUpRequest.getCreditCard();
	}
	
	public boolean isANeedToCheckCreditCard() {
		return (isUsingCreditCard() && !getSignUp().getSignUpPackage().isFree());
	}
	
	@VisitorFieldValidator(message = "")
	public AddressInfo getAddress() {
		return signUpRequest.getBillingAddress();
	}
	
	public Long getPrice() {
		SubscriptionAgent agent = getCreateHandlerFactory().getSubscriptionAgent();
		try {
			PriceCheckResponse price = agent.priceCheck(getSignUp());
			return price.getPricing().getFirstPaymentTotal().longValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
		
	}
}
