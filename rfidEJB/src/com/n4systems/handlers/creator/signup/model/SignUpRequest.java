package com.n4systems.handlers.creator.signup.model;

import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.subscription.AddressInfo;
import com.n4systems.subscription.Company;
import com.n4systems.subscription.CreditCard;
import com.n4systems.subscription.PaymentFrequency;
import com.n4systems.subscription.PaymentOption;
import com.n4systems.subscription.Person;
import com.n4systems.subscription.Subscription;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.Region;
import com.n4systems.util.timezone.TimeZoneSelectionHelper;

public class SignUpRequest implements Subscription, AccountCreationInformation, Company, Person {
	private static final long serialVersionUID = 1L;
	
	private SignUpPackage signUpPackage;
	
	private String companyName;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private Country country;
	private Region region;
	
	
	private AddressInfo address;
	
	private String username;
	private String password;
	private String passwordConfirm;
	
		
	private String tenantName;
	
	private Integer numberOfUsers = 1;
	private boolean purchasingPhoneSupport;
	
	private CreditCard creditCard = new CreditCard();
	
	private PaymentOption paymentOption = PaymentOption.ONE_YEAR_UP_FRONT;
	
	public SignUpRequest() {
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
	
	public boolean isNew() {
		return tenantName == null;
	}

	public SignUpPackage getSignUpPackage() {
		return signUpPackage;
	}


	public Long getContractId() {
		return 1L;
	}


	public PaymentFrequency getFrequency() {
		return paymentOption.getFrequency();
	}


	public Integer getMonths() {
		return paymentOption.getTerm();
	}


	public Integer getUsers() {
		return getNumberOfUsers();
	}

	public AddressInfo getBillingAddress() {
		return address;
	}

	public void setBillingAddress(AddressInfo address) {
		this.address = address;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}



	public String getPhone() {
		return getPhoneNumber();
	}


	public AddressInfo getShippingAddress() {
		return null;
	}


	public String getUrl() {
		
		return null;
	}


	public boolean isUsingCreditCard() {
		return true;
	}


	public boolean isPurchasingPhoneSupport() {
		return purchasingPhoneSupport;
	}


	public void setPurchasingPhoneSupport(boolean purchasingPhoneSupport) {
		this.purchasingPhoneSupport = purchasingPhoneSupport;
	}


	public String getPromoCode() {
		return null;
	}


	public String getN4Id() {
		return null;
	}


	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}


	public void setSignUpPackage(SignUpPackage signUpPackage) {
		this.signUpPackage = signUpPackage;
	}


	public String getPaymentOption() {
		return paymentOption.name();
	}


	public void setPaymentOption(String paymentOption) {
		this.paymentOption = PaymentOption.valueOf(paymentOption);
	}
}
