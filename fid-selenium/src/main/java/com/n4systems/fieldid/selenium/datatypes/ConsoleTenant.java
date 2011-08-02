package com.n4systems.fieldid.selenium.datatypes;

public class ConsoleTenant {
	
	// defaults set to Basic tenant
	
	/*
	 * Serial Number Format Options.
	 * %m = Month as a 2 digit number
	 * %d = Day of month as a 2 digit number
	 * %Y = 4 digit year
	 * %y = 2 digit year
	 * %H = Hour of day
	 * %M = Minute of day
	 * %S = Second of day
	 * %L = Millisecond of day
	 * %j = Jergen's style date code
	 * %g = Autoincrementing counter (defaults to 6 digits, reset every year)
	 */
	String identifierFormat = "NSA%y-%g";
	String dateFormat = "MM/dd/yy";
	String diskSpaceLimit = "1048576000";
	String assetLimit = "250";
	String userLimit = "1";
	String secondaryOrgLimit = "0";
	boolean integration = false;
	boolean jobSites = false;
	boolean projects = false;
	boolean branding = false;
	boolean readOnlyUser = false;
	boolean emailAlerts = true;
	boolean customCerts = false;
	boolean dedicatedProgramManager = false;
	boolean multiLocation = false;
	boolean allowIntegration = false;
	boolean unlimitedLinkedAssets = false;
	String title = "";
	String note = "";

	public ConsoleTenant() {
	}

	public ConsoleTenant(String identifierFormat) {
		this.identifierFormat = identifierFormat;
	}

	public String getIdentifierFormat() {
		return this.identifierFormat;
	}

	public String getDateFormat() {
		return this.dateFormat;
	}
	
	public String getDiskSpaceLimit() {
		return this.diskSpaceLimit;
	}
	
	public String getAssetLimit() {
		return this.assetLimit;
	}
	
	public String getUserLimit() {
		return this.userLimit;
	}
	
	public String getSecondaryOrgLimit() {
		return this.secondaryOrgLimit;
	}
	
	public boolean getIntegration() {
		return this.integration;
	}
	
	public boolean getJobSites() {
		return this.jobSites;
	}
	
	public boolean getProjects() {
		return this.projects;
	}
	
	public boolean getBranding() {
		return this.branding;
	}
	
	public boolean getReadOnlyUser() {
		return this.readOnlyUser;
	}
	
	public boolean getEmailAlerts() {	
		return this.emailAlerts;
	}
	
	public boolean getCustomCerts() {
		return this.customCerts;
	}
	
	public boolean getDedicatedProgramManager() {
		return this.dedicatedProgramManager;
	}
	
	public boolean getMultiLocation() {
		return this.multiLocation;
	}
	
	public boolean getAllowIntegration() {
		return this.allowIntegration;
	}
	
	public boolean getUnlimitedLinkedAssets() {
		return this.unlimitedLinkedAssets;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getNote() {
		return this.note;
	}

	public void setIdentifierFormat(String s) {
		this.identifierFormat = s;
	}

	public void setDateFormat(String s) {
		this.dateFormat = s;
	}
	
	public void setDiskSpaceLimit(String s) {
		this.diskSpaceLimit = s;
	}
	
	public void setAssetLimit(String s) {
		this.assetLimit = s;
	}
	
	public void setUserLimit(String s) {
		this.userLimit = s;
	}
	
	public void setSecondaryOrgLimit(String s) {
		this.secondaryOrgLimit = s;
	}
	
	public void setIntegration(boolean b) {
		this.integration = b;
	}
	
	public void setJobSites(boolean b) {
		this.jobSites = b;
	}
	
	public void setProjects(boolean b) {
		this.projects = b;
	}
	
	public void setBranding(boolean b) {
		this.branding = b;
	}
	
	public void setReadOnlyUser(boolean b) {
		this.readOnlyUser = b;
	}
	
	public void setEmailAlerts(boolean b) {	
		this.emailAlerts = b;
	}
	
	public void setCustomCerts(boolean b) {
		this.customCerts = b;
	}
	
	public void setDedicatedProgramManager(boolean b) {
		this.dedicatedProgramManager = b;
	}
	
	public void setMultiLocation(boolean b) {
		this.multiLocation = b;
	}
	
	public void setAllowIntegration(boolean b) {
		this.allowIntegration = b;
	}
	
	public void setUnlimitedLinkedAssets(boolean b) {
		this.unlimitedLinkedAssets = b;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
}
