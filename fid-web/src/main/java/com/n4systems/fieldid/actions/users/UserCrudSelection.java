package com.n4systems.fieldid.actions.users;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.service.user.UserGroupService;

public class UserCrudSelection extends UserCrud {
	protected UserCrudSelection(UserManager userManager, UserGroupService userGroupService, PersistenceManager persistenceManager) {
		super(userManager, userGroupService, persistenceManager);
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected int processPermissions() {
		return 0;
	}
	
	public String doShow(){
		return SUCCESS;
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

	@Override
	public boolean isFullUser() {
		return false;
	}

    @Override
    public boolean isPerson() {
        return false;
    }

    @Override
    public boolean isUsageBasedUser() {
        return false;
    }
}
