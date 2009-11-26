package com.n4systems.fieldid.actions.signup;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.MailManagerImpl;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.handlers.creator.signup.UpgradeCompletionException;
import com.n4systems.handlers.creator.signup.UpgradeHandlerImpl;
import com.n4systems.handlers.creator.signup.UpgradeRequest;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.signuppackage.SignUpPackageLoader;
import com.n4systems.model.signuppackage.UpgradePackageFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantCache;
import com.n4systems.services.limiters.TenantLimitService;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.subscription.UpgradeResponse;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.mail.MailMessage;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;


@UserPermissionFilter(userRequiresOneOf={Permissions.AccessWebStore})
public class AccountUpgrade extends AbstractCrud {
	private static final Logger logger = Logger.getLogger(AccountUpgrade.class);
	
	
	private List<DecoratedSignUpPackage> availablePackagesForUpdate;
	private SignUpPackage upgradePackage;
	private AccountHelper accountHelper;
	private UpgradeCost upgradeCost;
	private UpgradeResponse upgradeResponse;
	
	public AccountUpgrade(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	
	@Override
	protected void initMemberFields() {
	}
	@Override
	protected void loadMemberFields(Long uniqueId) {
	}
	
	private void testRequiredEntities() {
		if (upgradePackage == null) { 
			addActionErrorText("error.");
			throw new MissingEntityException();
		}
	}

	@Override
	protected void postInit() {
		super.postInit();
		accountHelper = new AccountHelper(getCreateHandlerFactory().getSubscriptionAgent(), getPrimaryOrg(), getNonSecureLoaderFactory().createSignUpPackageListLoader());
	}


	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	
	public String doAdd() {
		testRequiredEntities();
		UpgradeRequest upgradeRequest = createUpgradeRequest();
			
		try {
			upgradeCost = getUpgradeHandler().priceForUpgrade(upgradeRequest);
		} catch (CommunicationException e) {
			upgradeCost = null;
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
		} catch (CommunicationException e) {
			result = handleUpgradeError(transaction, "error.could_not_contact_billing_provider", e);
		} catch (UpgradeCompletionException e) {
			result = handleUpgradeError(transaction, "error.applying_upgrade_to_account", e);
			sendNotificationOfIncompleteUpgrade(getPrimaryOrg(), e);
		} catch (Exception e) {
			result = handleUpgradeError(transaction, "error.could_not_upgrade", e);
		} 
		return result;
	}


	private void sendNotificationOfIncompleteUpgrade(PrimaryOrg primaryOrg, UpgradeCompletionException e) {
		MailMessage message = new MailMessage();
		
		message.getToAddresses().add(ConfigContext.getCurrentContext().getString(ConfigEntry.FIELDID_ADMINISTRATOR_EMAIL));
		message.setSubject("FAILED TO APPLY UPGRADE to " + getPrimaryOrg().getName());
		message.setBody("could not upgrade tenant " + getPrimaryOrg().getName() + " purchasing contract " + e.getResponse().getContractId());
		
		try {
			new MailManagerImpl().sendMessage(message);
		} catch (Exception e1) {
			logger.error("failed to send message about failure", e1);
		}
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


	private void updateCachedValues() {
		TenantCache.getInstance().reloadPrimaryOrg(getTenantId());
		TenantLimitService.getInstance().updateAll();
	}


	private void upgradeAccount(Transaction transaction) throws CommunicationException, UpgradeCompletionException {
		UpgradeRequest upgradeRequest = createUpgradeRequest();
		
		upgradeResponse = getUpgradeHandler().upgradeTo(upgradeRequest, transaction);
		
		
		if (upgradeResponse == null) {
			throw new ProcessFailureException("upgrading the account was unsuccessful");
		}
	}


	private UpgradeRequest createUpgradeRequest() {
		ContractPricing upgradeContract = accountHelper.currentPackageFilter().getUpgradeContractForPackage(upgradePackage);
		UpgradeRequest upgradeRequest = new UpgradeRequest();
		upgradeRequest.setUpgradePackage(upgradeContract.getSignUpPackage());
		upgradeRequest.setContractExternalId(upgradeContract.getExternalId());
		upgradeRequest.setTenantExternalId(getPrimaryOrg().getExternalId());
		return upgradeRequest;
	}


	public UpgradePackageFilter currentPackageFilter() {
		return accountHelper.currentPackageFilter();
	}
	
	public List<DecoratedSignUpPackage> getPackages() {
		if (availablePackagesForUpdate == null) {
			availablePackagesForUpdate = new ArrayList<DecoratedSignUpPackage>();
			
			List<SignUpPackage> allSignUpPackages = getNonSecureLoaderFactory().createSignUpPackageListLoader().load();
			
			availablePackagesForUpdate.add(new CurrentSignUpPackage(currentPackageFilter().getCurrentPackage(allSignUpPackages)));
			for (SignUpPackage upgradePackage : currentPackageFilter().reduceToAvailablePackages(allSignUpPackages)) {
				availablePackagesForUpdate.add(new UpgradablePackage(upgradePackage));
			}
		}
		return availablePackagesForUpdate;
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
	
	
	public UpgradeCost getUpgradeCost() {
		return upgradeCost;
	}

	

	private UpgradeHandlerImpl getUpgradeHandler() {
		return new UpgradeHandlerImpl(getPrimaryOrg(), new OrgSaver(), getCreateHandlerFactory().getSubscriptionAgent());
	}


	public UpgradeResponse getUpgradeResponse() {
		return upgradeResponse;
	}
	
	
	public boolean isUpgradePackageAvailable() {
		return (!upgradePackage.getName().equalsIgnoreCase(currentPackageFilter().getPackageName()) &&
				getPackages().contains(upgradePackage));
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
