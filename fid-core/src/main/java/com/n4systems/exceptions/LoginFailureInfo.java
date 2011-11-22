package com.n4systems.exceptions;

import com.google.common.base.Preconditions;
import com.n4systems.model.security.AccountPolicy;
import com.n4systems.model.user.User;

public class LoginFailureInfo {

	private boolean locked = false;
	private String userId;
	private int attempts;
	private int maxAttempts;
	private int duration;
	
	public LoginFailureInfo(User user, int maxAttempts, int duration) {
		this(user.getUserID().toLowerCase(), maxAttempts, user.isLocked(), duration);
	}

	public LoginFailureInfo(String userId) {
		this(userId.toLowerCase(), AccountPolicy.DEFAULT_ACCOUNT_POLICY.getMaxAttempts(), false, AccountPolicy.DEFAULT_ACCOUNT_POLICY.getLockoutDuration());
	}
	
	private LoginFailureInfo(String userId, int maxAttempts, boolean locked, int duration) {
		this(userId, 1, maxAttempts, locked, duration);
	}

	private LoginFailureInfo(String userId, int attempts, int maxAttempts, boolean locked, int duration) {
		Preconditions.checkArgument(userId!=null);
		this.attempts = attempts;
		this.duration = duration; 
		this.maxAttempts = maxAttempts;
		this.userId = userId;
		this.locked = locked;		
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

	public int getDuration() {
		return duration;
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}	
	
	public LoginFailureInfo merge(LoginFailureInfo failureInfo) {
		int attempts = failureInfo==null ? getAttempts() : failureInfo.getAttempts()+1;
		return new LoginFailureInfo(userId, attempts, maxAttempts, locked, duration);
	}
	
	
	
}
