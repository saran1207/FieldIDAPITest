package com.n4systems.webservice.server;


import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.model.user.User;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.ServiceLocator;
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
		UserManager userManager = ServiceLocator.getUser();
		
		String tenantName = authenticationRequest.getTenantName();		
		String userId = authenticationRequest.getUserId();
		String password = authenticationRequest.getPassword();
		String securityRfid = authenticationRequest.getSecurityRfidNumber();
		
		User loginUser = null;
		
		if (authenticationRequest.getLoginType() == AuthenticationRequest.LoginType.USERNAME) {
			// FIXME DD : need to figure out how to deal with account policy settings.   can web service allow infinite login attempts?
			loginUser = userManager.findUserByPw(tenantName, userId, password, null/*BOGUS VALUE TO BE OVERRIDDEN BY USERMANAGER*/);				
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
