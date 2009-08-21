package com.n4systems.fieldid.view.model;

public interface SignUpPriceModifier {

	public Integer getNumberOfUsers();
	public void setNumberOfUsers(Integer numberOfUsers);
	
	public boolean isPhoneSupport();
	public void setPhoneSupport(boolean phoneSupport);
	
	public Long getSignUpPackageId();
	public void setSignUpPackageId(Long signUpPackageId);
}
