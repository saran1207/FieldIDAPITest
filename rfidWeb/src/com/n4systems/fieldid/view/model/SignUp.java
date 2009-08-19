package com.n4systems.fieldid.view.model;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

public class SignUp {
	private Long signUpPackageId;
	
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private String password;
	private String passwordConfirm;
	private String companyName;
	private String tenantName;
	private int numberOfUsers = 1;
	private boolean phoneSupport;
	private String refCode;
	
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
	// unique name validator.
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public int getNumberOfUsers() {
		return numberOfUsers;
	}
	
	@RequiredFieldValidator(message="", key="error.number_of_users_required")
	@IntRangeFieldValidator(message="", key="error.number_of_users_minimum", min="1")
	public void setNumberOfUsers(int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}
	
	public boolean isPhoneSupport() {
		return phoneSupport;
	}
	public void setPhoneSupport(boolean phoneSupport) {
		this.phoneSupport = phoneSupport;
	}
	
	public String getRefCode() {
		return refCode;
	}
	public void setRefCode(String refCode) {
		this.refCode = refCode;
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
	
}
