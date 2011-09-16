package com.n4systems.fieldid.actions.users;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.actions.user.ChangeMobilePasscode;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateRfidValidator;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemUsers})
public class AdminChangeMobilePasscode extends ChangeMobilePasscode  implements HasDuplicateRfidValidator  {
	
	public AdminChangeMobilePasscode(UserManager userManager, PersistenceManager persistenceManager) {
		super(userManager, persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields( Long uniqueId ) {
		user = persistenceManager.find(User.class, uniqueId, getTenantId());
	}
	
	@Override
	public String doShow() {
		if(isMobilePasscodeSet()) {
			return SUCCESS;
		}else {
			return INPUT;
		}
	} 
	
	@SkipValidation
	public String doEdit() {
		if (getSessionUser().hasAccess("managesystemusers")) {
			return SUCCESS;
		}
		addActionErrorText("error.not_an_administrator");
		return ERROR;
	}
	
	@Override
	public String doUpdate() {
		if( getSessionUser().hasAccess("managesystemusers") && user != null) {
				return super.doUpdate();
		}
		return SUCCESS;
	}
	
	public User getUser() {
		return user;
	}
	
}
