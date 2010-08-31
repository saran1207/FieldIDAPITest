package com.n4systems.exceptions;

import com.n4systems.model.user.User;

public class NonExistentLoginKeyException extends AuthException {
	private static final long serialVersionUID = 1L;

	public NonExistentLoginKeyException() {
		super();
	}

	public NonExistentLoginKeyException(String message) {
		super(message);
	}
	
	public NonExistentLoginKeyException(Throwable cause) {
		super(cause);
	}
	
	public NonExistentLoginKeyException(User user) {
		super("Non-Existant LoginKey requested for User [" + user.getUserID() + "], Tenant [" + user.getTenant().getName() + "]");
	}

}
