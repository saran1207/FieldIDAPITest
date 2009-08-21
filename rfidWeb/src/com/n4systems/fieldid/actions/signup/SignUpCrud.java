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
		signUp = (sessionContains("signUp")) ? (SignUp)getSessionVar("signUp") : new SignUp();
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
		
		setSessionVar("signUp", signUp);
		Transaction transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		
		try {
			createAccount(transaction);
			com.n4systems.persistence.PersistenceManager.finishTransaction(transaction);
		} catch (Exception e) {
			com.n4systems.persistence.PersistenceManager.rollbackTransaction(transaction);
			addActionErrorText("error.could_not_create_account");
			logger.error(getLogLinePrefix() + "signing up for an account tenant [" + signUp.getTenantName() + "]  package [" + signUpPackage.getName() + "]", e);
			return ERROR;
		} 
		
		addFlashMessageText("message.your_account_has_been_created");
		logger.info(getLogLinePrefix() + "signed up for an account tenant [" + signUp.getTenantName() + "]  package [" + signUpPackage.getName() + "]");
		return SUCCESS;
	}

	
	private void createAccount(Transaction transaction) {
		BaseSystemStructureCreateHandler baseSystemCreator = new BaseSystemStructureCreateHandlerImpl(new BaseSystemTenantStructureCreateHandlerImpl(new SetupDataLastModDatesSaver(), new SerialNumberCounterSaver()), new BaseSystemSetupDataCreateHandlerImpl(new TagOptionSaver(), new ProductTypeSaver(), new InspectionTypeGroupSaver(), new StateSetSaver()));
		TenantSaver tenantSaver = new TenantSaver();
		
		Tenant tenant = new Tenant();
		tenant.setName(signUp.getTenantName());
		tenantSaver.save(transaction, tenant);
		
		baseSystemCreator.forTenant(tenant).create(transaction);
		PrimaryOrgCreateHandler orgCreateHandler = new PrimaryOrgCreateHandlerImpl(new OrganizationSaver(), new UserSaver());
		orgCreateHandler.forTenant(tenant).forAccountInfo(signUp).create(transaction);
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
		this.signUpPackage = new SignUpPackage(signUpPackageId, "basic", 40, false, 1L);
	}


	@VisitorFieldValidator(message="")
	public SignUp getSignUp() {
		return signUp;
	}
}
