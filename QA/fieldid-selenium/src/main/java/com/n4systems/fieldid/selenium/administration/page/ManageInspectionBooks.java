package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ManageInspectionBooks {
	FieldIdSelenium selenium;
	MiscDriver misc;
	private String manageInspectionBooksPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Inspection Books')]";
	
	public ManageInspectionBooks(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageInspectionBooksPage() {
		misc.checkForErrorMessages("verifyManageInspectionBooksPage");
		if(!selenium.isElementPresent(manageInspectionBooksPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Inspection Books'.");
		}
	}
}
