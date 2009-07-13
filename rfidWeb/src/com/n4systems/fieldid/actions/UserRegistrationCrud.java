package com.n4systems.fieldid.actions;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.TenantOrganization;
import com.n4systems.model.UserRequest;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.StringListingPair;
import com.n4systems.util.mail.MailMessage;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class UserRegistrationCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserRegistrationCrud.class);

	private Collection<StringListingPair> timeZones;

	private TenantOrganization tenant;
	private UserBean userAccount;

	private UserRequest userRequest;
	private String password;
	private String passwordConfirmation;

	private User userManager;

	public UserRegistrationCrud(User userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@Override
	protected void initMemberFields() {
		userAccount = new UserBean();
		userRequest = new UserRequest();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		initMemberFields();
	}

	@SkipValidation
	public String doAdd() {

		return SUCCESS;
	}

	public String doSave() {

		userAccount.setActive(false);
		userAccount.setTenant(getTenant());
		userAccount.setOrganization(getTenant());
		userAccount.assignPassword(password);
		userRequest.setTenant(getTenant());
		userRequest.setUserAccount(userAccount);

		try {
			userManager.saveUserRequest(userRequest, userAccount);
		} catch (DuplicateUserException duplicateUser) {
			addFieldError("userId", getText("errors.data.userduplicate"));
			return INPUT;
		} catch (Exception e) {
			addActionError(getText("error.noaccount"));
			return ERROR;
		}

		MailMessage message = new MailMessage();
		message.setSubject("Customer Account Request");
		message.setBody("A user has requested a customer account. To view the request <a href=\"" + createActionURI("userRequestList.action").toString() + "?companyID=" + getTenant().getName()
				+ "\">click here</a>.");
		message.getToAddresses().add(userRequest.getTenant().getAdminEmail());

		try {
			ServiceLocator.getMailManager().sendMessage(message);
			logger.warn(getLogLinePrefix() + " request notification message admin of " + userRequest.getTenant().getName() + " for user " + userRequest.getUserAccount().getUserID());
		} catch (Exception e) {
			logger.warn(getLogLinePrefix() + " request notification message failed to send. ", e);
		}

		return "saved";
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.useridrequired")
	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "errors.useridlength", maxLength = "15")
	@CustomValidator(type = "uniqueValue", message = "", key = "errors.data.userduplicate")
	public String getUserId() {
		return userAccount.getUserID();
	}

	public void setUserId(String userId) {
		userAccount.setUserID(userId);
	}

	public String getFirstName() {
		return userAccount.getFirstName();
	}

	@RequiredStringValidator(message = "", key = "error.firstnameisrequired")
	public void setFirstName(String firstName) {
		userAccount.setFirstName(firstName);
	}

	public String getLastName() {
		return userAccount.getLastName();
	}

	@RequiredStringValidator(message = "", key = "error.lastnameisrequired")
	public void setLastName(String lastName) {
		userAccount.setLastName(lastName);
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.emailrequired")
	@EmailValidator(type = ValidatorType.FIELD, message = "", key = "error.emailformat")
	public String getEmailAddress() {
		return userAccount.getEmailAddress();
	}

	public void setEmailAddress(String emailAddress) {
		userAccount.setEmailAddress(emailAddress);
	}

	@RequiredStringValidator(type=ValidatorType.FIELD, message="", key="error.passwordrequired")
	@StringLengthFieldValidator(type=ValidatorType.FIELD, message="", key="errors.passwordlength", minLength="5")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@FieldExpressionValidator(expression = "passwordConfirmation == password", message = "", key = "error.passwordsmustmatch")
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public String getPosition() {
		return userAccount.getPosition();
	}

	public void setPosition(String position) {
		userAccount.setPosition(position);
	}

	public String getTimeZone() {
		return userAccount.getTimeZoneID();
	}

	public void setTimeZone(String timeZone) {
		userAccount.setTimeZoneID(timeZone);

	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.companynamerequired")
	public String getCompanyName() {
		return userRequest.getCompanyName();
	}

	public void setCompanyName(String companyName) {
		userRequest.setCompanyName(companyName);
	}

	public String getPhoneNumber() {
		return userRequest.getPhoneNumber();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.phonenumberrequired")
	public void setPhoneNumber(String phoneNumber) {
		userRequest.setPhoneNumber(phoneNumber);
	}

	public String getComment() {
		return userRequest.getComment();
	}

	public void setComment(String comment) {
		userRequest.setComment(comment);
	}

	public Collection<StringListingPair> getTimeZones() {
		if (timeZones == null) {
			timeZones = DateHelper.getTimeZones();
		}
		return timeZones;
	}

	public boolean duplicateValueExists(String formValue) {
		getTenant();
		if (tenant != null) {
			return !userManager.userIdIsUnique(tenant.getId(), formValue);
		}
		return false;
	}

}
