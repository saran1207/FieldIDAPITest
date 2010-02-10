package com.n4systems.fieldid.selenium.datatypes;


public class SystemSettings {
	String companyID;
	String currentPlan;
	long diskSpaceMaximum;			// -1 for unlimited
	long diskSpaceCurrent;
	long employeeAccountsMaximum;	// -1 for unlimited
	long employeeAccountsCurrent;
	long assetsMaximum;				// -1 for unlimited
	long assetsCurrent;
	String signInURL;
	String embeddedSignInCode;
	String preferredDateFormat;
	String defaultVendorContext;
	String webSiteAddress;
	
	public String getCompanyID() {
		return companyID;
	}
	
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}
	
	public String getCurrentPlan() {
		return currentPlan;
	}
	
	public void setCurrentPlan(String currentPlan) {
		this.currentPlan = currentPlan;
	}
	
	public long getDiskSpaceMaximum() {
		return diskSpaceMaximum;
	}
	
	public void setDiskSpaceMaximum(long diskSpaceMaximum) {
		this.diskSpaceMaximum = diskSpaceMaximum;
	}
	
	public long getDiskSpaceCurrent() {
		return diskSpaceCurrent;
	}
	
	public void setDiskSpaceCurrent(long diskSpaceCurrent) {
		this.diskSpaceCurrent = diskSpaceCurrent;
	}
	
	public long getEmployeeAccountsMaximum() {
		return employeeAccountsMaximum;
	}
	
	public void setEmployeeAccountsMaximum(long employeeAccountsMaximum) {
		this.employeeAccountsMaximum = employeeAccountsMaximum;
	}
	
	public long getEmployeeAccountsCurrent() {
		return employeeAccountsCurrent;
	}
	
	public void setEmployeeAccountsCurrent(long employeeAccountsCurrent) {
		this.employeeAccountsCurrent = employeeAccountsCurrent;
	}
	
	public long getAssetsMaximum() {
		return assetsMaximum;
	}
	
	public void setAssetsMaximum(long assetsMaximum) {
		this.assetsMaximum = assetsMaximum;
	}
	
	public long getAssetsCurrent() {
		return assetsCurrent;
	}
	
	public void setAssetsCurrent(long assetsCurrent) {
		this.assetsCurrent = assetsCurrent;
	}
	
	public String getSignInURL() {
		return signInURL;
	}
	
	public void setSignInURL(String signInURL) {
		this.signInURL = signInURL;
	}
	
	public String getEmbeddedSignInCode() {
		return embeddedSignInCode;
	}
	
	public void setEmbeddedSignInCode(String embeddedSignInCode) {
		this.embeddedSignInCode = embeddedSignInCode;
	}
	
	public String getPreferredDateFormat() {
		return preferredDateFormat;
	}
	
	public void setPreferredDateFormat(String preferredDateFormat) {
		this.preferredDateFormat = preferredDateFormat;
	}
	
	public String getDefaultVendorContext() {
		return defaultVendorContext;
	}
	
	public void setDefaultVendorContext(String defaultVendorContext) {
		this.defaultVendorContext = defaultVendorContext;
	}
	
	public String getWebSiteAddress() {
		return webSiteAddress;
	}
	
	public void setWebSiteAddress(String webSiteAddress) {
		this.webSiteAddress = webSiteAddress;
	}
}
