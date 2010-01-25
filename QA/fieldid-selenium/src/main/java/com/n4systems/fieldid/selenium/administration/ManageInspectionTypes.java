package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.*;

public class ManageInspectionTypes {
	Selenium selenium;
	Misc misc;
	private String manageInspectionTypesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Inspection Types')]";
	
	public ManageInspectionTypes(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageInspectionTypesPage() {
		misc.info("Verify going to Manage Inspection Types page went okay.");
		misc.checkForErrorMessages("verifyManageInspectionTypesPage");
		if(!selenium.isElementPresent(manageInspectionTypesPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Inspection Types'.");
		}
	}
}
