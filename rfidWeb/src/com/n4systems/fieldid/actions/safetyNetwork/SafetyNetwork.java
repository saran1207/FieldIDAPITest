package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigEntry;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSafetyNetwork})
public class SafetyNetwork extends AbstractAction {
	private static final long serialVersionUID = 1L;

	public SafetyNetwork(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doShow() {
		return SUCCESS;
	}

	public String getHelpUrl() {
		return getConfigContext().getString(ConfigEntry.SAFETY_NETWORK_HELP_URL);
	}
	
	public String getVideoUrl() {
		return getConfigContext().getString(ConfigEntry.SAFETY_NETWORK_VIDEO_URL);
	}
}
