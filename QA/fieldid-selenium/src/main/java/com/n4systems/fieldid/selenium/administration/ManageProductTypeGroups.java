package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import org.junit.Assert;

public class ManageProductTypeGroups extends Assert {
	Selenium selenium;
	Misc misc;
	private String manageProductTypeGroupsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Product Type Groups')]";
	
	public ManageProductTypeGroups(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageProductTypeGroupsPage() {
		misc.info("Verify going to Manage Product Type Groups page went okay.");
		misc.checkForErrorMessages("verifyManageProductTypeGroupsPage");
		if(!selenium.isElementPresent(manageProductTypeGroupsPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Product Type Groups'.");
		}
	}
}
