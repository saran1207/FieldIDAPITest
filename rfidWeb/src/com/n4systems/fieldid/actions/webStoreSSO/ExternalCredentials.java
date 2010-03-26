package com.n4systems.fieldid.actions.webStoreSSO;

import java.io.Serializable;

import com.n4systems.model.api.ExternalCredentialProvider;

public class ExternalCredentials implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	
	public String getUserName() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setCredentials(ExternalCredentialProvider provider) {
		userName = provider.getExternalUserName();
		password = provider.getExternalPassword();			
	}
}
