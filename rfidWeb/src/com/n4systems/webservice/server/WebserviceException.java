package com.n4systems.webservice.server;

public class WebserviceException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public WebserviceException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebserviceException(String message) {
		super(message);
	}
}
