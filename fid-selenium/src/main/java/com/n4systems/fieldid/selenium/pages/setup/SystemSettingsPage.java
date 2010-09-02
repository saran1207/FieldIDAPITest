package com.n4systems.fieldid.selenium.pages.setup;

import com.n4systems.fieldid.selenium.datatypes.SystemSettings;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class SystemSettingsPage extends FieldIDPage {
	
	private static final String COMPANY_ID_XPATH = "//label[contains(text(),'Company ID')]/../SPAN";
	private static final String YOUR_CURRENT_PLAN_XPATH = "//*[@id='currentPlan']";
	private static final String DISK_USAGE_XPATH = "//label[contains(text(),'Disk Space')]/../DIV/DIV[contains(text(),'bytes of')]";
	private static final String EMPLOYEE_ACCOUNTS_XPATH = "//label[contains(text(),'Employee Accounts')]/../DIV/DIV[contains(text(),' of ')]";
	private static final String ASSETS_XPATH = "//label[contains(text(),'Assets')]/../DIV/DIV[contains(text(),' of ')]";
	private static final String SIGN_IN_URL_XPATH = "//label[contains(text(),'Your Sign In URL')]/../SPAN";
	private static final String EMBEDDED_SIGN_IN_CODE_XPATH = "//label[contains(text(),'Embedded Sign In Code')]/../SPAN";
	private static final String PERFERRED_DATE_FORMAT_SELECT_LIST_XPATH = "//select[@id='systemSettingsUpdate_dateFormat']";
	private static final String DEFAULT_VENDOR_CONTEXT_SELECT_LIST_XPATH = "//select[@id='systemSettingsUpdate_defaultVendorContext']";
	private static final String WEB_SITE_ADDRESS_XPATH = "//input[@id='systemSettingsUpdate_webSite']";

	public SystemSettingsPage(Selenium selenium) {
		super(selenium);
	}
	
	public SystemSettings getSystemSettings(boolean hasVendors) {
		SystemSettings ss = new SystemSettings();
		ss.setCompanyID(getCompanyID());
		ss.setCurrentPlan(getYourCurrentPlan());
		ss.setDiskSpaceCurrent(getDiskSpaceCurrent());
		ss.setDiskSpaceMaximum(getDiskSpaceMaximum());
		ss.setEmployeeAccountsCurrent(getEmployeeAccountsCurrent());
		ss.setEmployeeAccountsMaximum(getEmployeeAccountsMaximum());
		ss.setAssetsCurrent(getAssetsCurrent());
		ss.setAssetsMaximum(getAssetsMaximum());
		ss.setSignInURL(getYourSignInURL());
		ss.setEmbeddedSignInCode(getEmbeddedSignInCode());
		ss.setPreferredDateFormat(getSelectedPreferredDateFormat());
		if(hasVendors) {
			ss.setDefaultVendorContext(getSelectedDefaultVendor());
		}
		ss.setWebSiteAddress(getWebSiteAddress());
		return ss;
	}

	public String getYourCurrentPlan() {
		return selenium.getText(YOUR_CURRENT_PLAN_XPATH);
	}

	public String getCompanyID() {
		return selenium.getText(COMPANY_ID_XPATH);
	}
	
	public long getDiskSpaceMaximum() {
		long result = -1;	// unlimited
		long mb = 1024;
		String s = selenium.getText(DISK_USAGE_XPATH);
		s = s.replaceFirst("[^ ]* bytes of ([^ ]*) .*", "$1");	// x bytes of y MB -> y
		if(!s.equals("Unlimited")) {
			result = Long.parseLong(s) * mb;					// y = y * mb
		}
		return result;
	}

	public long getDiskSpaceCurrent() {
		String s = selenium.getText(DISK_USAGE_XPATH);
		s = s.replaceFirst(" bytes of .*", "");					// x bytes of y MB -> x
		return Long.parseLong(s);
	}
	
	public long getEmployeeAccountsMaximum() {
		long result = -1;	// unlimited
		String s = selenium.getText(EMPLOYEE_ACCOUNTS_XPATH);
		s = s.replaceFirst("[^ ]* of (.*)", "$1");	// x of y -> y
		if(!s.equals("Unlimited")) {
			result = Long.parseLong(s);
		}
		return result;
	}

	public long getEmployeeAccountsCurrent() {
		String s = selenium.getText(EMPLOYEE_ACCOUNTS_XPATH);
		s = s.replaceFirst(" of [0-9]*", "");			// x of y -> x
		return Long.parseLong(s);
	}
	
	public long getAssetsMaximum() {
		long result = -1;
		String s = selenium.getText(ASSETS_XPATH);
		s = s.replaceFirst("[^ ]* of ", "");		// x of y - > y
		if(!s.equals("Unlimited")) {
			result = Long.parseLong(s);
		}
		return result;
	}

	public long getAssetsCurrent() {
		String s = selenium.getText(ASSETS_XPATH);
		s = s.replaceFirst(" of .*", "");		// x of y - > x
		return Long.parseLong(s);
	}
	
	public String getYourSignInURL() {
		return selenium.getText(SIGN_IN_URL_XPATH);
	}
	
	public String getEmbeddedSignInCode() {
		return selenium.getText(EMBEDDED_SIGN_IN_CODE_XPATH);
	}

	public String getSelectedPreferredDateFormat() {
		return selenium.getSelectedValue(PERFERRED_DATE_FORMAT_SELECT_LIST_XPATH);
	}
	
	public String getSelectedDefaultVendor() {
		return selenium.getSelectedLabel(DEFAULT_VENDOR_CONTEXT_SELECT_LIST_XPATH);
	}
	
	public String getWebSiteAddress() {
		if(selenium.isElementPresent(WEB_SITE_ADDRESS_XPATH)) {
			return selenium.getValue(WEB_SITE_ADDRESS_XPATH);
		}
		return null;
	}


}
