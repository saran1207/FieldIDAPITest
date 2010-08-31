package com.n4systems.fieldid.permissions;


import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

public class SessionUserSecurityGuard implements UserSecurityGuard {

	private final int permissions;
	
	public SessionUserSecurityGuard(User user) {
		permissions = user.getPermissions();
	}
	
	public boolean isAllowedManageSafetyNetwork() {
		return Permissions.hasOneOf(permissions, Permissions.ManageSafetyNetwork);
	}

	public boolean isAllowedCreateInspection() {
		return Permissions.hasOneOf(permissions, Permissions.CreateInspection);
	}

	public boolean isAllowedEditInspection() {
		return Permissions.hasOneOf(permissions, Permissions.EditInspection);
	}

	public boolean isAllowedManageEndUsers() {
		return Permissions.hasOneOf(permissions, Permissions.ManageEndUsers);
	}

	public boolean isAllowedManageJobs() {
		return Permissions.hasOneOf(permissions, Permissions.ManageJobs);
	}

	public boolean isAllowedManageSystemConfig() {
		return Permissions.hasOneOf(permissions, Permissions.ManageSystemConfig);
	}

	public boolean isAllowedTag() {
		return Permissions.hasOneOf(permissions, Permissions.Tag);
	}

	public boolean isAllowedManageSystemUsers() {
		return Permissions.hasOneOf(permissions, Permissions.ManageSystemUsers);
	}
	
	public boolean isAllowedAccessWebStore() {
		return Permissions.hasOneOf(permissions, Permissions.AccessWebStore);
	}
}
