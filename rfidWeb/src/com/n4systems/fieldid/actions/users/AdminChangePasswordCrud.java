package com.n4systems.fieldid.actions.users;

import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemUsers})
public class AdminChangePasswordCrud extends ChangePasswordCrud {
	private static final long serialVersionUID = 1L;
	
	
	public AdminChangePasswordCrud(UserManager userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}
	
	
	@Override
	protected void initMemberFields() {

	}

	@Override
	protected void loadMemberFields( Long uniqueId ) {
		user = persistenceManager.find(User.class, uniqueId, getTenantId());

	}

	

	@SkipValidation
	public String doEdit() {
		if (getSessionUser().hasAccess("managesystemusers")) {
			return SUCCESS;
		}
		addActionErrorText("error.not_an_administrator");
		return ERROR;
	}
	
	
	public String doUpdate() {
		if (getSessionUser().hasAccess("managesystemusers")) {
			updatePassword();
			addFlashMessageText( "message.users_password_updated" );
			return SUCCESS;
		}
		addActionErrorText("error.not_an_administrator");
		return ERROR;
	}

	public User getUser() {
		return user;
	}
}
