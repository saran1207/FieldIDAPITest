package com.n4systems.fieldid.view.model;

import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.signup.AccountCreationInformation;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.tenant.TenantUniqueAvailableNameLoader;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.Region;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class SignUp implements SignUpPriceModifier, AccountCreationInformation, HasDuplicateValueValidator {
	private final TenantUniqueAvailableNameLoader uniqueNameLoader;
	
	private final SignUpStorage signUpStorage;
	

	public SignUp(SignUpStorage signUp, TenantUniqueAvailableNameLoader uniqueNameAvailableLoader) {
		this.signUpStorage = signUp;
		this.uniqueNameLoader = uniqueNameAvailableLoader;
	}
	
	public SignUp(TenantUniqueAvailableNameLoader uniqueNameAvailableLoader) {
		this(new SignUpStorage(), uniqueNameAvailableLoader);
	}
	
	public boolean duplicateValueExists(String formValue) {
		return uniqueNameLoader.setUniqueName(formValue).load();
	}

	public String getCompanyName() {
		return signUpStorage.getCompanyName();
	}

	public Country getCountry() {
		return signUpStorage.getCountry();
	}

	public String getCountryId() {
		return signUpStorage.getCountryId();
	}

	public String getEmail() {
		return signUpStorage.getEmail();
	}

	
	public String getFirstName() {
		return signUpStorage.getFirstName();
	}

	public String getFullTimeZone() {
		return signUpStorage.getFullTimeZone();
	}

	public String getLastName() {
		return signUpStorage.getLastName();
	}

	public Integer getNumberOfUsers() {
		return signUpStorage.getNumberOfUsers();
	}

	public String getPassword() {
		return signUpStorage.getPassword();
	}

	public String getPasswordConfirm() {
		return signUpStorage.getPasswordConfirm();
	}

	public String getPhoneNumber() {
		return signUpStorage.getPhoneNumber();
	}

	public Region getRegion() {
		return signUpStorage.getRegion();
	}

	public SignUpPackage getSignUpPackage() {
		return signUpStorage.getSignUpPackage();
	}

	public Long getSignUpPackageId() {
		return signUpStorage.getSignUpPackageId();
	}

	public SignUpStorage getSignUpStorage() {
		return signUpStorage;
	}

	public String getTenantName() {
		return signUpStorage.getTenantName();
	}

	public String getTimeZone() {
		return signUpStorage.getTimeZone();
	}

	public String getUsername() {
		return signUpStorage.getUsername();
	}

	public boolean isNew() {
		return signUpStorage.isNew();
	}

	public boolean isPhoneSupport() {
		return signUpStorage.isPhoneSupport();
	}

	@RequiredStringValidator(message="", key="error.company_name_required")
	public void setCompanyName(String companyName) {
		signUpStorage.setCompanyName(companyName);
	}

	public void setCountryId(String countryId) {
		signUpStorage.setCountryId(countryId);
	}

	@RequiredStringValidator(message="", key="error.email")
	@EmailValidator(message="", key="error.email")
	public void setEmail(String email) {
		signUpStorage.setEmail(email);
	}

	@RequiredStringValidator(message="", key="error.first_name_required")
	@StringLengthFieldValidator(message="", key="error.first_name_length", maxLength="255")
	public void setFirstName(String firstName) {
		signUpStorage.setFirstName(firstName);
	}

	@RequiredStringValidator(message="", key="error.last_name_required")
	@StringLengthFieldValidator(message="", key="error.last_name_length", maxLength="255")
	public void setLastName(String lastName) {
		signUpStorage.setLastName(lastName);
	}

	@RequiredFieldValidator(message="", key="error.number_of_users_required")
	@IntRangeFieldValidator(message="", key="error.number_of_users_minimum", min="1")
	public void setNumberOfUsers(Integer numberOfUsers) {
		signUpStorage.setNumberOfUsers(numberOfUsers);
	}

	@RequiredStringValidator(type=ValidatorType.FIELD, message="", key="error.passwordrequired")
	@StringLengthFieldValidator(type=ValidatorType.FIELD, message="", key="errors.passwordlength", minLength="5") 
	public void setPassword(String password) {
		signUpStorage.setPassword(password);
	}
	
	@FieldExpressionValidator(expression="passwordConfirmation == password", message="", key="error.passwordsmustmatch")
	public void setPasswordConfirm(String passwordConfirm) {
		signUpStorage.setPasswordConfirm(passwordConfirm);
	}

	@RequiredStringValidator(message="", key="error.phone_number_required")
	public void setPhoneNumber(String phoneNumber) {
		signUpStorage.setPhoneNumber(phoneNumber);
	}

	public void setPhoneSupport(boolean phoneSupport) {
		signUpStorage.setPhoneSupport(phoneSupport);
	}

	public void setSignUpPackageId(Long signUpPackageId) {
		signUpStorage.setSignUpPackageId(signUpPackageId);
	}

	@RequiredStringValidator(message="", key="error.tenant_name_required")
	@StringLengthFieldValidator(message="", key="error.tenant_name_length", minLength="3", maxLength="255")
	@RegexFieldValidator(expression="^[a-zA-Z][a-zA-Z\\-]*[a-zA-Z]$", message = "", key="error.tenant_name_format")
	@CustomValidator(type="uniqueValue", message = "", key="error.name_already_used")
	public void setTenantName(String tenantName) {
		signUpStorage.setTenantName(tenantName);
	}

	
	@RequiredStringValidator(message="", key="error.time_zone_name_required")
	public void setTimeZone(String regionId) {
		signUpStorage.setTimeZone(regionId);
	}

	@RequiredStringValidator(message="", key="error.username_required")
	@FieldExpressionValidator(expression="(username != restrictedUsername)", message="", key="error.reserved_username")
	@StringLengthFieldValidator(message = "" , key="errors.useridlength", maxLength="15")
	public void setUsername(String username) {
		signUpStorage.setUsername(username);
		ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME);
	}
	
	public String getRestrictedUsername() {
		return ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME);
	}


}
