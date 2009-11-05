package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.security.Permissions;


@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSafetyNetwork})
public class PrivacySettingsAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final OrgSaver saver;
	
	private boolean autoPublish;

	public PrivacySettingsAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.saver = new OrgSaver();
	}

	public String doShow() {
		
		autoPublish = getPrimaryOrg().isAutoPublish();
		
		return SUCCESS;
	}
	
	public String doSave() {
		PrimaryOrg primaryOrg = getPrimaryOrg();
		primaryOrg.setAutoPublish(autoPublish);
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

}
