package com.n4systems.exceptions;

public class InvalidScheduleStateException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidScheduleStateException() {
		super();
	}

	public InvalidScheduleStateException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidScheduleStateException(String arg0) {
		super(arg0);
	}

	public InvalidScheduleStateException(Throwable arg0) {
		super(arg0);
	}
	

}
