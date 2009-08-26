package com.n4systems.fieldid.view.model;

import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.handlers.creator.AccountCreationInformation;
import com.n4systems.handlers.creator.SignUpRequest;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.tenant.TenantUniqueAvailableNameLoader;
import com.n4systems.subscription.AddressInfo;
import com.n4systems.subscription.Person;
import com.n4systems.subscription.Company;
import com.n4systems.subscription.PaymentFrequency;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.netsuite.model.CreditCard;
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

public class SignUp implements Subscription, AccountCreationInformation, HasDuplicateValueValidator, Company, Person {
	private final TenantUniqueAvailableNameLoader uniqueNameLoader;
	
	private final SignUpRequest signUpRequest;
	

	public SignUp(SignUpRequest signUpRequest, TenantUniqueAvailableNameLoader uniqueNameAvailableLoader) {
		this.signUpRequest = signUpRequest;
		this.uniqueNameLoader = uniqueNameAvailableLoader;
	}
	
	public SignUp(TenantUniqueAvailableNameLoader uniqueNameAvailableLoader) {
		this(new SignUpRequest(), uniqueNameAvailableLoader);
	}
	
	public boolean duplicateValueExists(String formValue) {
		return !uniqueNameLoader.setUniqueName(formValue).load();
	}

	public String getCompanyName() {
		return signUpRequest.getCompanyName();
	}

	public Country getCountry() {
		return signUpRequest.getCountry();
	}

	public String getCountryId() {
		return signUpRequest.getCountryId();
	}

	public String getEmail() {
		return signUpRequest.getEmail();
	}

	
	public String getFirstName() {
		return signUpRequest.getFirstName();
	}

	public String getFullTimeZone() {
		return signUpRequest.getFullTimeZone();
	}

	public String getLastName() {
		return signUpRequest.getLastName();
	}

	public Integer getNumberOfUsers() {
		return signUpRequest.getNumberOfUsers();
	}

	public String getPassword() {
		return signUpRequest.getPassword();
	}

	public String getPasswordConfirm() {
		return signUpRequest.getPasswordConfirm();
	}

	public String getPhoneNumber() {
		return signUpRequest.getPhoneNumber();
	}

	public Region getRegion() {
		return signUpRequest.getRegion();
	}

	public SignUpPackage getSignUpPackage() {
		return signUpRequest.getSignUpPackage();
	}

	public Long getSignUpPackageId() {
		return signUpRequest.getSignUpPackageId();
	}

	public SignUpRequest getSignUpRequest() {
		return signUpRequest;
	}

	public String getTenantName() {
		return signUpRequest.getTenantName();
	}

	public String getTimeZone() {
		return signUpRequest.getTimeZone();
	}

	public String getUsername() {
		return signUpRequest.getUsername();
	}

	public boolean isNew() {
		return signUpRequest.isNew();
	}

	public boolean isPhoneSupport() {
		return signUpRequest.isPhoneSupport();
	}

	@RequiredStringValidator(message="", key="error.company_name_required")
	public void setCompanyName(String companyName) {
		signUpRequest.setCompanyName(companyName);
	}

	public void setCountryId(String countryId) {
		signUpRequest.setCountryId(countryId);
	}

	@RequiredStringValidator(message="", key="error.email")
	@EmailValidator(message="", key="error.email")
	public void setEmail(String email) {
		signUpRequest.setEmail(email);
	}

	@RequiredStringValidator(message="", key="error.first_name_required")
	@StringLengthFieldValidator(message="", key="error.first_name_length", maxLength="255")
	public void setFirstName(String firstName) {
		signUpRequest.setFirstName(firstName);
	}

	@RequiredStringValidator(message="", key="error.last_name_required")
	@StringLengthFieldValidator(message="", key="error.last_name_length", maxLength="255")
	public void setLastName(String lastName) {
		signUpRequest.setLastName(lastName);
	}

	@RequiredFieldValidator(message="", key="error.number_of_users_required")
	@IntRangeFieldValidator(message="", key="error.number_of_users_minimum", min="1")
	public void setNumberOfUsers(Integer numberOfUsers) {
		signUpRequest.setNumberOfUsers(numberOfUsers);
	}

	@RequiredStringValidator(type=ValidatorType.FIELD, message="", key="error.passwordrequired")
	@StringLengthFieldValidator(type=ValidatorType.FIELD, message="", key="errors.passwordlength", minLength="5") 
	public void setPassword(String password) {
		signUpRequest.setPassword(password);
	}
	
	@FieldExpressionValidator(expression="passwordConfirm == password", message="", key="error.passwordsmustmatch")
	public void setPasswordConfirm(String passwordConfirm) {
		signUpRequest.setPasswordConfirm(passwordConfirm);
	}

	@RequiredStringValidator(message="", key="error.phone_number_required")
	public void setPhoneNumber(String phoneNumber) {
		signUpRequest.setPhoneNumber(phoneNumber);
	}

	public void setPhoneSupport(boolean phoneSupport) {
		signUpRequest.setPhoneSupport(phoneSupport);
	}

	public void setSignUpPackageId(Long signUpPackageId) {
		signUpRequest.setSignUpPackageId(signUpPackageId);
	}

	@RequiredStringValidator(message="", key="error.tenant_name_required")
	@StringLengthFieldValidator(message="", key="error.tenant_name_length", minLength="3", maxLength="255")
	@RegexFieldValidator(expression="^[\\w][\\w\\-]*[\\w]$", message = "", key="error.tenant_name_format")
	@CustomValidator(type="uniqueValue", message = "", key="error.name_already_used")
	public void setTenantName(String tenantName) {
		signUpRequest.setTenantName(tenantName);
	}

	
	@RequiredStringValidator(message="", key="error.time_zone_name_required")
	public void setTimeZone(String regionId) {
		signUpRequest.setTimeZone(regionId);
	}

	@RequiredStringValidator(message="", key="error.username_required")
	@FieldExpressionValidator(expression="(username != restrictedUsername)", message="", key="error.reserved_username")
	@StringLengthFieldValidator(message = "" , key="errors.useridlength", maxLength="15")
	public void setUsername(String username) {
		signUpRequest.setUsername(username);
		ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME);
	}
	
	public String getRestrictedUsername() {
		return ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME);
	}

	public AddressInfo getBillingAddress() {
		return signUpRequest.getBillingAddress();
	}

	public CreditCard getCreditCard() {
		return signUpRequest.getCreditCard();
	}

	public Long getExternalId() {
		return signUpRequest.getExternalId();
	}

	public String getN4Id() {
		return signUpRequest.getN4Id();
	}

	public PaymentFrequency getFrequency() {
		return signUpRequest.getFrequency();
	}

	public int getMonths() {
		return signUpRequest.getMonths();
	}

	public String getPhone() {
		return signUpRequest.getPhone();
	}

	public String getPromoCode() {
		return signUpRequest.getPromoCode();
	}

	public AddressInfo getShippingAddress() {
		return signUpRequest.getShippingAddress();
	}

	public String getUrl() {
		return signUpRequest.getUrl();
	}

	public int getUsers() {
		return signUpRequest.getUsers();
	}

	public boolean isPurchasingPhoneSupport() {
		return signUpRequest.isPurchasingPhoneSupport();
	}

	public boolean isUsingCreditCard() {
		return signUpRequest.isUsingCreditCard();
	}




}
