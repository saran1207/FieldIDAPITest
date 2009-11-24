package com.n4systems.fieldid.actions.signup;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.handlers.creator.signup.UpgradeHandlerImpl;
import com.n4systems.handlers.creator.signup.UpgradeRequest;
import com.n4systems.model.orgs.OrgSaver;
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
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;


@UserPermissionFilter(userRequiresOneOf={Permissions.AccessWebStore})
public class AccountUpgrade extends AbstractCrud {
	
	private List<SignUpPackage> availablePackagesForUpdate;
	private SignUpPackage upgradePackage;
	private AccountHelper accountHelper;
	
	
	public AccountUpgrade(PersistenceManager persistenceManager) {
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
	public String doList() {
		return SUCCESS;
	}
	
	
	public String doAdd() {
		return SUCCESS;
		
	}

	public String doCreate() {
		String result = upgradeAccount();
		
		updateCachedValues();
		
		return result;
	}


	private String upgradeAccount() {
		String result;
		Transaction transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		
		try {
			upgradeAccount(transaction);
			
			addActionMessageText("message.upgrade_successful");
			result = SUCCESS;
		} catch (Exception e) {
			com.n4systems.persistence.PersistenceManager.rollbackTransaction(transaction);
			addFlashErrorText("error.could_not_upgrade");
			result = ERROR;
		} finally {
			com.n4systems.persistence.PersistenceManager.finishTransaction(transaction);
		}
		return result;
	}


	private void updateCachedValues() {
		TenantCache.getInstance().reloadPrimaryOrg(getTenantId());
		TenantLimitService.getInstance().updateAll();
	}


	private void upgradeAccount(Transaction transaction) {
		UpgradeRequest upgradeRequest = createUpgradeRequest();
		
		if (!new UpgradeHandlerImpl(getPrimaryOrg(), new OrgSaver(), getCreateHandlerFactory().getSubscriptionAgent()).upgradeTo(upgradeRequest, transaction)) {
			throw new RuntimeException();
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
	
	public List<SignUpPackage> getPackages() {
		if (availablePackagesForUpdate == null) {
			List<SignUpPackage> fullPackageList = getNonSecureLoaderFactory().createSignUpPackageListLoader().load();
			availablePackagesForUpdate = currentPackageFilter().reduceToAvailablePackages(fullPackageList);
		}
		return availablePackagesForUpdate;
	}
	
	
	@RequiredFieldValidator(message="", key="error.vaild_upgrade_package_must_be_selected")
	public String getUpgradePackageId() {
		return (upgradePackage != null) ? upgradePackage.getName() : null;
	}
	
	public void setUpgradePackageId(String signUpPackageId) {
		SignUpPackageDetails targetPackage = SignUpPackageDetails.valueOf(signUpPackageId);
		SignUpPackageLoader loader = getNonSecureLoaderFactory().createSignUpPackageLoader();
		upgradePackage = loader.setSignUpPackageTarget(targetPackage).load();
		
	}

	public SignUpPackage getUpgradePackage() {
		return upgradePackage;
	}
	
	
	public UpgradeCost getUpgradeCost() {
		UpgradeRequest upgradeRequest = createUpgradeRequest();
		upgradeRequest.setShowPriceOnly(true);
		
		UpgradeCost cost = null;
		try {
			cost = new UpgradeHandlerImpl(getPrimaryOrg(), new OrgSaver(), getCreateHandlerFactory().getSubscriptionAgent()).priceForUpgrade(upgradeRequest);
		} catch (CommunicationException e) {}
		
		
		return cost; 
	}


}
