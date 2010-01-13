package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import org.junit.Assert;

public class ManageCommentTemplates extends Assert {
	Selenium selenium;
	Misc misc;
	private String manageCommentTemplatesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Comment Templates')]";
	
	public ManageCommentTemplates(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageCommentTemplatesPage() {
		misc.info("Verify going to Manage Comment Templates page went okay.");
		misc.checkForErrorMessages("verifyManageCommentTemplatesPage");
		if(!selenium.isElementPresent(manageCommentTemplatesPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Comment Templates'.");
		}
	}
}
