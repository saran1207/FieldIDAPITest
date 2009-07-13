package com.n4systems.webservice.server;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.util.ServiceLocator;
import com.n4systems.webservice.server.bundles.AuthBundle;

public abstract class AbstractWebServiceImpl implements AbstractWebService {
	private Logger logger = Logger.getLogger(AbstractWebServiceImpl.class);

	protected UserBean authenticateUser(AuthBundle authUser) throws WebserviceAuthenticationException {
		UserBean user = ServiceLocator.getUser().findUser(authUser.getTenantName(), authUser.getUserName(), authUser.getPassword());
		
		if(user == null) {
			logger.warn("User failed authentication to the webservice: tenant [" + authUser.getTenantName() + "] username [" + authUser.getUserName() + "]");
			throw new WebserviceAuthenticationException("Bad Organization name, Username or Password");
		}
		
		logger.warn("User authenticated sucessfully to the webservice: tenant [" + authUser.getTenantName() + "] username [" + authUser.getUserName() + "]");
		
		return user;
	}
	
	@Deprecated
	public String testConnection(String tenantName, String userName, String userPassword, String message) throws WebserviceAuthenticationException {
		return connectionTest(new AuthBundle(tenantName, userName, userPassword), message);
	}
	
	public String connectionTest(AuthBundle authUser, String message) throws WebserviceAuthenticationException {
		
		// if auth fails, this will throw the auth exception before we return the message
		authenticateUser(authUser);
		
		logger.info("Client Requested testConnection:  " + message);
		
		return message;
	}
}
