package com.n4systems.webservice.server;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.ServiceDTOBeanConverter;
import rfid.ejb.session.User;

import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ServiceLocator;
import com.n4systems.webservice.dto.AuthenticationRequest;
import com.n4systems.webservice.dto.AuthenticationResponse;

public class WebServiceAuthenticator {
	
	AuthenticationRequest authenticationRequest;
	AuthenticationResponse authenticationResponse = new AuthenticationResponse();
	
	public WebServiceAuthenticator(AuthenticationRequest authenticationRequest) {
		this.authenticationRequest = authenticationRequest;
	}
	
	public AuthenticationResponse authenticate() {
		
		ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
		
		
		if (passesMinimumMobileVersion()) {
			UserBean loginUser = getUserIfValid();
			
			if (loginUser != null) {
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
	
	private UserBean getUserIfValid()
	{
		User userManager = ServiceLocator.getUser();
		
		String tenantName = authenticationRequest.getTenantName();		
		String userId = authenticationRequest.getUserId();
		String password = authenticationRequest.getPassword();
		String securityRfid = authenticationRequest.getSecurityRfidNumber();
		
		UserBean loginUser = null;
		
		if (authenticationRequest.getLoginType() == AuthenticationRequest.LoginType.USERNAME) {
			loginUser = userManager.findUser(tenantName, userId, password);				
		} else if (authenticationRequest.getLoginType() == AuthenticationRequest.LoginType.SECURITY) {
			loginUser = userManager.findUser(tenantName, securityRfid);
		}
		
		return loginUser;
	}
	
	public boolean passesMinimumMobileVersion() {
		ConfigContext configContext = ConfigContext.getCurrentContext();
		
		int minimumMajorVersion = configContext.getInteger(ConfigEntry.MINIMUM_MOBILE_MAJOR_VERSION);
		int minimumMinorVersion = configContext.getInteger(ConfigEntry.MINIMUM_MOBILE_MINOR_VERSION);
		
		boolean passesVersionCheck = (authenticationRequest.getMajorVersion() >= minimumMajorVersion) &&
									 (authenticationRequest.getMinorVersion() >= minimumMinorVersion);
		
		
		return passesVersionCheck;
	}

}
