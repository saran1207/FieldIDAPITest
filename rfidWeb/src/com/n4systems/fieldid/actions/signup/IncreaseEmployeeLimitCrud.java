package com.n4systems.fieldid.actions.signup;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.handlers.creator.signup.IncreaseEmployeeLimitHandler;
import com.n4systems.handlers.creator.signup.IncreaseEmployeeLimitHandlerImpl;
import com.n4systems.handlers.creator.signup.UpgradeCompletionException;
import com.n4systems.handlers.creator.signup.UpgradeRequest;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.signuppackage.UpgradePackageFilter;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.persistence.Transaction;
import com.n4systems.services.TenantCache;
import com.n4systems.services.limiters.TenantLimitService;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.subscription.UpgradeResponse;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;

public class IncreaseEmployeeLimitCrud extends AbstractCrud {
	private static Logger logger = Logger.getLogger(IncreaseEmployeeLimitCrud.class);
	
	private UpgradeCost upgradeCost;
	private AccountHelper accountHelper;
	private List<SignUpPackage> allSignUpPackages;
	private SignUpPackage currentPackage;
	
	private Integer additionalEmployee = 0;
	
	public IncreaseEmployeeLimitCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		accountHelper = new AccountHelper(getCreateHandlerFactory().getSubscriptionAgent(), getPrimaryOrg(), getNonSecureLoaderFactory().createSignUpPackageListLoader());
	}

	@SkipValidation
	public String doAdd() {
		if (accountHelper.currentPackageFilter().isLegacy()) {
			return ERROR;
		}
		
		if (packageHasUserLimit() && accountHasReachedUserLimit()) {
			return "limitExceeded";
		}
		
		additionalEmployee = 1;
		
		findUpgradeCost();
		return SUCCESS;
	}
	
	private void findUpgradeCost() {
		UpgradeRequest upgradeRequest = createUpgradeRequest();
		upgradeRequest.setUpdatedBillingInformation(false);
		try {
			upgradeCost = getUpgradeHandler().priceForUpgrade(upgradeRequest);
		} catch (CommunicationException e) {
			upgradeCost = null;
		}
	}

	private IncreaseEmployeeLimitHandler getUpgradeHandler() {
		return new IncreaseEmployeeLimitHandlerImpl(getPrimaryOrg(), getCreateHandlerFactory().getSubscriptionAgent(), new OrgSaver());
	}

	
	private UpgradeRequest createUpgradeRequest() {
		UpgradeRequest upgradeRequest = new UpgradeRequest();
		upgradeRequest.setTenantExternalId(getPrimaryOrg().getExternalId());
		upgradeRequest.setNewUsers(additionalEmployee);
		
		upgradeRequest.setUpgradePackage(getUpgradeFilter().getCurrentContract().getSignUpPackage());
		upgradeRequest.setContractExternalId(getUpgradeFilter().getCurrentContract().getExternalId());
		upgradeRequest.setTenantExternalId(getPrimaryOrg().getExternalId());
		
		
		upgradeRequest.setFrequency(getUpgradeFilter().getCurrentContract().getPaymentOption().getFrequency());
		upgradeRequest.setMonths(getUpgradeFilter().getCurrentContract().getPaymentOption().getTerm());
		upgradeRequest.setPurchasingPhoneSupport(accountHelper.getCurrentSubscription().isPhonesupport());
		
		upgradeRequest.setUpdatedBillingInformation(false);
		
		
		return upgradeRequest;
	}
	
	
	@SkipValidation
	public String doShowCost() {
		findUpgradeCost();
		return SUCCESS;
	}
	
	public String doCreate() {
		String result = upgradeAccount();
	
		
		TenantCache.getInstance().reloadPrimaryOrg(getTenantId());
		TenantLimitService.getInstance().updateAll();
		return result;
	}

	
	
	private String upgradeAccount() {
		String result;
		Transaction transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		
		try {
			upgradeAccount(transaction);
			result = handleUpgradeSuccess(transaction, "message.upgrade_successful");
		} catch (CommunicationException e) {
			result = handleUpgradeError(transaction, "error.could_not_contact_billing_provider", e);
		} catch (UpgradeCompletionException e) {
			result = handleUpgradeError(transaction, "error.applying_upgrade_to_account", e);
			
		} catch (Exception e) {
			result = handleUpgradeError(transaction, "error.could_not_upgrade", e);
		} 
		return result;
	}
	
	
	private String handleUpgradeSuccess(Transaction transaction, String successMessage) {
		addActionMessageText(successMessage);
		logger.info("upgrade complete for tenant " + getPrimaryOrg().getName());
		com.n4systems.persistence.PersistenceManager.finishTransaction(transaction);
		return SUCCESS;
	}


	private String handleUpgradeError(Transaction transaction, String errorMessage, Exception e) {
		com.n4systems.persistence.PersistenceManager.rollbackTransaction(transaction);
		logger.error("upgrade failed", e);
		addFlashErrorText(errorMessage);
		return ERROR;
	}

	private void upgradeAccount(Transaction transaction) throws CommunicationException, UpgradeCompletionException {
		UpgradeRequest upgradeRequest = createUpgradeRequest();
		
		UpgradeResponse upgradeResponse = getUpgradeHandler().upgradeTo(upgradeRequest, transaction);
		
		if (upgradeResponse == null) {
			throw new ProcessFailureException("upgrading the account was unsuccessful");
		}
		upgradeCost = upgradeResponse.getCost();
	}
	private SignUpPackage getCurrentPackage() {
		if (currentPackage == null) {
			currentPackage = accountHelper.currentPackageFilter().getCurrentPackage(getAllSignUpPackages());
		}
		return currentPackage;
	}

	private boolean packageHasUserLimit() {
		return !getSignUpDetails().getUsers().equals(TenantLimit.UNLIMITED);
			
	}
	private boolean accountHasReachedUserLimit() {
		return getSignUpDetails().getUsers() <= getLimits().getEmployeeUsersMax();
	}

	private SignUpPackageDetails getSignUpDetails() {
		return getCurrentPackage().getSignPackageDetails();
	}
	
	public boolean isUnderEmployeePlanLimit() {
		Long limit = getEmployeeLimit();
		if (limit == null) {
			return true; 
		}

		return (employeeTotal() <= limit);
	}

	private long employeeTotal() {
		return getLimits().getEmployeeUsersMax() + additionalEmployee;
	}
	
	public Long getEmployeeLimit() {
		if (TenantLimit.isUnlimited(getSignUpDetails().getUsers())) {
			return null;
		}
		return getSignUpDetails().getUsers();
	}

	public UpgradeCost getUpgradeCost() {
		return upgradeCost;
	}
	
	
	private List<SignUpPackage> getAllSignUpPackages() {
		if (allSignUpPackages == null) {
			allSignUpPackages = getNonSecureLoaderFactory().createSignUpPackageListLoader().load();
		}
		return allSignUpPackages;
	}

	
	public Integer getAdditionalEmployee() {
		return additionalEmployee;
	}

	@IntRangeFieldValidator(message="", key="error.number_of_additional_employee_accounts_must_be_greater_than_zero", min="1")
	@FieldExpressionValidator(message="", key="error.you_may_only_x_have_employee_accounts_on_this_play", expression="underEmployeePlanLimit")
	public void setAdditionalEmployee(Integer additionalEmployee) {
		this.additionalEmployee = additionalEmployee;
	}
	
	public UpgradePackageFilter getUpgradeFilter() {
		return accountHelper.currentPackageFilter();
	}
	
}
