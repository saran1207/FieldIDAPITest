package com.n4systems.fieldid.actions.user;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.orgs.BaseOrg;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class MyAccount extends AbstractCrud implements HasDuplicateValueValidator {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MyAccount.class);

	private User userManager;
	private UserBean currentUser;

	public MyAccount(User userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@Override
	protected void initMemberFields() {
		currentUser = userManager.getUser(getSessionUser().getId());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		initMemberFields();
	}

	@SkipValidation
	public String doEdit() {
		return SUCCESS;
	}

	public String doCreate() {
		try {
			userManager.updateUser(currentUser);
			refreshSessionUser();
		} catch (Exception e) {
			addActionErrorText("error.updating_account");
			logger.error("failed to save user ", e);
			return ERROR;
		}

		return SUCCESS;
	}

	public String getEmailAddress() {
		return currentUser.getEmailAddress();
	}

	public String getFirstName() {
		return currentUser.getFirstName();
	}

	public String getInitials() {
		return currentUser.getInitials();
	}

	public String getLastName() {
		return currentUser.getLastName();
	}

	public String getPosition() {
		return currentUser.getPosition();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.emailrequired")
	@EmailValidator(type = ValidatorType.FIELD, message = "", key = "error.emailformat")
	public void setEmailAddress(String emailAddress) {
		currentUser.setEmailAddress(emailAddress);
	}

	@RequiredStringValidator(message = "", key = "error.firstnameisrequired")
	public void setFirstName(String firstName) {
		currentUser.setFirstName(firstName);
	}

	public void setInitials(String initials) {
		currentUser.setInitials(initials);
	}

	@RequiredStringValidator(message = "", key = "error.lastnameisrequired")
	public void setLastName(String lastName) {
		currentUser.setLastName(lastName);
	}

	public void setPosition(String position) {
		currentUser.setPosition(position);
	}

	public String getUserID() {
		return currentUser.getUserID();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.useridrequired")
	@StringLengthFieldValidator( type=ValidatorType.FIELD, message = "" , key = "errors.useridlength", maxLength="15")
	@CustomValidator(type="uniqueValue", message = "", key="errors.data.userduplicate")
	public void setUserID(String userID) {
		currentUser.setUserID(userID);
	}

	public boolean duplicateValueExists(String formValue) {
		return !userManager.userIdIsUnique(getTenantId(), formValue, currentUser.getId());
	}
	
	public BaseOrg getOwner() {
		return currentUser.getOwner();
	}

}
