package com.n4systems.exceptions;



public class Exceptions {


	public static RuntimeException convertToRuntimeException(Exception exception) {
		if (exception instanceof RuntimeException) {
			return (RuntimeException)exception;
		}
	
		return new RuntimeException(exception);
		
	}

	
}
