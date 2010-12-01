package com.n4systems.fieldid.actions.users;

import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.UserRequest;
import com.n4systems.model.api.Listable;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.AdminUserListLoader;
import com.n4systems.model.user.User;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;
import com.n4systems.util.timezone.TimeZoneSelectionHelper;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.ReadOnlyUser)
public class UserRegistrationCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserRegistrationCrud.class);
	
	private User userAccount;

	private UserRequest userRequest;
	private String password;
	private String passwordConfirmation;
	private UserManager userManager;
	private Country country;
	private Region region;

	public UserRegistrationCrud(UserManager userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@Override
	protected void initMemberFields() {
		userAccount = new User();
		userRequest = new UserRequest();
		String defaultZoneId = getConfigContext().getString(ConfigEntry.DEFAULT_TIMEZONE_ID);
		country = CountryList.getInstance().getCountryByFullName(defaultZoneId);
		region = CountryList.getInstance().getRegionByFullId(defaultZoneId);
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
		userAccount.setOwner(getPrimaryOrg());
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

		TemplateMailMessage message = new TemplateMailMessage();
		message.setSubject("Customer Account Request");
		message.setEmailConent(String.format("A user has requested a customer account. To view the request <a href=\"%s?companyID=%s\">click here</a>.", createActionURI("userRequestList"), getTenant().getName()));
		
		AdminUserListLoader userLoader = new AdminUserListLoader(new TenantOnlySecurityFilter(getTenant()));
		for (User user: userLoader.load()) {
			message.getToAddresses().add(user.getEmailAddress());
		}
		
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

	@RequiredStringValidator(type=ValidatorType.FIELD, message="", key="error.timezonerequired")
	public String getTimeZone() {
		return region.getId();
	}

	public void setTimeZone(String regionId) {
		if (country != null) {
			region = country.getRegionById(regionId);
			userAccount.setTimeZoneID(country.getFullName(region));
		}
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

	public SortedSet<? extends Listable<String>> getCountries() {
		return TimeZoneSelectionHelper.getCountries();
	}

	public SortedSet<? extends Listable<String>> getTimeZones() {
		return TimeZoneSelectionHelper.getTimeZones(country);
	}
	
	public String getCountryId() {
		return country.getId();
	}

	public void setCountryId(String countryId) {
		country = TimeZoneSelectionHelper.getCountryById(countryId);
	}
	
	public boolean duplicateValueExists(String formValue) {
		return !userManager.userIdIsUnique(getTenantId(), formValue);
	}

	public String getCity() {
		return userRequest.getCity();
	}

	public void setCity(String city) {
		userRequest.setCity(city);
	}
	
	

}
