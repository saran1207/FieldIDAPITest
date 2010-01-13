package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import org.junit.Assert;

public class ManageProductTypes extends Assert {
	Selenium selenium;
	Misc misc;
	private String manageProductTypesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Product Types')]";
	
	public ManageProductTypes(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageProductTypesPage() {
		misc.info("Verify going to Manage Product Types page went okay.");
		misc.checkForErrorMessages("verifyManageProductTypesPage");
		if(!selenium.isElementPresent(manageProductTypesPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Product Types'.");
		}
	}
}
