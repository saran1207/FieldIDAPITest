package com.n4systems.fieldid.actions.signup;

import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.actions.helpers.TimeZoneSelectionHelper;
import com.n4systems.fieldid.view.model.SignUp;
import com.n4systems.fieldid.view.model.SignUpPackage;
import com.n4systems.fieldid.view.model.SignUpStorage;
import com.n4systems.handlers.creator.BaseSystemSetupDataCreateHandlerImpl;
import com.n4systems.handlers.creator.BaseSystemStructureCreateHandler;
import com.n4systems.handlers.creator.BaseSystemStructureCreateHandlerImpl;
import com.n4systems.handlers.creator.BaseSystemTenantStructureCreateHandlerImpl;
import com.n4systems.handlers.creator.PrimaryOrgCreateHandler;
import com.n4systems.handlers.creator.PrimaryOrgCreateHandlerImpl;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Listable;
import com.n4systems.model.inspectiontypegroup.InspectionTypeGroupSaver;
import com.n4systems.model.producttype.ProductTypeSaver;
import com.n4systems.model.serialnumbercounter.SerialNumberCounterSaver;
import com.n4systems.model.stateset.StateSetSaver;
import com.n4systems.model.tagoption.TagOptionSaver;
import com.n4systems.model.tenant.OrganizationSaver;
import com.n4systems.model.tenant.SetupDataLastModDatesSaver;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;
import com.n4systems.subscription.BillingInfoException;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.SubscriptionAgentFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

public class SignUpCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SignUpCrud.class);

	private SignUpPackage signUpPackage;
	private SignUp signUp;

	public SignUpCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		signUp = (sessionContains("signUp")) ? new SignUp((SignUpStorage) getSessionVar("signUp"), NonSecureLoaderFactory.createTenantUniqueAvailableNameLoader()) : new SignUp(NonSecureLoaderFactory
				.createTenantUniqueAvailableNameLoader());
		setSignUpPackageId(signUp.getSignUpPackageId());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		initMemberFields();
	}

	private void testRequiredEntities(boolean exists) {
		if (exists && signUp.isNew()) {
			addFlashErrorText("error.you_must_go_through_sign_up");
			throw new MissingEntityException("you must go through the sign up process");
		}

		if (signUpPackage == null) {
			addFlashErrorText("error.no_sign_up_package");
			throw new MissingEntityException("you must select a package");
		}
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
		clearSessionVar("signUp");
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		logger.info(getLogLinePrefix() + "signing up for an account tenant [" + signUp.getTenantName() + "]  package [" + signUpPackage.getName() + "]");

		setSessionVar("signUp", signUp.getSignUpStorage());
		
		
		try {
			createAccount();
		} catch (Exception e) {
			addActionErrorText("error.could_not_create_account");
			logger.error(getLogLinePrefix() + "signing up for an account tenant [" + signUp.getTenantName() + "]  package [" + signUpPackage.getName() + "]", e);
			return ERROR;
		}

		addFlashMessageText("message.your_account_has_been_created");
		logger.info(getLogLinePrefix() + "signed up for an account tenant [" + signUp.getTenantName() + "]  package [" + signUpPackage.getName() + "]");
		return SUCCESS;
	}

	private void createAccount() {
		
		Tenant tenant = null;
		Transaction transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		try {
			tenant = createTenant(transaction);
			com.n4systems.persistence.PersistenceManager.finishTransaction(transaction);
		} catch (RuntimeException e) {
			com.n4systems.persistence.PersistenceManager.rollbackTransaction(transaction);
			throw e;
		}
		
		transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		try {
			SignUpTenantResponse signUpTenantResponse = confirmSubscription();
			completeSystemSetup(transaction, tenant, signUpTenantResponse);
			com.n4systems.persistence.PersistenceManager.finishTransaction(transaction);
		} catch (RuntimeException e) {
			com.n4systems.persistence.PersistenceManager.rollbackTransaction(transaction);
			removeTenant(transaction, tenant);
			throw e;
		}
	}

	private void completeSystemSetup(Transaction transaction, Tenant tenant, SignUpTenantResponse signUpTenantResponse) {
		BaseSystemStructureCreateHandler baseSystemCreator = new BaseSystemStructureCreateHandlerImpl(new BaseSystemTenantStructureCreateHandlerImpl(new SetupDataLastModDatesSaver(),
				new SerialNumberCounterSaver()), new BaseSystemSetupDataCreateHandlerImpl(new TagOptionSaver(), new ProductTypeSaver(), new InspectionTypeGroupSaver(), new StateSetSaver()));
		
		PrimaryOrgCreateHandler orgCreateHandler = new PrimaryOrgCreateHandlerImpl(new OrganizationSaver(), new UserSaver());
		
		baseSystemCreator.forTenant(tenant).create(transaction);
		orgCreateHandler.forTenant(tenant).forAccountInfo(signUp).create(transaction);
	}

	private SignUpTenantResponse confirmSubscription() {
		SubscriptionAgent subscriptionAgent = SubscriptionAgentFactory.createSubscriptionFactory(ConfigContext.getCurrentContext().getString(ConfigEntry.SUBSCRIPTION_AGENT));
		
		SignUpTenantResponse response = null;
		try {
			response = subscriptionAgent.buy(signUp.getSignUpStorage(), signUp.getSignUpStorage(), signUp.getSignUpStorage());
		} catch (CommunicationException e) {
			// TODO SOMETHING!!
		} catch (BillingInfoException ee) {
			// TODO SOMETHING ELSE!!
		}
		
		return response;
	}

	private Tenant createTenant(Transaction transaction) {
		TenantSaver tenantSaver = new TenantSaver();
		Tenant tenant = new Tenant();
		tenant.setName(signUp.getTenantName());
		tenantSaver.save(transaction, tenant);
		return tenant;
	}
	
	private void removeTenant(Transaction transaction, Tenant tenant) {
		TenantSaver tenantSaver = new TenantSaver();
		tenantSaver.remove(transaction, tenant);
	}

	public SortedSet<? extends Listable<String>> getCountries() {
		return TimeZoneSelectionHelper.getCountries();
	}

	public SortedSet<? extends Listable<String>> getTimeZones() {
		return TimeZoneSelectionHelper.getTimeZones(signUp.getCountry());
	}

	public SignUpPackage getSignUpPackage() {
		return signUpPackage;
	}

	public Long getSignUpPackageId() {
		return signUpPackage.getId();
	}

	public void setSignUpPackageId(Long signUpPackageId) {
		signUp.setSignUpPackageId(signUpPackageId);
		this.signUpPackage = new SignUpPackage(signUpPackageId, "Basic", 40, false, 1L);
	}

	@VisitorFieldValidator(message = "")
	public SignUp getSignUp() {
		return signUp;
	}
}
