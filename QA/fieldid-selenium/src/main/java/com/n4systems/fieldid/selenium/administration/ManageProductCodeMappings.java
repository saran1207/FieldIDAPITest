package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import org.junit.Assert;

public class ManageProductCodeMappings extends Assert {
	Selenium selenium;
	Misc misc;
	private String manageProductCodeMappingsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Product Code Mappings')]";
	
	public ManageProductCodeMappings(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageProductCodeMappingsPage() {
		misc.info("Verify going to Manage Product Code Mappings page went okay.");
		misc.checkForErrorMessages("verifyManageProductCodeMappingsPage");
		if(!selenium.isElementPresent(manageProductCodeMappingsPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Product Code Mappings'.");
		}
	}
}
