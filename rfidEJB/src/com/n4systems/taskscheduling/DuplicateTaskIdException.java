package com.n4systems.taskscheduling;

@SuppressWarnings("serial")
public class DuplicateTaskIdException extends SchedulingException {

	public DuplicateTaskIdException(String message) {
		super(message);
	}
}
