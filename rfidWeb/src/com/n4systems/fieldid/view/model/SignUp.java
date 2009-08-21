package com.n4systems.fieldid.view.model;

import com.n4systems.fieldid.actions.helpers.TimeZoneSelectionHelper;
import com.n4systems.model.signup.AccountCreationInformation;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.Region;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

public class SignUp implements SignUpPriceModifier, AccountCreationInformation {
	private Long signUpPackageId;
	
	private String companyName;
	private String firstName;
	private String lastName;
	private String email;
	private Country country;
	private Region region;
	
	
	private String username;
	private String password;
	private String passwordConfirm;
	
		
	private String tenantName;
	
	private Integer numberOfUsers = 1;
	private boolean phoneSupport;
	
	public SignUp() {
		this.country = TimeZoneSelectionHelper.defaultCountry();
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	@RequiredStringValidator(message="", key="error.first_name_required")
	@StringLengthFieldValidator(message="", key="error.first_name_length", maxLength="255")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	
	public String getLastName() {
		return lastName;
	}
	
	@RequiredStringValidator(message="", key="error.last_name_required")
	@StringLengthFieldValidator(message="", key="error.last_name_length", maxLength="255")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	
	public String getEmail() {
		return email;
	}
	
	@RequiredFieldValidator(message="", key="error.email")
	@EmailValidator(message="", key="error.email")
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public Country getCountry() {
		return country;
	}

	public String getCountryId() {
		return country.getId();
	}

	public void setCountryId(String countryId) {
		country = TimeZoneSelectionHelper.getCountryById(countryId);
	}
	
	public Region getRegion() {
		return region;
	}
	
	public String getTimeZone() {
		return country.getFullId(region);
	}

	public void setTimeZone(String regionId) {
		if (country != null) {
			region = country.getRegionById(regionId);
		} else {
			region = null;
		}
	}

	public String getUsername() {
		return username;
	}
	
	@RequiredStringValidator(message="", key="error.username")
	@StringLengthFieldValidator(message="", key="error.username_length", maxLength="100")
	public void setUsername(String username) {
		this.username = username;
	}
	
	

	public String getPassword() {
		return password;
	}
	@RequiredStringValidator(message="", key="error.password_required")
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	
	
	public String getCompanyName() {
		return companyName;
	}
	@RequiredStringValidator(message="", key="error.company_name_required")
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getTenantName() {
		return tenantName;
	}
	@RequiredStringValidator(message="", key="error.tenant_name_required")
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public Integer getNumberOfUsers() {
		return numberOfUsers;
	}
	
	@RequiredFieldValidator(message="", key="error.number_of_users_required")
	@IntRangeFieldValidator(message="", key="error.number_of_users_minimum", min="1")
	public void setNumberOfUsers(Integer numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}
	
	public boolean isPhoneSupport() {
		return phoneSupport;
	}
	public void setPhoneSupport(boolean phoneSupport) {
		this.phoneSupport = phoneSupport;
	}
	
	public Long getSignUpPackageId() {
		return signUpPackageId;
	}
	public void setSignUpPackageId(Long signUpPackageId) {
		this.signUpPackageId = signUpPackageId;
	}

	public boolean isNew() {
		return tenantName == null;
	}

	public SignUpPackage getSignUpPackage() {
		return null;
	}

}
