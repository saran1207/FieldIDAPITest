package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.datatypes.SystemSettings;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.*;

public class ManageSystemSettings {
	Selenium selenium;
	Misc misc;
	private String manageSystemSettingsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage System Settings')]";
	private String companyIDLocator = "xpath=//LABEL[contains(text(),'Company ID')]/../SPAN";
	private String yourCurrentPlanLocator = "xpath=//*[@id='currentPlan']";
	private String currentDiskUsageLocator = "xpath=//LABEL[contains(text(),'Disk Space')]/../DIV/DIV[contains(text(),'bytes of')]";
	private String maximumDiskUsageLocator = currentDiskUsageLocator;
	private String currentEmployeeAccountsLocator = "xpath=//LABEL[contains(text(),'Employee Accounts')]/../DIV/DIV[contains(text(),' of ')]";
	private String maximumEmployeeAccountsLocator = currentEmployeeAccountsLocator;
	private String currentAssetsLocator = "xpath=//LABEL[contains(text(),'Assets')]/../DIV/DIV[contains(text(),' of ')]";
	private String maximumAssetsLocator = currentAssetsLocator;
	private String yourSigninURLLocator = "xpath=//LABEL[contains(text(),'Your Sign In URL')]/../SPAN";
	private String embeddedSignInCodeLocator = "xpath=//LABEL[contains(text(),'Embedded Sign In Code')]/../SPAN";
	private String preferredDateFormatSelectListLocator = "xpath=//SELECT[@id='systemSettingsUpdate_dateFormat']";
	private String defaultVendorContextSelectListLocator = "xpath=//SELECT[@id='systemSettingsUpdate_defaultVendorContext']";
	private String webSiteAddressTextFieldLocator = "xpath=//INPUT[@id='systemSettingsUpdate_webSite']";
	private String saveButtonLocator = "xpath=//INPUT[@id='systemSettingsUpdate_label_save']";
	private String cancelLinkLocator = "xpath=//A[contains(text(),'Cancel')]";
	
	public ManageSystemSettings(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageSystemSettingsPage() {
		misc.info("Verify going to Manage SystemSettings page went okay.");
		misc.checkForErrorMessages("verifyManageSystemSettingsPage");
		if(!selenium.isElementPresent(manageSystemSettingsPageHeaderLocator)) {
			fail("Could not find the header for 'Manage SystemSettings'.");
		}
	}

	public SystemSettings getSystemSettings() {
		verifySystemSettingsPage();
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
		ss.setDefaultVendorContext(getSelectedDefaultVendor());
		ss.setWebSiteAddress(getWebSiteAddress());
		return ss;
	}

	public String getWebSiteAddress() {
		String result = null;
		if(selenium.isElementPresent(webSiteAddressTextFieldLocator)) {
			result = selenium.getValue(webSiteAddressTextFieldLocator);
		}
		return result;
	}

	public String getEmbeddedSignInCode() {
		return selenium.getText(embeddedSignInCodeLocator);
	}

	public String getYourSignInURL() {
		return selenium.getText(yourSigninURLLocator);
	}

	public String getYourCurrentPlan() {
		return selenium.getText(yourCurrentPlanLocator);
	}

	public String getCompanyID() {
		return selenium.getText(companyIDLocator);
	}

	public String getSelectedDefaultVendor() {
		return selenium.getSelectedLabel(defaultVendorContextSelectListLocator);
	}

	public String getSelectedPreferredDateFormat() {
		return selenium.getSelectedValue(preferredDateFormatSelectListLocator);
	}

	public long getAssetsMaximum() {
		long result = -1;
		String s = selenium.getText(maximumAssetsLocator);
		s = s.replaceFirst("[^ ]* of ", "");		// x of y - > y
		if(!s.equals("Unlimited")) {
			result = Long.parseLong(s);
		}
		return result;
	}

	public long getAssetsCurrent() {
		long result = 0;
		String s = selenium.getText(currentAssetsLocator);
		s = s.replaceFirst(" of .*", "");		// x of y - > x
		result = Long.parseLong(s);
		return result;
	}

	public long getEmployeeAccountsMaximum() {
		long result = -1;	// unlimited
		String s = selenium.getText(currentEmployeeAccountsLocator);
		s = s.replaceFirst("[^ ]* of (.*)", "$1");	// x of y -> y
		if(!s.equals("Unlimited")) {
			result = Long.parseLong(s);
		}
		return result;
	}

	public long getEmployeeAccountsCurrent() {
		long result = 1;
		String s = selenium.getText(maximumEmployeeAccountsLocator);
		s = s.replaceFirst(" of [0-9]*", "");			// x of y -> x
		result = Long.parseLong(s);
		return result;
	}

	public long getDiskSpaceMaximum() {
		long result = -1;	// unlimited
		long mb = 1024;
		String s = selenium.getText(maximumDiskUsageLocator);
		s = s.replaceFirst("[^ ]* bytes of ([^ ]*) .*", "$1");	// x bytes of y MB -> y
		if(!s.equals("Unlimited")) {
			result = Long.parseLong(s) * mb;					// y = y * mb
		}
		return result;
	}

	public long getDiskSpaceCurrent() {
		long result = 0;
		String s = selenium.getText(currentDiskUsageLocator);
		s = s.replaceFirst(" bytes of .*", "");					// x bytes of y MB -> x
		result = Long.parseLong(s);
		return result;
	}

	private void verifySystemSettingsPage() {
		assertTrue("Could not find 'Company ID'", selenium.isElementPresent(companyIDLocator));
		assertTrue("Could not find 'Your current plan'", selenium.isElementPresent(yourCurrentPlanLocator));
		assertTrue("Could not find current disk space used", selenium.isElementPresent(currentDiskUsageLocator));
		assertTrue("Could not find maximum disk space usable", selenium.isElementPresent(maximumDiskUsageLocator));
		assertTrue("Could not find current employee accounts", selenium.isElementPresent(currentEmployeeAccountsLocator));
		assertTrue("Could not find maximum employee accounts", selenium.isElementPresent(maximumEmployeeAccountsLocator));
		assertTrue("Could not find current assets", selenium.isElementPresent(currentAssetsLocator));
		assertTrue("Could not find maximum assets", selenium.isElementPresent(maximumAssetsLocator));
		assertTrue("Could not find 'Your Sign In URL'", selenium.isElementPresent(yourSigninURLLocator));
		assertTrue("Could not find 'Embedded Sign In Code'", selenium.isElementPresent(embeddedSignInCodeLocator));
		assertTrue("Could not find 'Preferred Date Format'", selenium.isElementPresent(preferredDateFormatSelectListLocator));
		assertTrue("Could not find 'Default Vendor Context'", selenium.isElementPresent(defaultVendorContextSelectListLocator));
		assertTrue("Could not find 'Save'", selenium.isElementPresent(saveButtonLocator));
		assertTrue("Could not find 'Cancel'", selenium.isElementPresent(cancelLinkLocator));
	}
}
