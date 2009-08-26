package com.n4systems.fieldid.actions.signup;

import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.view.model.SignUpRequestDecorator;
import com.n4systems.fieldid.view.model.SignUpPackage;
import com.n4systems.handlers.creator.signup.AccountPlaceHolderCreateHandler;
import com.n4systems.handlers.creator.signup.AccountPlaceHolderCreateHandlerImpl;
import com.n4systems.handlers.creator.signup.BaseSystemSetupDataCreateHandlerImpl;
import com.n4systems.handlers.creator.signup.BaseSystemStructureCreateHandler;
import com.n4systems.handlers.creator.signup.BaseSystemStructureCreateHandlerImpl;
import com.n4systems.handlers.creator.signup.BaseSystemTenantStructureCreateHandlerImpl;
import com.n4systems.handlers.creator.signup.PrimaryOrgCreateHandler;
import com.n4systems.handlers.creator.signup.PrimaryOrgCreateHandlerImpl;
import com.n4systems.handlers.creator.signup.SignUpHandlerImpl;
import com.n4systems.handlers.creator.signup.SubscriptionApprovalHandler;
import com.n4systems.handlers.creator.signup.SubscriptionApprovalHandlerImpl;
import com.n4systems.handlers.creator.signup.model.SignUpRequest;
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
import com.n4systems.persistence.PersistenceProvider;
import com.n4systems.persistence.StandardPersistenceProvider;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.SubscriptionAgentFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.timezone.TimeZoneSelectionHelper;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

public class SignUpCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SignUpCrud.class);

	private SignUpPackage signUpPackage;
	private SignUpRequestDecorator signUpRequest;

	public SignUpCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		signUpRequest = (sessionContains("signUp")) ? new SignUpRequestDecorator((SignUpRequest) getSessionVar("signUp"), NonSecureLoaderFactory.createTenantUniqueAvailableNameLoader()) : new SignUpRequestDecorator(NonSecureLoaderFactory
				.createTenantUniqueAvailableNameLoader());
		setSignUpPackageId(signUpRequest.getSignUpPackageId());
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
		logger.info(getLogLinePrefix() + "signing up for an account tenant [" + signUpRequest.getTenantName() + "]  package [" + signUpPackage.getName() + "]");

		setSessionVar("signUp", signUpRequest.getSignUpRequest());
		
		try {
			createAccount();
		} catch (Exception e) {
			addActionErrorText("error.could_not_create_account");
			logger.error(getLogLinePrefix() + "signing up for an account tenant [" + signUpRequest.getTenantName() + "]  package [" + signUpPackage.getName() + "]", e);
			return ERROR;
		}

		addFlashMessageText("message.your_account_has_been_created");
		logger.info(getLogLinePrefix() + "signed up for an account tenant [" + signUpRequest.getTenantName() + "]  package [" + signUpPackage.getName() + "]");
		return SUCCESS;
	}

	private void createAccount() {
		PersistenceProvider persistenceProvider = new StandardPersistenceProvider();
		new SignUpHandlerImpl(getAccountPlaceHolderCreateHandler(), getBaseSystemStructureCreateHandler(), getSubscriptionAgent(), getSubscriptionApprovalHandler())
			.withPersistenceProvider(persistenceProvider)
			.signUp(signUpRequest.getSignUpRequest());
	}

	private SubscriptionApprovalHandler getSubscriptionApprovalHandler() {
		return new SubscriptionApprovalHandlerImpl(new OrganizationSaver(), new UserSaver());
	}

	private BaseSystemStructureCreateHandler getBaseSystemStructureCreateHandler() {
		return new BaseSystemStructureCreateHandlerImpl(new BaseSystemTenantStructureCreateHandlerImpl(new SetupDataLastModDatesSaver(),
				new SerialNumberCounterSaver()), new BaseSystemSetupDataCreateHandlerImpl(new TagOptionSaver(), new ProductTypeSaver(), new InspectionTypeGroupSaver(), new StateSetSaver()));
	}
	
	private AccountPlaceHolderCreateHandler getAccountPlaceHolderCreateHandler() {
		return new AccountPlaceHolderCreateHandlerImpl(new TenantSaver(), getPrimaryOrgCreateHandler(), new UserSaver());
	}
	
	private PrimaryOrgCreateHandler getPrimaryOrgCreateHandler() {
		return new PrimaryOrgCreateHandlerImpl(new OrganizationSaver());
	}

	private SubscriptionAgent getSubscriptionAgent() {
		return SubscriptionAgentFactory.createSubscriptionFactory(ConfigContext.getCurrentContext().getString(ConfigEntry.SUBSCRIPTION_AGENT));
	}

	

	public SortedSet<? extends Listable<String>> getCountries() {
		return TimeZoneSelectionHelper.getCountries();
	}

	public SortedSet<? extends Listable<String>> getTimeZones() {
		return TimeZoneSelectionHelper.getTimeZones(signUpRequest.getCountry());
	}

	public SignUpPackage getSignUpPackage() {
		return signUpPackage;
	}

	public Long getSignUpPackageId() {
		return signUpPackage.getId();
	}

	public void setSignUpPackageId(Long signUpPackageId) {
		signUpRequest.setSignUpPackageId(signUpPackageId);
		this.signUpPackage = new SignUpPackage(signUpPackageId, "Basic", 40, false, 1L);
	}

	@VisitorFieldValidator(message = "")
	public SignUpRequestDecorator getSignUp() {
		return signUpRequest;
	}
}
