package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.security.Permissions;


@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSafetyNetwork})
public class PrivacySettingsAction extends SafetyNetwork {
	private static final long serialVersionUID = 1L;
	
	private final OrgSaver saver;
	
	private boolean autoPublish;
	private boolean autoAcceptConnections;
    private boolean searchableOnSafetyNetwork;
	
	public PrivacySettingsAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.saver = new OrgSaver();
	}

	public String doShow() {
		
		autoPublish = getPrimaryOrg().isAutoPublish();
		autoAcceptConnections = getPrimaryOrg().isAutoAcceptConnections();
        searchableOnSafetyNetwork = getPrimaryOrg().isSearchableOnSafetyNetwork();
		
		return SUCCESS;
	}
	
	public String doSave() {
		PrimaryOrg primaryOrg = getPrimaryOrg();
		primaryOrg.setAutoPublish(autoPublish);
		primaryOrg.setAutoAcceptConnections(autoAcceptConnections);
        primaryOrg.setSearchableOnSafetyNetwork(searchableOnSafetyNetwork);
		saver.saveOrUpdate(primaryOrg);
		addFlashMessageText("label.settings_updated");
		return SUCCESS;
	}

	public boolean isAutoPublish() {
		return autoPublish;
	}

	public void setAutoPublish(boolean autoPublish) {
		this.autoPublish = autoPublish;
	}

	public boolean isAutoAcceptConnections() {
		return autoAcceptConnections;
	}

	public void setAutoAcceptConnections(boolean autoAcceptConnections) {
		this.autoAcceptConnections = autoAcceptConnections;
	}

    public boolean isSearchableOnSafetyNetwork() {
        return searchableOnSafetyNetwork;
    }

    public void setSearchableOnSafetyNetwork(boolean searchableOnSafetyNetwork) {
        this.searchableOnSafetyNetwork = searchableOnSafetyNetwork;
    }
}
