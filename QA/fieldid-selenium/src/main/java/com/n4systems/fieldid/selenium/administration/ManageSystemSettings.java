package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.*;

public class ManageSystemSettings {
	Selenium selenium;
	Misc misc;
	private String manageSystemSettingsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage System Settings')]";
	
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
}
