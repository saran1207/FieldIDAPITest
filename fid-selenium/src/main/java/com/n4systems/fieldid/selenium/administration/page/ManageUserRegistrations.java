package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ManageUserRegistrations {
	FieldIdSelenium selenium;
	MiscDriver misc;
	private String manageUserRegistrationsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage User Registrations')]";
	
	public ManageUserRegistrations(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageUserRegistrationsPage() {
		misc.checkForErrorMessages("verifyManageUserRegistrationsPage");
		if(!selenium.isElementPresent(manageUserRegistrationsPageHeaderLocator)) {
			fail("Could not find the header for 'Manage User Registrations'.");
		}
	}
}
