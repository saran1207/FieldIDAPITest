package com.n4systems.handlers.creator.signup.model;

import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.subscription.AddressInfo;


public interface AccountCreationInformation {

	public String getFirstName();
	
	public String getLastName();
	
	public String getEmail();
	
	public String getFullTimeZone();

	public String getUsername() ;

	public String getPassword();
	

	public String getCompanyName();
	
	public String getTenantName();
	
	public String getPromoCode();
	
	public SignUpPackage getSignUpPackage();
	
	public Integer getNumberOfUsers();
	
	public AddressInfo getBillingAddress();
	
	public String getPhone();
}
