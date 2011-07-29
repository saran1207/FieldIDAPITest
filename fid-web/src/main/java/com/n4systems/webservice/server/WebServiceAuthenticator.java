package com.n4systems.webservice.server;


import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.LoginException;
import com.n4systems.model.user.User;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.WsServiceLocator;
import com.n4systems.webservice.dto.AuthenticationRequest;
import com.n4systems.webservice.dto.AuthenticationResponse;

public class WebServiceAuthenticator {
	
	AuthenticationRequest authenticationRequest;
	AuthenticationResponse authenticationResponse = new AuthenticationResponse();
	
	public WebServiceAuthenticator(AuthenticationRequest authenticationRequest) {
		this.authenticationRequest = authenticationRequest;
	}
	
	public AuthenticationResponse authenticate() {
		if (passesMinimumMobileVersion()) {
			User loginUser = getUserIfValid();
			
			if (loginUser != null) {
				ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(loginUser.getTenant().getId());	
				authenticationResponse.setAuthenticationResult(AuthenticationResponse.AuthenticationResult.SUCCESSFUL);
				authenticationResponse.setUser(converter.convert(loginUser));
				authenticationResponse.setTenant(converter.convert(loginUser.getOwner().getPrimaryOrg()));
			} else {
				authenticationResponse.setAuthenticationMessage("Authentication failed");
				authenticationResponse.setAuthenticationResult(AuthenticationResponse.AuthenticationResult.NOT_SUCCESSFUL);
			}			
		} else {
			authenticationResponse.setAuthenticationMessage("Your mobile version is too old. Please upgrade your mobile.");
			authenticationResponse.setAuthenticationResult(AuthenticationResponse.AuthenticationResult.NOT_SUCCESSFUL);			
		}
		
		
		return authenticationResponse;		
	}
	
	private User getUserIfValid() {
		UserManager userManager = WsServiceLocator.getUser(null);
		
		String tenantName = authenticationRequest.getTenantName();		
		String userId = authenticationRequest.getUserId();
		String password = authenticationRequest.getPassword();
		String securityRfid = authenticationRequest.getSecurityRfidNumber();
		
		User loginUser = null;
		
		try {
			if (authenticationRequest.getLoginType() == AuthenticationRequest.LoginType.USERNAME) {
				loginUser = userManager.findUserByPw(tenantName, userId, password);
			} else if (authenticationRequest.getLoginType() == AuthenticationRequest.LoginType.SECURITY) {
				loginUser = userManager.findUser(tenantName, securityRfid);
			}
		} catch (LoginException e) { 
			loginUser = null;		// couldn't login.  (don't want an exception thrown here...just return null value).
		}
		return loginUser;
	}
	
	public boolean passesMinimumMobileVersion() {
		ConfigurationProvider configContext = ConfigContext.getCurrentContext();
		
		int minimumMajorVersion = configContext.getInteger(ConfigEntry.MINIMUM_MOBILE_MAJOR_VERSION);
		int minimumMinorVersion = configContext.getInteger(ConfigEntry.MINIMUM_MOBILE_MINOR_VERSION);
		
		boolean passesVersionCheck = (authenticationRequest.getMajorVersion() >= minimumMajorVersion) &&
									 (authenticationRequest.getMinorVersion() >= minimumMinorVersion);
		
		
		return passesVersionCheck;
	}

}
