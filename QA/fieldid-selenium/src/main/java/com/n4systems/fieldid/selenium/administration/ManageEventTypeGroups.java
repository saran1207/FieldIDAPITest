package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import org.junit.Assert;

public class ManageEventTypeGroups extends Assert {
	Selenium selenium;
	Misc misc;
	private String manageEventTypeGroupsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Event Type Groups')]";
	
	public ManageEventTypeGroups(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageEventTypeGroupsPage() {
		misc.info("Verify going to Manage Event Type Groups page went okay.");
		misc.checkForErrorMessages("verifyManageEventTypeGroupsPage");
		if(!selenium.isElementPresent(manageEventTypeGroupsPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Event Type Groups'.");
		}
	}
}
