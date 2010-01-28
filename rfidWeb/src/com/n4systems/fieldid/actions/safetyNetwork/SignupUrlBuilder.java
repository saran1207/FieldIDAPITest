package com.n4systems.fieldid.actions.safetyNetwork;

import java.net.URI;

import rfid.ejb.entity.UserBean;

import com.n4systems.fieldid.utils.UrlBuilder;

public class SignupUrlBuilder implements UrlBuilder {
	private final URI baseUri;
	private final UserBean referralUser;
	private final String signupPath;
	
	public SignupUrlBuilder(URI baseUri, UserBean referralUser, String signupPath) {
		this.baseUri = baseUri;
		this.referralUser = referralUser;
		this.signupPath = signupPath;
	}
	
	private String buildSignupPath() {
		return signupPath + "/" + referralUser.getReferralKey();
	}
	
	public URI getURI() {
		return baseUri.resolve(buildSignupPath());
	}

	public String build() {
		return getURI().toString();
	}
	
	@Override
	public String toString() {
		return build();
	}
	
}
