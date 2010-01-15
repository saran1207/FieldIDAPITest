package com.n4systems.fieldid.actions;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.security.Permissions;

public class SetupAction extends SimpleAction {

	public SetupAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	@UserPermissionFilter(userRequiresOneOf={Permissions.ManageEndUsers, Permissions.ManageSystemConfig, Permissions.ManageSystemUsers})
	public String execute() {
		return super.execute();
	}
	
	
	

}
