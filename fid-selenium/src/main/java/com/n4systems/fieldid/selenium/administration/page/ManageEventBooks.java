package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ManageEventBooks {
	FieldIdSelenium selenium;
	MiscDriver misc;
	private String manageEventBooksPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Event Books')]";
	
	public ManageEventBooks(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageEventBooksPage() {
		misc.checkForErrorMessages("verifyManageEventBooksPage");
		if(!selenium.isElementPresent(manageEventBooksPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Event Books'.");
		}
	}
}
