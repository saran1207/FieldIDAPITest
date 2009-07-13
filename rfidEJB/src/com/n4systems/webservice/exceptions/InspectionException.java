package com.n4systems.webservice.exceptions;

public class InspectionException extends RequestException {

	private static final long serialVersionUID = 1L;

	public InspectionException() {
		super();
	}

	public InspectionException(String message, Throwable cause) {
		super( message, cause );
	}

	public InspectionException(String message) {
		super( message );
	}

	public InspectionException(Throwable cause) {
		super( cause );
	}

	
}
