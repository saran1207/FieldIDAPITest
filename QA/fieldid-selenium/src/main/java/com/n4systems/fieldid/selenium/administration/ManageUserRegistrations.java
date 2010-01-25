package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.*;

public class ManageUserRegistrations {
	Selenium selenium;
	Misc misc;
	private String manageUserRegistrationsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage User Registrations')]";
	
	public ManageUserRegistrations(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageUserRegistrationsPage() {
		misc.info("Verify going to Manage User Registrations page went okay.");
		misc.checkForErrorMessages("verifyManageUserRegistrationsPage");
		if(!selenium.isElementPresent(manageUserRegistrationsPageHeaderLocator)) {
			fail("Could not find the header for 'Manage User Registrations'.");
		}
	}
}
