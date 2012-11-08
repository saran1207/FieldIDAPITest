package com.n4systems.exceptions;

import com.google.common.base.Preconditions;
import com.n4systems.model.user.User;

import java.util.Date;

public class LoginFailureInfo {

	private boolean locked = false;
    private boolean existingUser;
	private String userId;
	private int attempts;
	private int maxAttempts;
	private int duration;
    private Date lockedUntil;

	public LoginFailureInfo(User user, int maxAttempts, int duration) {
		this(user.getUserID().toLowerCase(), user.getFailedLoginAttempts(), maxAttempts, user.isLocked(), duration, true);
	}

	public LoginFailureInfo(String userId, int maxAttempts, boolean locked, int duration) {
		this(userId, 1, maxAttempts, locked, duration, true);
	}

    public LoginFailureInfo(String userId, int maxAttempts, boolean locked, int duration, boolean existingUser) {
        this(userId, 1, maxAttempts, locked, duration, existingUser);
    }

	private LoginFailureInfo(String userId, int attempts, int maxAttempts, boolean locked, int duration, boolean existingUser) {
		Preconditions.checkArgument(userId!=null);
		this.attempts = attempts;
		this.duration = duration; 
		this.maxAttempts = maxAttempts;
		this.userId = userId;
		this.locked = locked;
        this.existingUser = existingUser;
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
		return new LoginFailureInfo(userId, attempts, maxAttempts, locked, duration, existingUser);
	}

    public void incrementAndLockIfNecessary() {
        attempts++;
        locked = requiresLocking();
        lockedUntil = new Date(System.currentTimeMillis() + duration * 60 * 1000);
    }

    public boolean isExistingUser() {
        return existingUser;
    }

    public void unlockIfNecessary() {
        if (locked && new Date().getTime() > lockedUntil.getTime()) {
            locked = false;
            attempts = 0;
        }
    }
}
