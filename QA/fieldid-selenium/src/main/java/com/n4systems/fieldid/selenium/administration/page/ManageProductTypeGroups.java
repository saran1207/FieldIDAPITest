package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;

public class ManageProductTypeGroups {
	FieldIdSelenium selenium;
	Misc misc;
	private String manageProductTypeGroupsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Product Type Groups')]";
	
	public ManageProductTypeGroups(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageProductTypeGroupsPage() {
		misc.checkForErrorMessages("verifyManageProductTypeGroupsPage");
		if(!selenium.isElementPresent(manageProductTypeGroupsPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Product Type Groups'.");
		}
	}
}
