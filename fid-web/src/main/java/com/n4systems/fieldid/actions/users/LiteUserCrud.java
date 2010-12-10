package com.n4systems.fieldid.actions.users;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.util.UserType;

public class LiteUserCrud extends UserCrud {

	protected LiteUserCrud(UserManager userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}

	private static final long serialVersionUID = 1L;
	
	@Override
	public String doCreate(){
		testRequiredEntities(false);
		user.setUserType(UserType.LITE);
		save();
		return SUCCESS;
	}

	@Override
	protected int processPermissions() {
		return 0;
	}

	@Override
	public boolean isEmployee() {
		return false;
	}

	@Override
	public boolean isLiteUser() {
		return true;
	}
	
	@Override
	public boolean isReadOnlyUser() {
		return false;
	}

}
