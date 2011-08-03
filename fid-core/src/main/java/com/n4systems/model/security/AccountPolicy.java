package com.n4systems.model.security;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
@SuppressWarnings("serial")
public class AccountPolicy implements Serializable {
	private int maxAttempts;
	private int lockoutDuration;

	public static AccountPolicy DEFAULT_ACCOUNT_POLICY = new AccountPolicy();
	
	public AccountPolicy() {
		this(5, 10);
	}

	public AccountPolicy(int maxAttempts, int lockoutDuration) {
		this.maxAttempts = maxAttempts;
		this.lockoutDuration = lockoutDuration;
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

}
