package com.n4systems.fieldid.actions.helpers;

import java.util.Date;

import com.n4systems.exceptions.LoginException;

public class FailedLogin {
	private String userId;
	private int attempts;
	private int maxAttempts = 5;
	private Integer duration = 20;  // FIXME DD : need to implement this
	private long lastFailureMS = 0;
	
	public FailedLogin(LoginException e) {
		userId = e.getUser();
		attempts = 1;
		lastFailureMS = new Date().getTime();
	}

	public Integer getDuration() {
		return duration;
	}
	
	public String getTextLabel() {
		if (isNowLocked()) { 
			return duration!=null ? "error.accountlocked_duration" : "error.accountlocked_contact";
		} else {		
			return "error.loginfailure";
		}
	}
	
	public String[] getTextArgs() { 
		if (isNowLocked()) { 
			return new String[]{duration+""};
		} else {		
			return new String[]{attempts+"", maxAttempts+""};
		}
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}
	
	public boolean isNowLocked() {
		return attempts >= maxAttempts;
	}

	public String getUserId() {
		return userId;
	}
	
	public int getAttempts() { 
		return attempts;
	}
	
	public FailedLogin merge(FailedLogin failedLogin) {
		if (isSameUser(failedLogin)) { 
			attempts+=failedLogin.getAttempts();
		}
		return this;
	}

	private boolean isSameUser(FailedLogin failedLogin) {
		// TODO DD : add time or sessionId comparsion???  e.g. if new Date().getTime()-lastFailureMS<threshold....
		return failedLogin!=null && failedLogin.getUserId().equals(getUserId());
	}

}

