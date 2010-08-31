package com.n4systems.webservice.exceptions;


/**
 * This is returned by the web service any time an unknown exception, Naming or db exception happens that you would not expect.
 * this type would mean that the request my not beable to be serviced at this time. 
 * @author aaitken
 *
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceException() {
	}

	public ServiceException(String message) {
		super( message );
	}

	public ServiceException(Throwable cause) {
		super( cause );
	}

	public ServiceException(String message, Throwable cause) {
		super( message, cause );
	}

}
