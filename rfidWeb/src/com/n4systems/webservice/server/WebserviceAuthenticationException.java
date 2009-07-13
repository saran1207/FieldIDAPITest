package com.n4systems.webservice.server;

public class WebserviceAuthenticationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public WebserviceAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebserviceAuthenticationException(String message) {
		super(message);
	}
}
