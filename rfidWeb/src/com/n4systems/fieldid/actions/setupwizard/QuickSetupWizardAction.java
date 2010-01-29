package com.n4systems.fieldid.actions.setupwizard;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.model.ui.seenit.SeenItItem;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantCache;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class QuickSetupWizardAction extends AbstractAction {

	private boolean turnOnJobSites;
	private TransactionManager transactionManager;
	
	
	public QuickSetupWizardAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doShow() {
		getSession().getSeenItRegistry().iHaveSeen(SeenItItem.SetupWizard);
		getSession().clearQuickSetupWizardImports();
		return SUCCESS;
	}
	
	public String doStep1() {
		turnOnJobSites = getPrimaryOrg().hasExtendedFeature(ExtendedFeature.JobSites);
		return SUCCESS;
	}
	
	public String doStep1Complete() {
		Transaction transaction = transactionManager().startTransaction();
		
		try {
			updateFeatures(transaction);
			
			transactionManager().finishTransaction(transaction);
			
			clearCachedValues();
		} catch (Exception e) {
			transactionManager().rollbackTransaction(transaction);
			addActionErrorText("error.could_not_setup_your_company_profile");
			return ERROR;
		}
		addFlashMessageText("message.company_profile_setup");
		return SUCCESS;
	}
	
	
	public String doCompleted() {
		return SUCCESS;
	}
	
	private TransactionManager transactionManager() {
		if (transactionManager == null) {
			transactionManager = new FieldIdTransactionManager();
		}
		return transactionManager;
	}


	private void updateFeatures(Transaction transaction) throws Exception {
		PrimaryOrg updatedPrimaryOrg = processJobSiteSetting(transaction);
		new OrgSaver().update(transaction, updatedPrimaryOrg);
	}


	private void clearCachedValues() {
		TenantCache.getInstance().reloadPrimaryOrg(getPrimaryOrg().getTenant().getId());
		refreshSessionUser();
	}
	
	private PrimaryOrg processJobSiteSetting(Transaction transaction) throws Exception {
		PrimaryOrg primaryOrg = getPrimaryOrg();
		ExtendedFeatureSwitch featureSwitch = ExtendedFeatureFactory.getSwitchFor(ExtendedFeature.JobSites, primaryOrg);
		
		if (turnOnJobSites) {
			featureSwitch.enableFeature(transaction);
		} else {
			featureSwitch.disableFeature(transaction);
		}
		return primaryOrg;
	}	

	public boolean isTurnOnJobSites() {
		return turnOnJobSites;
	}


	public void setTurnOnJobSites(boolean turnOnJobSites) {
		this.turnOnJobSites = turnOnJobSites;
	}

	

	
}
