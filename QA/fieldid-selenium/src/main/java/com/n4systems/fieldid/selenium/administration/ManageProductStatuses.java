package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.*;

public class ManageProductStatuses {
	Selenium selenium;
	Misc misc;
	private String manageProductStatusesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Product Statuses')]";
	
	public ManageProductStatuses(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageProductStatusesPage() {
		misc.info("Verify going to Manage Product Statuses page went okay.");
		misc.checkForErrorMessages("verifyManageProductStatusesPage");
		if(!selenium.isElementPresent(manageProductStatusesPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Product Statuses'.");
		}
	}
}
