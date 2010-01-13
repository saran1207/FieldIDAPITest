package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import org.junit.Assert;

public class ManageUsers extends Assert {
	Selenium selenium;
	Misc misc;
	private String manageUsersPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Users')]";
	
	public ManageUsers(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageUsersPage() {
		misc.info("Verify going to Manage Users page went okay.");
		misc.checkForErrorMessages("verifyManageUsersPage");
		if(!selenium.isElementPresent(manageUsersPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Users'.");
		}
	}
}
