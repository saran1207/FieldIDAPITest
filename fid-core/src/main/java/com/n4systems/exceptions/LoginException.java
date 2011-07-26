package com.n4systems.exceptions;

import com.google.common.base.Objects;
import com.n4systems.model.user.User;

public class LoginException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private boolean locked = false;
	private String userId;
	private int attempts;
	private int maxAttempts;
	private Integer duration;
	
	
	public LoginException(User user, String userId, int maxAttempts, Integer duration) { 
		super("user " + userId + " failed to log into system");
		attempts = 1;
		this.duration = (duration!=null && !duration.equals(0)) ? duration : null;  // recall : 0 treated as no duration. 
		this.maxAttempts = maxAttempts;
		this.userId = userId;
		locked = user==null ? false : user.isLocked();
		
System.out.println(Objects.toStringHelper(this));
	}
		
	public int getAttempts() {
		return attempts;
	}
	
	public boolean requiresLocking() { 
		return attempts>=maxAttempts && !isLocked();		// if already locked, skip update.
	}
	
	public boolean isLocked() {
		return locked;
	}

	public String getUserId() {
		return userId;
	}

	public Integer getDuration() {
		return duration;
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public LoginException merge(LoginException e) {
		if (e!=null) { 
			attempts = e.getAttempts()+1;
		}
		return this;
	}

}
