package com.n4systems.fieldid.ws.v1.resources.user;

import java.io.Serializable;

public class ApiUserLockoutPolicy implements Serializable {
	private int maxAttempts;
	private int lockoutDuration;
	private boolean lockoutOnMobile;

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public int getLockoutDuration() {
		return lockoutDuration;
	}

	public void setLockoutDuration(int lockoutDuration) {
		this.lockoutDuration = lockoutDuration;
	}

	public boolean isLockoutOnMobile() {
		return lockoutOnMobile;
	}

	public void setLockoutOnMobile(boolean lockoutOnMobile) {
		this.lockoutOnMobile = lockoutOnMobile;
	}
}
