package com.n4systems.fieldid.actions.users;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.fieldid.actions.user.ChangeSecurityCard;
import com.n4systems.fieldid.actions.users.ChangePasswordCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemUsers})
public class AdminChangeSecurityCard extends ChangeSecurityCard {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger( ChangePasswordCrud.class );
	
	public AdminChangeSecurityCard(UserManager userManager, PersistenceManager persistenceManager) {
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
		if( getSessionUser().hasAccess("managesystemusers") ) {
			try {
				updateSecurityCard();
			} catch (DuplicateUserException e) {}
				
			logger.info("security card number updated for " + getSessionUser().getUserID());
			addFlashMessageText("message.securityupdated");
		}
		return SUCCESS;
	}

	public User getUser() {
		return user;
	}
}
