package com.n4systems.handlers.creator.signup;

import java.util.ArrayList;

import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.subscription.AddressInfo;
import com.n4systems.util.timezone.Region;

public class AccountCreationInformationStub implements AccountCreationInformation {

	private String companyName;
	private String firstName;
	private String lastName;
	private String email;
	private Region region;
	
	private String username;
	private String password;
	
	private String tenantName;
	
	private String promoCode;
	
	private Integer numberOfUsers = 1;
	
	private SignUpPackage signUpPackage;
	private String fullTimeZone;
	
	private AddressInfo billingAddress = new AddressInfo();
	
	private String phone;
	
	public AccountCreationInformationStub() {
	}
	
	public AccountCreationInformationStub(SignUpPackageDetails signUpPackage) {
		this.signUpPackage = new SignUpPackage(signUpPackage, new ArrayList<ContractPricing>());
	}
	
	
	public String getCompanyName() {
		return companyName;
	}

	public AccountCreationInformationStub setCompanyName(String companyName) {
		this.companyName = companyName;
		return this;
	}
	
	public SignUpPackage getSignUpPackage() {
		return signUpPackage;
	}

	public AccountCreationInformationStub setSignUpPackage(SignUpPackage signUpPackage) {
		this.signUpPackage = signUpPackage;
		return this;
	}

	public String getFullTimeZone() {
		return fullTimeZone;
	}

	public AccountCreationInformationStub setFullTimeZone(String fullTimeZone) {
		this.fullTimeZone = fullTimeZone;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public AccountCreationInformationStub setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public AccountCreationInformationStub setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public AccountCreationInformationStub setEmail(String email) {
		this.email = email;
		return this;
	}

	public Region getRegion() {
		return region;
	}

	public AccountCreationInformationStub setRegion(Region region) {
		this.region = region;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public AccountCreationInformationStub setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public AccountCreationInformationStub setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getTenantName() {
		return tenantName;
	}

	public AccountCreationInformationStub setTenantName(String tenantName) {
		this.tenantName = tenantName;
		return this;
	}

	public Integer getNumberOfUsers() {
		return numberOfUsers;
	}

	public AccountCreationInformationStub setNumberOfUsers(Integer numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
		return this;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public AddressInfo getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(AddressInfo billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
