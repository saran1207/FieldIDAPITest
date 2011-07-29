package com.n4systems.model.security;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class AccountPolicy implements Serializable {
	private int maxAttempts;
	private int lockoutDuration;

	public AccountPolicy() {
		this(5, 10);
	}

	public AccountPolicy(int maxAttempts, int lockoutDuration) {
		this.maxAttempts = maxAttempts;
		this.lockoutDuration = lockoutDuration;
	}

	public Integer getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(Integer maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public Integer getLockoutDuration() {
		return lockoutDuration;
	}

	public void setLockoutDuration(Integer lockoutDuration) {
		this.lockoutDuration = lockoutDuration;
	}

	@Deprecated
	// only for dev use until persistence is implemented.
	public static AccountPolicy makeDummyAccountPolicy() {
		AccountPolicy policy = new AccountPolicy();
		policy.setMaxAttempts(5);
		policy.setLockoutDuration(20);
		return policy;
	}
}
