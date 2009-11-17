package com.n4systems.fieldid.actions.signup;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.handlers.creator.signup.UpgradeHandlerImpl;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.signuppackage.SignUpPackageLoader;
import com.n4systems.model.signuppackage.UpgradePackageFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.services.TenantCache;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

public class AccountUpgrade extends AbstractAction {

	private List<SignUpPackage> availablePackagesForUpdate;


	private SignUpPackage upgradePackage;

	public AccountUpgrade(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}


	@SkipValidation
	public String doAdd() {
		return SUCCESS;
		
	}

	public String doCreate() {
		String result = null;
		
		Transaction transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		
		try {
			new UpgradeHandlerImpl(getPrimaryOrg(), new OrgSaver()).upgradeTo(upgradePackage.getSignPackageDetails(), transaction);
			// do i need this?
			TenantCache.getInstance().reloadPrimaryOrg(getTenantId());
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


	public SignUpPackage currentPackage() {
		return getNonSecureLoaderFactory().createSignUpPackageListLoader().load().get(0);
	}
	
	public List<SignUpPackage> getPackages() {
		if (availablePackagesForUpdate == null) {
			List<SignUpPackage> fullPackageList = getNonSecureLoaderFactory().createSignUpPackageListLoader().load();
			availablePackagesForUpdate = new UpgradePackageFilter(currentPackage().getSignPackageDetails()).reduceToAvailablePackages(fullPackageList);
			
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

}
