package com.n4systems.fieldid.actions.users;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.users.ChangePasswordCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateRfidValidator;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemUsers})
public class AdminChangeSecurityCard extends AbstractCrud  implements HasDuplicateRfidValidator  {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger( ChangePasswordCrud.class );

	private User user;

	private String securityCardNumber;


	private UserManager userManager;

	public AdminChangeSecurityCard(UserManager userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
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
		if( getSessionUser().hasAccess("managesystemusers") && user != null) {
			try {
				
				user.assignSecruityCardNumber(getSecurityCardNumber());
				userManager.updateUser(user);
			} catch (DuplicateUserException e) {}
				
			logger.info("security card number updated for " + getSessionUser().getUserID());
			addFlashMessageText("message.securityupdated");
		}
		return SUCCESS;
	}

	public User getUser() {
		return user;
	}
	
	@StringLengthFieldValidator(type=ValidatorType.FIELD, message = "" , key = "errors.securitycardlength", minLength="16")
	public void setSecurityCardNumber(String securityCardNumber) {
		if (securityCardNumber == null || securityCardNumber.trim().length() == 0) {
			this.securityCardNumber = null;
		} else {
			this.securityCardNumber = securityCardNumber;
		}
	}
	
	@CustomValidator(type = "duplicateRfidValidator", message = "", key = "errors.data.rfidduplicate")
	public String getSecurityCardNumber() {
		return securityCardNumber;
	}

	@Override
	public boolean validateRfid(String formValue) {
		return !userManager.userRfidIsUnique(getTenantId(), formValue, uniqueID);
	}
	
}
