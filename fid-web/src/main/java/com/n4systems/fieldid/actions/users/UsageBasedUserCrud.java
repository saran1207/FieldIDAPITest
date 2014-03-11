package com.n4systems.fieldid.actions.users;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.model.user.UserSaver;
import com.n4systems.security.Permissions;
import org.apache.struts2.interceptor.validation.SkipValidation;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemUsers})
public class UsageBasedUserCrud extends UserCrud {
	private static final long serialVersionUID = 1L;

	public UsageBasedUserCrud(UserManager userManager, UserGroupService userGroupService, PersistenceManager persistenceManager) {
		super(userManager, userGroupService, persistenceManager);
	}
	
	@Override
	protected void testRequiredEntities(boolean existing) {
		super.testRequiredEntities(existing);
		if (existing && !user.isUsageBasedUser()) {
			throw new MissingEntityException("another user was loaded for when a person user was expected.");
		}
	}

	@SkipValidation
	public String doUnarchive() {
        testRequiredEntities(true);
        user.activateEntity();
        new UserSaver().update(user);
        addFlashMessageText("message.user_unarchived");
        return SUCCESS;
	}

	@Override
	protected int processPermissions() {
		return Permissions.ALLEVENT;
	}

	@Override
	public boolean isFullUser(){
		return false;
	}
	
	@Override
	public boolean isEmployee() {
		return false;
	}

	@Override
	public boolean isLiteUser() {
		return false;
	}

	@Override
	public boolean isReadOnlyUser() {
		return true;
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
