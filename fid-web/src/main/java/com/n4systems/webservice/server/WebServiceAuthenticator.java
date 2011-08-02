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
			authenticationResponse.setAuthenticationResult(AuthenticationResponse.AuthenticationResult.NOT_SUCCESSFUL);
			UserManager userManager = WsServiceLocator.getUser(null);
			
			try {				
				User loginUser = getUserIfValid(userManager);
				
				if (loginUser != null) {
					ServiceDTOBeanConverter converter = WsServiceLocator.getServiceDTOBeanConverter(loginUser.getTenant().getId());	
					authenticationResponse.setAuthenticationResult(AuthenticationResponse.AuthenticationResult.SUCCESSFUL);
					authenticationResponse.setUser(converter.convert(loginUser));
					authenticationResponse.setTenant(converter.convert(loginUser.getOwner().getPrimaryOrg()));
				} else {
					authenticationResponse.setAuthenticationMessage("Authentication failed.");
				}
			}
			catch(LoginException e) {
				if (e.requiresLocking()) { 
					userManager.lockUser(authenticationRequest.getTenantName(), e.getUserId(), e.getDuration(), e.getMaxAttempts());
				}
				
				authenticationResponse.setAuthenticationMessage("We're sorry, this account is locked.");
			}
		} else {
			authenticationResponse.setAuthenticationMessage("Your mobile version is too old. Please upgrade your mobile.");		
		}
		
		
		return authenticationResponse;		
	}
	
	private User getUserIfValid(UserManager userManager) {
		String tenantName = authenticationRequest.getTenantName();		
		String userId = authenticationRequest.getUserId();
		String password = authenticationRequest.getPassword();
		String securityRfid = authenticationRequest.getSecurityRfidNumber();
		
		User loginUser = null;
		
		if (authenticationRequest.getLoginType() == AuthenticationRequest.LoginType.USERNAME) {
			loginUser = userManager.findUserByPw(tenantName, userId, password);
		} else if (authenticationRequest.getLoginType() == AuthenticationRequest.LoginType.SECURITY) {
			loginUser = userManager.findUser(tenantName, securityRfid);
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
