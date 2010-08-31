package com.n4systems.webservice.server;

import com.n4systems.webservice.server.bundles.AuthBundle;


public interface AbstractWebService {
	
	@Deprecated
	public String testConnection(String tenantName, String userName, String userPassword, String message) throws WebserviceAuthenticationException;
	
	public String connectionTest(AuthBundle authUser, String message) throws WebserviceAuthenticationException;
}
