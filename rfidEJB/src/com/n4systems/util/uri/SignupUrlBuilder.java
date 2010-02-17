package com.n4systems.util.uri;

import java.net.URI;

import rfid.ejb.entity.UserBean;

import com.n4systems.util.ConfigContext;

public class SignupUrlBuilder extends BaseUrlBuilder {
	private final UserBean referralUser;
	private final String signupPath;
	
	public SignupUrlBuilder(URI baseUri, ConfigContext configContext, UserBean referralUser, String signupPath) {
		super(baseUri, configContext);
		this.referralUser = referralUser;
		this.signupPath = signupPath;
	}
	
	private String buildSignupPath() {
		return signupPath + "/" + referralUser.getReferralKey();
	}
	
	@Override
	protected String path() {
		return buildSignupPath();
	}


	
	
}
