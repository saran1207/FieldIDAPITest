package com.n4systems.exceptions;

import com.n4systems.model.user.User;

public class InvalidLoginKeyException extends AuthException {
	private static final long serialVersionUID = 1L;

	public InvalidLoginKeyException() {
		super();
	}

	public InvalidLoginKeyException(String message) {
		super(message);
	}
	
	public InvalidLoginKeyException(Throwable cause) {
		super(cause);
	}
	
	public InvalidLoginKeyException(User user, String message) {
		super("Invalid LoginKey requested for User [" + user.getUserID() + "], Tenant [" + user.getTenant().getName() + "].  " + message);
	}
}
