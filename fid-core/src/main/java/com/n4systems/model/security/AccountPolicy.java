package com.n4systems.model.security;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@SuppressWarnings("serial")
public class AccountPolicy implements Serializable {
	private int maxAttempts;
	private int lockoutDuration;
	private boolean lockoutOnMobile;

	public static AccountPolicy DEFAULT_ACCOUNT_POLICY = new AccountPolicy();
	
	public AccountPolicy() {
		this(5, 10, false);
	}

	public AccountPolicy(int maxAttempts, int lockoutDuration, boolean lockoutOnMobile) {
		this.maxAttempts = maxAttempts;
		this.lockoutDuration = lockoutDuration;
		this.lockoutOnMobile = lockoutOnMobile;
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(Integer maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public int getLockoutDuration() {
		return lockoutDuration;
	}

	public void setLockoutDuration(Integer lockoutDuration) {
		this.lockoutDuration = lockoutDuration;
	}

	public boolean isLockoutOnMobile() {
		return lockoutOnMobile;
	}

	public void setLockoutOnMobile(boolean lockoutOnMobile) {
		this.lockoutOnMobile = lockoutOnMobile;
	}
}
