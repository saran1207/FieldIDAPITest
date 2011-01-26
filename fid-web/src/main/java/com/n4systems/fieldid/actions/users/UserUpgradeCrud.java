package com.n4systems.fieldid.actions.users;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.security.Permissions;
import com.n4systems.util.UserType;

public class UserUpgradeCrud extends UserCrud{

	protected UserUpgradeCrud(UserManager userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}
	
	public String doChangeToFull() {
		if(!isEmployeeLimitReached()) {
			user.setUserType(UserType.FULL);
			save();
			return SUCCESS;
		}else {
			addActionErrorText("label.full_user_limit_reached");
			return ERROR;
		}
	}
	
	public String doChangeToLite() {
		if(!isLiteUserLimitReached()) {
			user.setUserType(UserType.LITE);
			save();
			return SUCCESS;
		}else {
			addActionErrorText("label.lite_user_limit_reached");
			return ERROR;
		}
	}
	
	public String doChangeToReadOnly() {
		user.setUserType(UserType.READONLY);
		save();
		return SUCCESS;
	}

	@Override
	public boolean isEmployee() {
		return user.isEmployee();
	}
	
	public boolean isFullUser() {
		return user.getUserType().equals(UserType.FULL);
	}
	
	public boolean isLiteUser() {
		return user.getUserType().equals(UserType.LITE);
	}
	
	public boolean isReadOnlyUser() {
		return user.getUserType().equals(UserType.READONLY);
	}

	@Override
	protected int processPermissions() {
		return Permissions.NO_PERMISSIONS;
	}

}
