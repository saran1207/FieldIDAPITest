package com.n4systems.exceptions;

import rfid.ejb.entity.UserBean;

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
	
	public InvalidLoginKeyException(UserBean user, String message) {
		super("Invalid LoginKey requested for User [" + user.getUserID() + "], Tenant [" + user.getTenant().getName() + "].  " + message);
	}
}
