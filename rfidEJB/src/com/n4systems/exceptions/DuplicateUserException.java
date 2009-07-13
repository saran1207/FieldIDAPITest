package com.n4systems.exceptions;

public class DuplicateUserException extends Exception {
	private static final long serialVersionUID = 1L;
	private String userId;
	
	public DuplicateUserException() {}

	public DuplicateUserException(String message, String userId) {
		super(message);
		setUserId(userId);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
