package com.n4systems.exceptions;


@SuppressWarnings("serial")
public class LoginException extends RuntimeException {
	
	private LoginFailureInfo failureInfo;

	public LoginException(LoginFailureInfo failureInfo) {		
		super("user " + failureInfo.getUserId() + " failed to log into system");
		this.failureInfo = failureInfo;
	}
	
	public boolean isLocked() {
		return failureInfo.isLocked();
	}
		
	public int getAttempts() {
		return failureInfo.getAttempts();
	}

	public String getUserId() {
		return failureInfo.getUserId();
	}

	public Integer getDuration() {
		return failureInfo.getDuration();
	}

	public int getMaxAttempts() {
		return failureInfo.getMaxAttempts();
	}


	public LoginFailureInfo getLoginFailureInfo() {
		return failureInfo;
	}

	
}
