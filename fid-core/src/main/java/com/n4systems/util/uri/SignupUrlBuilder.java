package com.n4systems.util.uri;

import com.n4systems.model.user.User;
import com.n4systems.util.ConfigurationProvider;

import java.net.URI;

public class SignupUrlBuilder extends InternalUrlBuilder {
	private final User referralUser;
	private final String signupPath;
	
	public SignupUrlBuilder(URI baseUri, ConfigurationProvider configContext, User referralUser, String signupPath) {
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
