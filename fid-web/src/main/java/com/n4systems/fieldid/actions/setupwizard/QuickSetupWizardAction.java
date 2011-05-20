package com.n4systems.fieldid.actions.setupwizard;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.tenant.extendedfeatures.ToggleExendedFeatureMethod;
import com.n4systems.model.ui.seenit.SeenItItem;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class QuickSetupWizardAction extends AbstractAction {

	private boolean turnOnJobSites;
	private boolean turnOnAssignedTo=false;
	private boolean turnOnProofTests = false;
	private boolean turnOnManufacturerCertificates = false;
	
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
		turnOnAssignedTo=getPrimaryOrg().hasExtendedFeature(ExtendedFeature.AssignedTo);
		turnOnProofTests=getPrimaryOrg().hasExtendedFeature(ExtendedFeature.ProofTestIntegration);
		turnOnManufacturerCertificates=getPrimaryOrg().hasExtendedFeature(ExtendedFeature.ManufacturerCertificate);
		return SUCCESS;
	}
	
	public String doStep1Complete() {
		Transaction transaction = transactionManager().startTransaction();
		
		try {
			updateExtendedFeatures(transaction);
			save(transaction);
			transactionManager().finishTransaction(transaction);
		
			addFlashMessageText("message.company_profile_setup");
		} catch (Exception e) {
			transactionManager().rollbackTransaction(transaction);
			addActionErrorText("error.could_not_setup_your_company_profile");
			return ERROR;
		} finally {
			clearCachedValues();
		}
		
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

	private void save(Transaction transaction) {
		new OrgSaver().update(transaction, getPrimaryOrg());
	}

	private void updateExtendedFeatures(Transaction transaction) throws Exception{
		new ToggleExendedFeatureMethod(ExtendedFeature.JobSites, turnOnJobSites).applyTo(getPrimaryOrg(), transaction);
		new ToggleExendedFeatureMethod(ExtendedFeature.AssignedTo, turnOnAssignedTo).applyTo(getPrimaryOrg(), transaction);
		new ToggleExendedFeatureMethod(ExtendedFeature.ProofTestIntegration, turnOnProofTests).applyTo(getPrimaryOrg(), transaction);
		new ToggleExendedFeatureMethod(ExtendedFeature.ManufacturerCertificate, turnOnManufacturerCertificates).applyTo(getPrimaryOrg(), transaction);
	}

	private void clearCachedValues() {
		refreshSessionUser();
	}
	
	public boolean isTurnOnJobSites() {
		return turnOnJobSites;
	}
	
	public void setTurnOnJobSites(boolean turnOnJobSites) {
		this.turnOnJobSites = turnOnJobSites;
	}

	public boolean isTurnOnAssignedTo() {
		return turnOnAssignedTo;
	}

	public void setTurnOnAssignedTo(boolean turnOnAssignedTo) {
		this.turnOnAssignedTo = turnOnAssignedTo;
	}

	public String getLoginUrl(){
		return getBaseBrandedNonFieldIDUrl(getTenant().getName());
	}

	public boolean isTurnOnProofTests() {
		return turnOnProofTests;
	}

	public void setTurnOnProofTests(boolean turnOnProofTests) {
		this.turnOnProofTests = turnOnProofTests;
	}

	public boolean isTurnOnManufacturerCertificates() {
		return turnOnManufacturerCertificates;
	}

	public void setTurnOnManufacturerCertificates(boolean turnOnManufacturerCertificate) {
		this.turnOnManufacturerCertificates = turnOnManufacturerCertificate;
	}
	
}
