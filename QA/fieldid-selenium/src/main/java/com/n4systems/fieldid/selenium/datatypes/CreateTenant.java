package com.n4systems.fieldid.selenium.datatypes;

public class CreateTenant {
	// About You
	String firstName;
	String lastName;
	String email;
	String country;
	String timeZone;
	String userName;
	String password;
	String password2;
	
	// About Your Company
	String companyName;
	String companyAddress;
	String companyCity;
	String companyState;
	String companyCountry;
	String companyZipCode;
	String companyPhoneNumber;
	
	// Site Address
	String siteAddress;
	
	// Promo Code
	String promoCode;
	
	// All class variables below this point do not apply to Free accounts
	
	// negative users is a flag for Free account
	public final static int numUsersFreeAccountFlag = -1;

	// Users and Options (defaults to Free account)
	int numberOfUsers = numUsersFreeAccountFlag;
	boolean phoneSupport = false;
	
	// Payment Options
	public final static String paymentOptionsOneYear = "One Year";
	public final static String paymentOptionsTwoYear = "Two Year";
	String paymentOptions = null;
	
	// types of credit card we support
	public final static String creditCardTypeVISA = "Visa";
	public final static String creditCardTypeMC = "MasterCard";
	public final static String creditCardTypeAMEX = "AMEX";
	
	
	// types of payment types
	public final static String PAY_BY_CREDIT_CARD = "Credit Card";
	public final static String payByPurchaseOrder = "Purchase Order";
	public final static String PAY_BY_FREE_ACCOUNT = "Free Account";
	
	String paymentType = PAY_BY_FREE_ACCOUNT;
	String cardType;
	String nameOnCard;
	String cardNumber;
	String expiryMonth;
	String expiryYear;
	String purchaseOrderNumber;
	
	public CreateTenant(String username, String password, String tenantName, String tenantID) {
		
		this.setFirstName("Darrell");
		this.setLastName("Grainger");
		this.setEmail("selenium@fieldid.com");
		this.setCountry("Canada");
		this.setTimeZone("Ontario - Toronto");
		this.setCompanyAddress("179 John Street");
		this.setCompanyCity("Toronto");
		this.setCompanyState("Ontario");
		this.setCompanyCountry("Canada");
		this.setCompanyZipCode("M5T 1X4");
		this.setCompanyPhoneNumber("(416) 599-6464");
		this.setNumberOfUsers(1);
		this.setPhoneSupport(false);
		this.setUserName(username);
		this.setPassword(password);
		this.setPassword2(password);
		this.setCompanyName(tenantName);
		this.setSiteAddress(tenantID);
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		s.append("\n           First Name: " + firstName);
		s.append("\n            Last Name: " + lastName);
		s.append("\n        Email Address: " + email);
		s.append("\n              Country: " + country);
		s.append("\n            Time Zone: " + timeZone);
		s.append("\n            User Name: " + userName);
		s.append("\n             Password: " + password);
		s.append("\n            Password2: " + password2);
		s.append("\n         Company Name: " + companyName);
		s.append("\n      Company Address: " + companyAddress);
		s.append("\n         Company City: " + companyCity);
		s.append("\n        Company State: " + companyState);
		s.append("\n      Company Country: " + companyCountry);
		s.append("\n     Company Zip Code: " + companyZipCode);
		s.append("\n Company Phone Number: " + companyPhoneNumber);
		s.append("\n         Site Address: " + siteAddress);
		s.append("\n           Promo Code: " + promoCode);
		s.append("\n      Number Of Users: " + numberOfUsers);
		s.append("\n        Phone Support: " + phoneSupport);
		s.append("\n      Payment Options: " + paymentOptions);
		s.append("\n         Payment Type: " + paymentType);
		s.append("\n            Card Type: " + cardType);
		s.append("\n         Name On Card: " + nameOnCard);
		s.append("\n          Card Number: " + cardNumber);
		s.append("\n         Expiry Month: " + expiryMonth);
		s.append("\n          Expiry Year: " + expiryYear);
		s.append("\nPurchase Order Number: " + purchaseOrderNumber);
		
		return s.toString();
	}

	public void setFirstName(String s) {
		this.firstName = s;
	}
	
	public void setLastName(String s) {
		this.lastName = s;
	}
	
	public void setEmail(String s) {
		this.email = s;
	}
	
	public void setCountry(String s) {
		this.country = s;
	}
	
	public void setTimeZone(String s) {
		this.timeZone = s;
	}
	
	public void setUserName(String s) {
		this.userName = s;
	}
	
	public void setPassword(String s) {
		this.password = s;
	}
	
	public void setPassword2(String s) {
		this.password2 = s;
	}
	
	public void setCompanyName(String s) {
		this.companyName = s;
	}
	
	public void setCompanyAddress(String s) {
		this.companyAddress = s;
	}
	
	public void setCompanyCity(String s) {
		this.companyCity = s;
	}
	
	public void setCompanyState(String companyState) {
		this.companyState = companyState;
	}
	
	public void setCompanyCountry(String s) {
		this.companyCountry = s;
	}
	
	public void setCompanyZipCode(String s) {
		this.companyZipCode = s;
	}
	
	public void setCompanyPhoneNumber(String s) {
		this.companyPhoneNumber = s;
	}
	
	public void setSiteAddress(String s) {
		this.siteAddress = s;
	}
	
	public void setNumberOfUsers(int i) {
		this.numberOfUsers = i;
	}
	
	public void setPhoneSupport(boolean b) {
		this.phoneSupport = b;
	}
	
	public void setPromoCode(String s) {
		this.promoCode = s;
	}
	
	public void setPaymentOptions(String s) {
		this.paymentOptions = s;
	}
		
	public void setPaymentType(String s) {
		this.paymentType = s;
	}
	
	public void setCreditCardType(String s) {
		this.cardType = s;
	}
	
	public void setNameOnCard(String s) {
		this.nameOnCard = s;
	}
	
	public void setCardNumber(String s) {
		this.cardNumber = s;
	}
	
	public void setexpiryMonth(String s) {
		this.expiryMonth = s;
	}
	
	public void setexpiryYear(String s) {
		this.expiryYear = s;
	}
	
	public void setpurchaseOrderNumber(String s) {
		this.purchaseOrderNumber = s;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getTimeZone() {
		return timeZone;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getPassword2() {
		return password2;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public String getCompanyAddress() {
		return companyAddress;
	}
	
	public String getCompanyCity() {
		return companyCity;
	}
	
	public String getCompanyState() {
		return companyState;
	}
	
	public String getCompanyCountry() {
		return companyCountry;
	}
	
	public String getCompanyZipCode() {
		return companyZipCode;
	}
	
	public String getCompanyPhoneNumber() {
		return companyPhoneNumber;
	}
	
	public String getSiteAddress() {
		return siteAddress;
	}
	
	public int getNumberOfUsers() {
		return numberOfUsers;
	}
	
	public boolean getPhoneSupport() {
		return phoneSupport;
	}
	
	public String getPromoCode() {
		return promoCode;
	}
	
	public String getPaymentOptions() {
		return paymentOptions;
	}
		
	public String getPaymentType() {
		return this.paymentType;
	}
	
	public String getCreditCardType() {
		return cardType;
	}
	
	public String getNameOnCard() {
		return nameOnCard;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	
	public String getExpiryMonth() {
		return expiryMonth;
	}
	
	public String getExpiryYear() {
		return expiryYear;
	}
	
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}
}
