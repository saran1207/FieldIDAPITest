package com.n4systems.subscription;

public interface SignUpPriceModifier {

	public Integer getNumberOfUsers();
	
	public boolean isPhoneSupport();
	
	public Long getSignUpPackageId();
}
