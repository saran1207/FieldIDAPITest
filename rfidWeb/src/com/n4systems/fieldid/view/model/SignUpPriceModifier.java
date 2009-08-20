package com.n4systems.fieldid.view.model;

public interface SignUpPriceModifier {

	public int getNumberOfUsers();
	public void setNumberOfUsers(int numberOfUsers);
	
	public boolean isPhoneSupport();
	public void setPhoneSupport(boolean phoneSupport);
	
	public Long getSignUpPackageId();
	public void setSignUpPackageId(Long signUpPackageId);
}
