package com.n4systems.subscription;

import com.n4systems.model.signuppackage.SignUpPackage;


public interface AccountCreationInformation {

	public String getFirstName();
	
	public String getLastName();
	
	public String getEmail();
	
	public String getFullTimeZone();

	public String getUsername() ;

	public String getPassword();
	

	public String getCompanyName();
	
	public String getTenantName();
	
	
	
	public SignUpPackage getSignUpPackage();
	
	public Integer getNumberOfUsers();
}
