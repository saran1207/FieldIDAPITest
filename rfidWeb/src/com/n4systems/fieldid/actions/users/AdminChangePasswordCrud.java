package com.n4systems.fieldid.actions.users;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;

public class AdminChangePasswordCrud extends ChangePasswordCrud {
	private static final long serialVersionUID = 1L;
	
	
	public AdminChangePasswordCrud(User userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}
	
	
	@Override
	protected void initMemberFields() {

	}

	@Override
	protected void loadMemberFields( Long uniqueId ) {
		user = userManager.findUser(uniqueId, getTenantId());

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

	public UserBean getUser() {
		return user;
	}
}
