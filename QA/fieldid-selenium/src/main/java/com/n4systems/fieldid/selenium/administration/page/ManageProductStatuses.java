package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;

public class ManageProductStatuses {
	FieldIdSelenium selenium;
	Misc misc;
	private String manageProductStatusesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Product Statuses')]";
	
	public ManageProductStatuses(FieldIdSelenium selenium, Misc misc) {
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
