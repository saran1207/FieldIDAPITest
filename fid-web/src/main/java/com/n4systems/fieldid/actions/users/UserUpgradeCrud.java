package com.n4systems.fieldid.actions.users;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.security.Permissions;

public class UserUpgradeCrud extends UserCrud{

	protected UserUpgradeCrud(UserManager userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}
	
	public String doChangeToFull() {
		//TODO set user type
		save();
		return SUCCESS;
	}
	
	public String doChangeToLite() {
		//TODO set user type
		save();
		return SUCCESS;
	}
	
	public String doChangeToReadOnly() {
		//TODO set user type
		save();
		return SUCCESS;
	}

	@Override
	public boolean isEmployee() {
		return user.isEmployee();
	}
	
	public boolean isFullUser() {
		return user.isEmployee();
	}
	
	public boolean isLiteUser() {
		return false;
	}
	
	public boolean isReadOnlyUser() {
		return !user.isEmployee();
	}

	@Override
	protected int processPermissions() {
		return Permissions.NO_PERMISSIONS;
	}

}
