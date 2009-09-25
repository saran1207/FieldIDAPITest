package com.n4systems.fieldid.permissions;

import com.n4systems.security.Permissions;

public abstract class AbstractUserSecurityTest {
	protected String constructMethodName(int permission) {
		return "isAllowed"+resolvePermissionName(permission);
	}
	
	protected String resolvePermissionName(int permission) {
		String permissionName = null;
		
		switch (permission) {
			case Permissions.Tag : permissionName="Tag"; break;
			case Permissions.CreateInspection : permissionName = "CreateInspection"; break;
			case Permissions.EditInspection : permissionName = "EditInspection"; break;
			case Permissions.ManageEndUsers : permissionName = "ManageEndUsers"; break;
			case Permissions.ManageJobs : permissionName = "ManageJobs"; break;
			case Permissions.ManageSafetyNetwork : permissionName = "ManageSafetyNetwork"; break;
			case Permissions.ManageSystemConfig : permissionName = "ManageSystemConfig"; break;
			case Permissions.ManageSystemUsers : permissionName = "ManageSystemUsers"; break;		
		}
		
		return permissionName;
	}
}
