package com.n4systems.fieldid.actions.subscriptions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.handlers.creator.signup.UpgradeAccountHandler;
import com.n4systems.handlers.creator.signup.UpgradeCompletionException;
import com.n4systems.handlers.creator.signup.UpgradePlanHandlerImpl;
import com.n4systems.handlers.creator.signup.UpgradeRequest;
import com.n4systems.handlers.creator.signup.exceptions.BillingValidationException;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.signuppackage.SignUpPackageLoader;
import com.n4systems.model.signuppackage.UpgradePackageFilter;
import com.n4systems.model.user.AdminUserListLoader;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantCache;
import com.n4systems.services.limiters.TenantLimitService;
import com.n4systems.subscription.BillingInfoException;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.PaymentOption;
import com.n4systems.subscription.UpgradeResponse;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.TemplateMailMessage;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;


@UserPermissionFilter(userRequiresOneOf={Permissions.AccessWebStore})
public class UpgradePlanCrud extends AbstractUpgradeCrud {
	static final Logger logger = Logger.getLogger(UpgradePlanCrud.class);
	
	
	private List<DecoratedSignUpPackage> availablePackagesForUpdate;
	private SignUpPackage upgradePackage;
	private PaymentOption paymentOption;
	private boolean phoneSupport;
	
	private List<SignUpPackage> allSignUpPackages;
	
	
	public UpgradePlanCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	
	@Override
	protected void initMemberFields() {
	}
	
	@Override
	protected void loadMemberFields(Long uniqueId) {
	}
	
