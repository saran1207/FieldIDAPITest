package com.n4systems.fieldid.permissions;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.security.Permissions;

public class UserAccessController {
	private final AbstractAction action;
	private final AnnotationFilterLocator<UserPermissionFilter> filterLocator;
	
	public UserAccessController(AbstractAction action) {
		this.action = action;
		this.filterLocator = new AnnotationFilterLocator<UserPermissionFilter>(action.getClass(), UserPermissionFilter.class);
	}

	public boolean userHasAccessToAction(String methodName) {
		UserPermissionFilter filter = filterLocator.getFilter(methodName);
		if (allowAnyOne(filter)) {
			return true;
		}
		return Permissions.hasOneOf(action.getSessionUser().getPermissions(), filter.userRequiresOneOf());
	}

	private boolean allowAnyOne(UserPermissionFilter filter) {
		return filter == null || filter.open();
	}
}
