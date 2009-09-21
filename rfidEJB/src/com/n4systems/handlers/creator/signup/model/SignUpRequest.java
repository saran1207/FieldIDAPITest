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
	
	
	private AddressInfo address = new AddressInfo();
	
	private String username;
	private String password;
	private String passwordConfirm;
	
	private String promoCode;
		
	private String tenantName;
	
	private Integer numberOfUsers = 1;
	private boolean purchasingPhoneSupport;
	private String userN4Id;
	private String companyN4Id;
	
	private CreditCard creditCard = new CreditCard();
	private boolean usingCreditCard = true;
	private String purchaseOrderNumber;
	
	private PaymentOption paymentOption = PaymentOption.ONE_YEAR_UP_FRONT;
	
	public SignUpRequest() {
		this.country = TimeZoneSelectionHelper.defaultCountry();
		this.address.setCountry(TimeZoneSelectionHelper.defaultCountry().getCode());
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


	public Long getContractExternalId() {
		return signUpPackage.getContract(paymentOption);
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
		return getBillingAddress();
	}

	public String getUrl() {
		return "";
	}

	public boolean isUsingCreditCard() {
		return usingCreditCard;
	}

	public void setUsingCreditCard(boolean usingCreditCard) {
		this.usingCreditCard = usingCreditCard;
	}


	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}


	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}


	public boolean isPurchasingPhoneSupport() {
		return purchasingPhoneSupport;
	}

	
	public void setPurchasingPhoneSupport(boolean purchasingPhoneSupport) {
		this.purchasingPhoneSupport = purchasingPhoneSupport;
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

	
	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}


	public String getUserN4Id() {
		return userN4Id.toString();
	}


	public void setUserN4Id(Long userN4Id) {
		this.userN4Id = userN4Id.toString();
	}


	public String getCompanyN4Id() {
		return companyN4Id;
	}


	public void setCompanyN4Id(Long companyN4Id) {
		this.companyN4Id = companyN4Id.toString();
	}
}
