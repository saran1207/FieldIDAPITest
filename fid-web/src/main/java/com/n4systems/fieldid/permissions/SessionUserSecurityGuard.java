package com.n4systems.fieldid.permissions;


import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

public class SessionUserSecurityGuard implements UserSecurityGuard {

	private final int permissions;
	
	public SessionUserSecurityGuard(User user) {
		permissions = user.getPermissions();
	}
	
	public boolean isAllowedManageSafetyNetwork() {
		return Permissions.hasOneOf(permissions, Permissions.MANAGE_SAFETY_NETWORK);
	}

	public boolean isAllowedCreateEvent() {
		return Permissions.hasOneOf(permissions, Permissions.CREATE_EVENT);
	}

	public boolean isAllowedEditEvent() {
		return Permissions.hasOneOf(permissions, Permissions.EDIT_EVENT);
	}

	public boolean isAllowedManageEndUsers() {
		return Permissions.hasOneOf(permissions, Permissions.MANAGE_END_USERS);
	}

	public boolean isAllowedManageJobs() {
		return Permissions.hasOneOf(permissions, Permissions.MANAGE_JOBS);
	}

	public boolean isAllowedManageSystemConfig() {
		return Permissions.hasOneOf(permissions, Permissions.MANAGE_SYSTEM_CONFIG);
	}

	public boolean isAllowedTag() {
		return Permissions.hasOneOf(permissions, Permissions.TAG);
	}

	public boolean isAllowedManageSystemUsers() {
		return Permissions.hasOneOf(permissions, Permissions.MANAGE_SYSTEM_USERS);
	}

}
