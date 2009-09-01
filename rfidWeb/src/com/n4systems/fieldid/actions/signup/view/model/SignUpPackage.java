package com.n4systems.fieldid.actions.signup.view.model;

import com.n4systems.model.tenant.TenantLimit;

public class SignUpPackage {
	private int priceInDollars;
	private com.n4systems.model.signuppackage.SignUpPackageDetails signUpPackage;
	

	public SignUpPackage(String name) {
		super();
		this.signUpPackage = com.n4systems.model.signuppackage.SignUpPackageDetails.valueOf(name);
		
	}

	public com.n4systems.model.signuppackage.SignUpPackageDetails getSignUpPackage() {
		return signUpPackage;
	}
	
	public String getName() {
		return signUpPackage.getName();
	}

	public int getPriceInDollars() {
		return priceInDollars;
	}

	public boolean isPreferred() {
		return (signUpPackage == com.n4systems.model.signuppackage.SignUpPackageDetails.Enterprise);
	}

	public String getNumberOfUsersLabel() {
		if (signUpPackage.getUsers().equals(TenantLimit.UNLIMITED)) { 
			return "label.unlimited";
		}
		
		return signUpPackage.getUsers().toString();
	}


}
