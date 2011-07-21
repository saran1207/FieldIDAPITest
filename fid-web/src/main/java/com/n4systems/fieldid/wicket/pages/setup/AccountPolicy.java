package com.n4systems.fieldid.wicket.pages.setup;

import java.io.Serializable;

public class AccountPolicy implements Serializable {
	private static final long serialVersionUID = -8015854089001738209L;
	
	private Integer maxAttempts;
	private Integer lockoutDurationInMinutes;

	@Deprecated //only for dev use until persistence is implemented.
	public static AccountPolicy makeDummyAccountPolicy() { 
		AccountPolicy policy = new AccountPolicy();
		policy.setMaxAttempts(3);
		policy.setLockoutDuration(1);
		return policy;
	}
	
	public Integer getMaxAttempts() {
		return maxAttempts;
	}
	public void setMaxAttempts(Integer maxAttempts) {
		this.maxAttempts = maxAttempts;
	}
	public Integer getLockoutDuration() {
		return lockoutDurationInMinutes;
	}
	public void setLockoutDuration(Integer lockoutDuration) {
		this.lockoutDurationInMinutes = lockoutDuration;
	}
	
}
