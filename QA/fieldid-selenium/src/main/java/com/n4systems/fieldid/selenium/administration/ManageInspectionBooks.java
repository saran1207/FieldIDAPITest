package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import org.junit.Assert;

public class ManageInspectionBooks extends Assert {
	Selenium selenium;
	Misc misc;
	private String manageInspectionBooksPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Inspection Books')]";
	
	public ManageInspectionBooks(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageInspectionBooksPage() {
		misc.info("Verify going to Manage Inspection Books page went okay.");
		misc.checkForErrorMessages("verifyManageInspectionBooksPage");
		if(!selenium.isElementPresent(manageInspectionBooksPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Inspection Books'.");
		}
	}
}
