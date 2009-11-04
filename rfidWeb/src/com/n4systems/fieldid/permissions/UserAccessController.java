package com.n4systems.fieldid.permissions;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.security.Permissions;

public class UserAccessController {
	private final AbstractAction action;
	private final UserPermissionLocator permissionLocator;
	
	public UserAccessController(AbstractAction action) {
		this.action = action;
		this.permissionLocator = new UserPermissionLocator(action.getClass()); 
	}

	public boolean userHasAccessToAction(String methodName) {
		int[] userRequiresOneOfThesePermissions = permissionLocator.getActionPermissionRequirements(methodName);
		if (userRequiresOneOfThesePermissions.length == 0) {
			return true;
		} 
		
		return Permissions.hasOneOf(action.getSessionUser().getPermissions(), userRequiresOneOfThesePermissions);
	}

}
