package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;

public class ManageInspectionTypes {
	FieldIdSelenium selenium;
	Misc misc;
	private String manageInspectionTypesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Inspection Types')]";
	
	public ManageInspectionTypes(FieldIdSelenium selenium, Misc misc) {
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
