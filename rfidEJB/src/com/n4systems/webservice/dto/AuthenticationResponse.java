package com.n4systems.webservice.dto;

public class AuthenticationResponse extends RequestResponse {

	public static enum AuthenticationResult {NOT_SUCCESSFUL, SUCCESSFUL};
	
	private AuthenticationResult authenticationResult = AuthenticationResult.NOT_SUCCESSFUL;
	private String authenticationMessage;
	private TenantServiceDTO tenant;
	private UserServiceDTO user;

	public AuthenticationResult getAuthenticationResult() {
		return authenticationResult;
	}

	public void setAuthenticationResult(AuthenticationResult authenticationResult) {
		this.authenticationResult = authenticationResult;
	}

	public TenantServiceDTO getTenant() {
		return tenant;
	}

	public void setTenant(TenantServiceDTO tenant) {
		this.tenant = tenant;
	}

	public UserServiceDTO getUser() {
		return user;
	}

	public void setUser(UserServiceDTO user) {
		this.user = user;
	}

	public String getAuthenticationMessage() {
		return authenticationMessage;
	}

	public void setAuthenticationMessage(String authenticationMessage) {
		this.authenticationMessage = authenticationMessage;
	}	
}
