package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ManageCommentTemplates {
	FieldIdSelenium selenium;
	MiscDriver misc;
	private String manageCommentTemplatesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Comment Templates')]";
	
	public ManageCommentTemplates(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyManageCommentTemplatesPage() {
		misc.checkForErrorMessages("verifyManageCommentTemplatesPage");
		if(!selenium.isElementPresent(manageCommentTemplatesPageHeaderLocator)) {
			fail("Could not find the header for 'Manage Comment Templates'.");
		}
	}
}
