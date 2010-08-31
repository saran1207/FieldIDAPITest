package com.n4systems.taskscheduling;

@SuppressWarnings("serial")
public class SchedulingException extends Exception {

	public SchedulingException() {}

	public SchedulingException(String message) {
		super(message);
	}

	public SchedulingException(Throwable cause) {
		super(cause);
	}

	public SchedulingException(String message, Throwable cause) {
		super(message, cause);
	}

}
