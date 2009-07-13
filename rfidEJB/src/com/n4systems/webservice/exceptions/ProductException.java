package com.n4systems.webservice.exceptions;

public class ProductException extends RequestException {

	private static final long serialVersionUID = 1L;
	
	public ProductException() {
		super();
	}

	public ProductException(String message, Throwable cause) {
		super( message, cause );
	}

	public ProductException(String message) {
		super( message );
	}

	public ProductException(Throwable cause) {
		super( cause );
	}



}
