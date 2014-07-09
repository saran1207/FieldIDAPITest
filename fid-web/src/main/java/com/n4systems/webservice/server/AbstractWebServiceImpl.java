package com.n4systems.webservice.server;

import com.n4systems.exceptions.LoginException;
import com.n4systems.fieldid.service.SecurityContextInitializer;
import com.n4systems.fieldid.ws.v1.exceptions.ForbiddenException;
import com.n4systems.model.user.User;
import com.n4systems.util.WsServiceLocator;
import com.n4systems.webservice.server.bundles.AuthBundle;
import org.apache.log4j.Logger;

public abstract class AbstractWebServiceImpl implements AbstractWebService {
	private Logger logger = Logger.getLogger(AbstractWebServiceImpl.class);

	protected User authenticateUser(AuthBundle authUser) throws WebserviceAuthenticationException {
        User user = null;
        try {
            user = WsServiceLocator.getUser(null /*note : i don't know the tenantId so just passing in null.  not needed*/).
                            findUserByPw(authUser.getTenantName(), authUser.getUserName(), authUser.getPassword());
        } catch (LoginException e) {
            logger.warn("User failed authentication to the webservice: tenant [" + authUser.getTenantName() + "] username [" + authUser.getUserName() + "]");
            throw new WebserviceAuthenticationException("Bad Organization name, Username or Password");
        }

        if(user == null) {
			logger.warn("User failed authentication to the webservice: tenant [" + authUser.getTenantName() + "] username [" + authUser.getUserName() + "]");
			throw new WebserviceAuthenticationException("Bad Organization name, Username or Password");
		}
        String authKey = user.getAuthKey();
        try {
            SecurityContextInitializer.initSecurityContext(authKey);
        } catch (SecurityException e) {
            throw new ForbiddenException("Invalid auth key '" + authKey + "'");
        }

		logger.warn("User authenticated sucessfully to the webservice: tenant [" + authUser.getTenantName() + "] username [" + authUser.getUserName() + "]");
		
		return user;
	}
	
	@Override
	@Deprecated
	public String testConnection(String tenantName, String userName, String userPassword, String message) throws WebserviceAuthenticationException {
		return connectionTest(new AuthBundle(tenantName, userName, userPassword), message);
	}
	
	@Override
	public String connectionTest(AuthBundle authUser, String message) throws WebserviceAuthenticationException {
		
		// if auth fails, this will throw the auth exception before we return the message
		authenticateUser(authUser);
		
		logger.info("Client Requested testConnection:  " + message);
		
		return message;
	}
}
