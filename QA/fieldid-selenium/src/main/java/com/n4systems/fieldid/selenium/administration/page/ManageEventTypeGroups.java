package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ManageEventTypeGroups {
	FieldIdSelenium selenium;
	MiscDriver misc;
	private String manageEventTypeGroupsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Event Type Groups')]";
	
	public ManageEventTypeGroups(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageEventTypeGroupsPage() {
		misc.checkForErrorMessages("verifyManageEventTypeGroupsPage");
		if(!selenium.isElementPresent(manageEventTypeGroupsPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Event Type Groups'.");
		}
	}
}
