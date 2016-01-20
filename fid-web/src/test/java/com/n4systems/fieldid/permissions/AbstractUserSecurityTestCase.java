package com.n4systems.fieldid.permissions;

import com.n4systems.security.Permissions;

public abstract class AbstractUserSecurityTestCase {
	protected String constructMethodName(int permission) {
		return "isAllowed"+resolvePermissionName(permission);
	}
	
	protected String resolvePermissionName(int permission) {
		String permissionName = null;
		
		switch (permission) {
			case Permissions.TAG: permissionName="Tag"; break;
			case Permissions.CREATE_EVENT: permissionName = "CreateEvent"; break;
			case Permissions.EDIT_EVENT: permissionName = "EditEvent"; break;
			case Permissions.MANAGE_END_USERS: permissionName = "ManageEndUsers"; break;
			case Permissions.MANAGE_JOBS: permissionName = "ManageJobs"; break;
			case Permissions.MANAGE_SAFETY_NETWORK: permissionName = "ManageSafetyNetwork"; break;
			case Permissions.MANAGE_SYSTEM_CONFIG: permissionName = "ManageSystemConfig"; break;
			case Permissions.MANAGE_SYSTEM_USERS: permissionName = "ManageSystemUsers"; break;
		}
		
		return permissionName;
	}
}