	private void testRequiredEntities() {
		if (upgradePackage == null || !isUpgradePackageAvailable()) { 
			addActionErrorText("error.valid_upgrade_package_must_be_selected");
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doShowCost() {
		testRequiredEntities();
		
		findUpgradeCost();
		if (upgradeCost == null) {
			addActionErrorText("error.could_not_contact_billing_provider");
			return ERROR;
		}
		
		return SUCCESS;
	}


	protected void findUpgradeCost() {
		UpgradeRequest upgradeRequest = createUpgradeRequest();
		upgradeRequest.setUpdatedBillingInformation(false);
		try {
			upgradeCost = getUpgradeHandler().priceForUpgrade(upgradeRequest);
		} catch (CommunicationException e) {
			upgradeCost = null;
		}
	}
	
	@SkipValidation
	public String doAdd() {
		testRequiredEntities();
		
		findUpgradeCost();
		if (upgradeCost == null) {
			addActionErrorText("error.could_not_contact_billing_provider");
			return ERROR;
		}
		if (isFreeAccount()) {
			setPaymentOption(PaymentOption.preferredOption().name());
		}
		
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities();
		String result = upgradeAccount();
		updateCachedValues();
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

	
	protected UpgradeAccountHandler getUpgradeHandler() {
		return new UpgradePlanHandlerImpl(getPrimaryOrg(), new OrgSaver(), getCreateHandlerFactory().getSubscriptionAgent());
	}

	ContractPricing upgradeContract() {
		if (paymentOption != null) {
			return upgradePackage.getContract(paymentOption);
		}
		return accountHelper.currentPackageFilter().getUpgradeContractForPackage(upgradePackage);
	}


	private String handleUpgradeSuccess(Transaction transaction, String successMessage) {
		addActionMessageText(successMessage);
		logger.info("upgrade complete for tenant " + getPrimaryOrg().getName() + " to plan " + upgradePackage.getName() + " by user " + getSessionUserId());
		com.n4systems.persistence.PersistenceManager.finishTransaction(transaction);
		sendUpgradeCompleteEmail();
		return SUCCESS;
	}


	private String handleUpgradeError(Transaction transaction, String errorMessage, Exception e) {
		com.n4systems.persistence.PersistenceManager.rollbackTransaction(transaction);
		logger.error("upgrade failed", e);
		addFlashErrorText(errorMessage);
		sendNotificationOfIncompleteUpgrade();
		return ERROR;
	}


	private void updateCachedValues() {
		TenantCache.getInstance().reloadPrimaryOrg(getTenantId());
		TenantLimitService.getInstance().updateAll();
	}


	private void upgradeAccount(Transaction transaction) throws CommunicationException, UpgradeCompletionException, BillingInfoException {
		UpgradeRequest upgradeRequest = createUpgradeRequest();
		
		upgradeResponse = getUpgradeHandler().upgradeTo(upgradeRequest, transaction);
		
		if (upgradeResponse == null) {
			throw new ProcessFailureException("upgrading the account was unsuccessful");
		}
		upgradeCost = upgradeResponse.getCost();
	}


	protected void setUpgradeInformation(UpgradeRequest upgradeRequest) {
		ContractPricing upgradeContract = upgradeContract();
		upgradeRequest.setUpgradePackage(upgradeContract.getSignUpPackage());
		upgradeRequest.setContractExternalId(upgradeContract.getExternalId());
		
		upgradeRequest.setFrequency(upgradeContract.getPaymentOption().getFrequency());
		upgradeRequest.setMonths(upgradeContract.getPaymentOption().getTerm());
		upgradeRequest.setPurchasingPhoneSupport(getPhoneSupport());
	}


	


	private boolean getPhoneSupport() {
		if (isFreeAccount()) {
			return isPurchasingPhoneSupport();
		}
		
		return accountCurrentlyHasPhoneSupport();
	}


	private boolean accountCurrentlyHasPhoneSupport() {
		try {
			return accountHelper.getCurrentSubscription().isPhonesupport();
		} catch (CommunicationException e) {
			return false;
		}
	}

	

	public boolean isFreeAccount() {
		return currentPackageFilter().getCurrentPackage(getAllSignUpPackages()).isFree();
	}


	public UpgradePackageFilter currentPackageFilter() {
		return accountHelper.currentPackageFilter();
	}
	
	public List<DecoratedSignUpPackage> getPackages() {
		if (availablePackagesForUpdate == null) {
			availablePackagesForUpdate = new ArrayList<DecoratedSignUpPackage>();
			
			availablePackagesForUpdate.add(new CurrentSignUpPackage(currentPackageFilter().getCurrentPackage(getAllSignUpPackages())));
			for (SignUpPackage upgradePackage : currentPackageFilter().reduceToAvailablePackages(allSignUpPackages)) {
				availablePackagesForUpdate.add(new UpgradablePackage(upgradePackage));
			}
		}
		return availablePackagesForUpdate;
	}


	private List<SignUpPackage> getAllSignUpPackages() {
		if (allSignUpPackages == null) {
			allSignUpPackages = getNonSecureLoaderFactory().createSignUpPackageListLoader().load();
		}
		return allSignUpPackages;
	}
	
	
	@RequiredFieldValidator(message="", key="error.valid_upgrade_package_must_be_selected")
	@ExpressionValidator(message="", key="error.valid_upgrade_package_must_be_selected", expression="upgradePackageAvailable")
	public String getUpgradePackageId() {
		return (upgradePackage != null) ? upgradePackage.getName() : null;
	}
	
	public void setUpgradePackageId(String signUpPackageId) {
		SignUpPackageDetails targetPackage = SignUpPackageDetails.valueOf(signUpPackageId);
		SignUpPackageLoader loader = getNonSecureLoaderFactory().createSignUpPackageLoader();
		upgradePackage = loader.setSignUpPackageTarget(targetPackage).load();
	}
	
	
	public void setPackageId(String signUpPackageId) {
		setUpgradePackageId(signUpPackageId);
	}

	public SignUpPackage getUpgradePackage() {
		return upgradePackage;
	}
	
	
	public UpgradeResponse getUpgradeResponse() {
		return upgradeResponse;
	}
	
	
	public boolean isUpgradePackageAvailable() {
		return getPackages().contains(upgradePackage);
	}
	
	public String getPaymentOption() {
		return paymentOption != null ? paymentOption.name() : null;
	}


	public void setPaymentOption(String paymentOption) {
		this.paymentOption = PaymentOption.valueOf(paymentOption);
	}
	

	public boolean isPurchasingPhoneSupport() {
		return phoneSupport;
	}


	public void setPurchasingPhoneSupport(boolean phoneSupport) {
		this.phoneSupport = phoneSupport;
	}
	
	@Override
	protected String getWhatWasBeingBought() {
		return "contract " + upgradeContract();
	}
	
	protected MailMessage createUpgradeMessage() {
		TemplateMailMessage invitationMessage = new TemplateMailMessage("Your Field ID Account Has Been Upgraded", "planUpgrade");
		String emailAddress = new AdminUserListLoader(getSecurityFilter()).load().get(0).getEmailAddress();
		invitationMessage.getToAddresses().add(emailAddress);
		invitationMessage.getBccAddresses().add("sales@fieldid.com");
		
		
		
		return invitationMessage;
	}

	/**
	 * these decorator classes add the is current method the sign up packages. which allows the view to render the 
	 * selection of products correctly.
	 * They have been placed here since this is the only context that they are needed in.
	 * 
	 * @author aaitken
	 *
	 */
	public abstract class DecoratedSignUpPackage extends SignUpPackage {
		public DecoratedSignUpPackage(SignUpPackage signPackage) {
			super(signPackage.getSignPackageDetails(), signPackage.getPaymentOptions());
		}
		
		public abstract boolean isCurrent();
		
	}
	
	public class CurrentSignUpPackage extends DecoratedSignUpPackage {
		public CurrentSignUpPackage(SignUpPackage signPackage) {
			super(signPackage);
		}
		
		public boolean isCurrent() {
			return true;
		}
		
	}
	
	public class UpgradablePackage extends DecoratedSignUpPackage {
		public UpgradablePackage(SignUpPackage signPackage) {
			super(signPackage);
		}
		
		public boolean isCurrent() {
			return false;
		}
		
	}

	
	
	

	

	
}
