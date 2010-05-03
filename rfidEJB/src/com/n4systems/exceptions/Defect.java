package com.n4systems.exceptions;

public class Defect extends RuntimeException {

	public Defect(String message) {
		super(message);
	}

	public Defect(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	

}
