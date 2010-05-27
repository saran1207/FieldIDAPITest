package com.n4systems.util.uri;

import java.net.URI;


import com.n4systems.model.user.User;
import com.n4systems.util.ConfigContext;

public class SignupUrlBuilder extends BaseUrlBuilder {
	private final User referralUser;
	private final String signupPath;
	
	public SignupUrlBuilder(URI baseUri, ConfigContext configContext, User referralUser, String signupPath) {
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
