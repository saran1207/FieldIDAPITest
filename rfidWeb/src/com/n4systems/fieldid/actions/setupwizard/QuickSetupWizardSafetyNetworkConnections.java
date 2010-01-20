package com.n4systems.fieldid.actions.setupwizard;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.safetyNetwork.SafetyNetworkConnectionCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSafetyNetwork})
public class QuickSetupWizardSafetyNetworkConnections extends SafetyNetworkConnectionCrud {
	
	private Long idImported;

	public QuickSetupWizardSafetyNetworkConnections(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@Override
	public String doList() {
		if (idImported != null) {
			getSession().getQuickSetupWizardImports().add(idImported);
		}
		return super.doList();
	}

	public boolean isImportedFromConnection(TypedOrgConnection connection) {
		return getSession().getQuickSetupWizardImports().contains(connection.getConnectedOrg().getTenant().getId());
	}
	
	public boolean isHaveAnyImportsBeenCompleted() {
		return !getSession().getQuickSetupWizardImports().isEmpty();
	}

	public Long getIdImported() {
		return idImported;
	}

	public void setIdImported(Long idImported) {
		this.idImported = idImported;
	}
	
}
