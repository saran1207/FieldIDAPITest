package com.n4systems.fieldid.actions.subscriptions;

import java.util.List;

import com.n4systems.services.TenantFinder;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.handlers.creator.signup.IncreaseEmployeeLimitHandlerImpl;
import com.n4systems.handlers.creator.signup.UpgradeAccountHandler;
import com.n4systems.handlers.creator.signup.UpgradeCompletionException;
import com.n4systems.handlers.creator.signup.UpgradeRequest;
import com.n4systems.handlers.creator.signup.exceptions.BillingValidationException;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.signuppackage.UpgradePackageFilter;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.user.AdminUserListLoader;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.services.limiters.TenantLimitService;
import com.n4systems.subscription.BillingInfoException;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.subscription.UpgradeResponse;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.TemplateMailMessage;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
@UserPermissionFilter(userRequiresOneOf={Permissions.AccessWebStore})
public class IncreaseEmployeeLimitCrud extends AbstractUpgradeCrud {
	private static Logger logger = Logger.getLogger(IncreaseEmployeeLimitCrud.class);
	
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
	

	@SkipValidation
	public String doAdd() {
		if (accountHelper.currentPackageFilter().isLegacy()) {
			return ERROR;
		}
		
		if (packageHasUserLimit() && accountHasReachedUserLimit()) {
			return "limitExceeded";
		}
		
		additionalEmployee = 1;
		
		
		return SUCCESS;
	}
	
	protected void findUpgradeCost() {
		UpgradeRequest upgradeRequest = createUpgradeRequest();
		upgradeRequest.setUpdatedBillingInformation(false);
		try {
			upgradeCost = getUpgradeHandler().priceForUpgrade(upgradeRequest);
		} catch (CommunicationException e) {
			upgradeCost = UpgradeCost.nullUpgradeCost();
		} catch (Exception e) {
			upgradeCost = UpgradeCost.nullUpgradeCost();
		}
	}

	private UpgradeAccountHandler getUpgradeHandler() {
		return new IncreaseEmployeeLimitHandlerImpl(getPrimaryOrg(), getCreateHandlerFactory().getSubscriptionAgent(), new OrgSaver());
	}

	
	

	protected void setUpgradeInformation(UpgradeRequest upgradeRequest) {
		upgradeRequest.setContractExternalId(getUpgradeFilter().getCurrentContract().getExternalId());
		
		upgradeRequest.setFrequency(getUpgradeFilter().getCurrentContract().getPaymentOption().getFrequency());
		upgradeRequest.setMonths(getUpgradeFilter().getCurrentContract().getPaymentOption().getTerm());
		upgradeRequest.setPurchasingPhoneSupport(accountHelper.getCurrentSubscription().isPhonesupport());
		
		upgradeRequest.setNewUsers(additionalEmployee);
	}
	
	
	@SkipValidation
	public String doShowCost() {
		findUpgradeCost();
		return SUCCESS;
	}
	
	public String doCreate() {
		
		String result = upgradeAccount();
		TenantLimitService.getInstance().updateAll();
		return result;
	}

	
	
	private String upgradeAccount() {
		String result;
		Transaction transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		
		try {
			upgradeAccount(transaction);
			result = handleUpgradeSuccess(transaction, "message.upgrade_successful");
		} catch (BillingValidationException e) {
			addFieldError("creditCard", getText("error.credit_card_information_is_incorrect"));
			logger.debug("billing information incorrect", e);
			result = INPUT;
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
		logger.info("upgrade complete for tenant " + getPrimaryOrg().getName() + " employee limit increased by " + additionalEmployee + " by user " + getSessionUserId());
		com.n4systems.persistence.PersistenceManager.finishTransaction(transaction);
		sendUpgradeCompleteEmail();
		return SUCCESS;
	}


	private String handleUpgradeError(Transaction transaction, String errorMessage, Exception e) {
		com.n4systems.persistence.PersistenceManager.rollbackTransaction(transaction);
		logger.error("upgrade failed", e);
		addFlashErrorText(errorMessage);
		return ERROR;
	}

	private void upgradeAccount(Transaction transaction) throws CommunicationException, UpgradeCompletionException, BillingInfoException {
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

	public SignUpPackageDetails getSignUpDetails() {
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

	
	
	
	private List<SignUpPackage> getAllSignUpPackages() {
		if (allSignUpPackages == null) {
			allSignUpPackages = getNonSecureLoaderFactory().createSignUpPackageListLoader().load();
		}
		return allSignUpPackages;
	}

	
	public Integer getAdditionalEmployee() {
		return additionalEmployee;
	}

	@RequiredFieldValidator(message="", key="error.number_of_additional_employee_accounts_must_be_greater_than_zero")
	@IntRangeFieldValidator(message="", key="error.number_of_additional_employee_accounts_must_be_greater_than_zero", min="1")
	@FieldExpressionValidator(message="", key="error.limited_employee_accounts", expression="underEmployeePlanLimit")
	public void setAdditionalEmployee(Integer additionalEmployee) {
		this.additionalEmployee = additionalEmployee;
	}
	
	public UpgradePackageFilter getUpgradeFilter() {
		return accountHelper.currentPackageFilter();
	}

	@Override
	protected String getWhatWasBeingBought() {
		return " employee limit increase of " + additionalEmployee;
	}

	protected MailMessage createUpgradeMessage() {
		TemplateMailMessage invitationMessage = new TemplateMailMessage("Your Field ID Account Has Been Upgraded", "increaseEmployeeUpgrade");
		String emailAddress = new AdminUserListLoader(getSecurityFilter()).load().get(0).getEmailAddress();
		invitationMessage.getToAddresses().add(emailAddress);
		if (!getConfigContext().getString(ConfigEntry.SALES_ADDRESS).equals("")){
			invitationMessage.getBccAddresses().add("sales@fieldid.com");
		}
		
		return invitationMessage;
	}
	
	
}
