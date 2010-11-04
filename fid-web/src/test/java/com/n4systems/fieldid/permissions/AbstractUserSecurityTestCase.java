package com.n4systems.fieldid.permissions;

import com.n4systems.security.Permissions;

public abstract class AbstractUserSecurityTestCase {
	protected String constructMethodName(int permission) {
		return "isAllowed"+resolvePermissionName(permission);
	}
	
	protected String resolvePermissionName(int permission) {
		String permissionName = null;
		
		switch (permission) {
			case Permissions.Tag : permissionName="Tag"; break;
			case Permissions.CreateEvent: permissionName = "CreateEvent"; break;
			case Permissions.EditEvent: permissionName = "EditEvent"; break;
			case Permissions.ManageEndUsers : permissionName = "ManageEndUsers"; break;
			case Permissions.ManageJobs : permissionName = "ManageJobs"; break;
			case Permissions.ManageSafetyNetwork : permissionName = "ManageSafetyNetwork"; break;
			case Permissions.ManageSystemConfig : permissionName = "ManageSystemConfig"; break;
			case Permissions.ManageSystemUsers : permissionName = "ManageSystemUsers"; break;
			case Permissions.AccessWebStore : permissionName = "AccessWebStore"; break;
		}
		
		return permissionName;
	}
}
