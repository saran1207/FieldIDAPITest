package com.n4systems.exceptions;

public class EJBLookupException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EJBLookupException() {
		super();
	}

	public EJBLookupException(Class<?> ejbClass, Throwable cause) {
		this(ejbClass.getSimpleName(), cause);
	}
	
	public EJBLookupException(String ejbName, Throwable cause) {
		super("Cannot resolve ejb " + ejbName, cause);
	}

}
