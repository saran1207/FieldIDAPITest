package com.n4systems.exceptions;

import com.n4systems.model.user.User;

public class LoginException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private boolean locked = false;
	private String userId;
	
	public LoginException(User user) {
		super( user==null ? "user doesn't exist" : 
				user.getLocked() ? "user is locked" : "incorrect password" );
		locked = user==null ? false : user.getLocked();
		userId = user==null ? "" : user.getUserID();
	}
		

	public boolean isLocked() {
		return locked;
	}
	
	public String getUser() { 
		return userId;
	}

}
