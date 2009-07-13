package com.n4systems.fieldid.web.services.exceptions;

public class WebServiceLoadingFailure extends Exception {

	
	private static final long serialVersionUID = 1L;

	public WebServiceLoadingFailure() {
		super();
		
	}

	public WebServiceLoadingFailure(String message, Throwable throwable) {
		super(message, throwable);
		
	}

	public WebServiceLoadingFailure(String message) {
		super(message);
		
	}

	public WebServiceLoadingFailure(Throwable throwable) {
		super(throwable);
		
	}

}
