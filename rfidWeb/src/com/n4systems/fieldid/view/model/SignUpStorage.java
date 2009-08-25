package com.n4systems.fieldid.view.model;

import com.n4systems.fieldid.actions.helpers.TimeZoneSelectionHelper;
import com.n4systems.model.signup.AccountCreationInformation;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.Region;

public class SignUpStorage implements SignUpPriceModifier, AccountCreationInformation {
	private static final long serialVersionUID = 1L;
	
	private Long signUpPackageId;
	
	private String companyName;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private Country country;
	private Region region;
	
	
	private String username;
	private String password;
	private String passwordConfirm;
	
		
	private String tenantName;
	
	private Integer numberOfUsers = 1;
	private boolean phoneSupport;
	
	public SignUpStorage() {
		this.country = TimeZoneSelectionHelper.defaultCountry();
	}

	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
		return region.getId();
	}

	public void setTimeZone(String regionId) {
		if (country != null) {
			region = country.getRegionById(regionId);
		} else {
			region = null;
		}
	}

	public String getFullTimeZone() {
		return country.getFullId(region);
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	

	public String getPassword() {
		return password;
	}

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
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	
	public Integer getNumberOfUsers() {
		return numberOfUsers;
	}
	
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
		return SignUpPackage.Basic;
	}
}
