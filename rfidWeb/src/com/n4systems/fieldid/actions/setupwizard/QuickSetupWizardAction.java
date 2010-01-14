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
import com.n4systems.persistence.PersistenceProvider;
import com.n4systems.persistence.StandardPersistenceProvider;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantCache;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class QuickSetupWizardAction extends AbstractAction {

	private boolean turnOnJobSites;
	private PersistenceProvider persistenceProvider;
	
	
	public QuickSetupWizardAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doShow() {
		getSession().getSeenItRegistry().iHaveSeen(SeenItItem.SetupWizard);
		return SUCCESS;
	}
	
	public String doStep1() {
		turnOnJobSites = getPrimaryOrg().hasExtendedFeature(ExtendedFeature.JobSites);
		return SUCCESS;
	}
	
	public String doStep1Complete() {
		Transaction transaction = persistenceProvider().startTransaction();
		
		try {
			updateFeatures(transaction);
			
			persistenceProvider().finishTransaction(transaction);
			
			clearCachedValues();
		} catch (Exception e) {
			persistenceProvider().rollbackTransaction(transaction);
			addActionErrorText("error.could_not_setup_your_company_profile");
			return ERROR;
		}
		addFlashMessageText("message.company_profile_setup");
		return SUCCESS;
	}
	
	
	public String doCompleted() {
		return SUCCESS;
	}
	
	private PersistenceProvider persistenceProvider() {
		if (persistenceProvider == null) {
			persistenceProvider = new StandardPersistenceProvider();
		}
		return persistenceProvider;
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
