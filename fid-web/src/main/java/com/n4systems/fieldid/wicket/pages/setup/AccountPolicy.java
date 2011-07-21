package com.n4systems.fieldid.wicket.pages.setup;

import java.io.Serializable;

public class AccountPolicy implements Serializable {
	private static final long serialVersionUID = -8015854089001738209L;
	
	private String maxAttempts;
	private String lockoutDuration;
	
	public String getMaxAttempts() {
		return maxAttempts;
	}
	public void setMaxAttempts(String maxAttempts) {
		this.maxAttempts = maxAttempts;
	}
	public String getLockoutDuration() {
		return lockoutDuration;
	}
	public void setLockoutDuration(String lockoutDuration) {
		this.lockoutDuration = lockoutDuration;
	}
	
}
