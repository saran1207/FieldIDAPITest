package com.n4systems.fieldid.actions.users;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;

public class UserCrudSelection extends UserCrud {
	protected UserCrudSelection(UserManager userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected int processPermissions() {
		return 0;
	}
	
	public String doShow(){
		return SUCCESS;
	}
	
	public boolean isEmployeeLimitReached(){
		return getLimits().isEmployeeUsersMaxed();
	}
	
	public boolean isLiteUserLimitReached(){
		boolean is = getLimits().isLiteUsersMaxed();
		return getLimits().isLiteUsersMaxed();
	}

	@Override
	public boolean isLiteUser() {
		return false;
	}

	@Override
	public boolean isReadOnlyUser() {
		return false;
	}

	@Override
	public boolean isEmployee() {
		return false;
	}

}
