package com.n4systems.services.limiters;


public enum LimitType {
	DISK_SPACE, 
	ASSETS			("Warning: Asset Limit", "assetLimitAlert", 90, 95, 99),
	SECONDARY_ORGS	("Warning: Organization Limit", "secondaryOrgLimitAlert", 90, 95, 99),
	EMPLOYEE_USERS;
	
	private boolean alertByEmail;
	private int[] alertThresholds;
	private String emailSubject;
	private String emailTemplate;
	
	LimitType() {
		alertByEmail = false;
	}
	
	LimitType(String emailSubject, String emailTemplate, int ... alertThresholds) {
		alertByEmail = true;
		this.emailSubject = emailSubject;
		this.emailTemplate = emailTemplate;
		this.alertThresholds = alertThresholds;
	}

	public boolean isAlertByEmail() {
		return alertByEmail;
	}

	public int[] getAlertThresholds() {
		return alertThresholds;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public String getEmailTemplate() {
		return emailTemplate;
	}
	
	/**
	 * @return true if this limit has surpassed an alert threshold 
	 */
	public boolean isPassedThreshold(ResourceLimit limit) {
		boolean passed = false;
		
		int usagePcnt = (int)Math.floor(limit.getUsagePercent() * 100.0);
		for (int i = 0; i < alertThresholds.length; i++) {
			if (usagePcnt >= alertThresholds[i]) {
				passed = true;
				break;
			}
		}
		
		return passed;
	}
	
	/**
	 * @return true if this limit has surpassed an alert threshold.  -1 if no threshold was passed. 
	 */
	public int getLastPassedThreshold(ResourceLimit limit) {
		int lastThreshold = -1;
		
		int usagePcnt = (int)Math.floor(limit.getUsagePercent() * 100.0);
		for (int i = alertThresholds.length - 1; i >= 0; i--) {
			// working backwards, find the first threshold that is passed
			if (usagePcnt >= alertThresholds[i]) {
				lastThreshold = alertThresholds[i];
				break;
			}
		}
		
		return lastThreshold;
	}
	
}
