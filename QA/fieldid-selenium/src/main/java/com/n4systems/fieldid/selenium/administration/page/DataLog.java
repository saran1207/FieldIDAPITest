package com.n4systems.fieldid.selenium.administration.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class DataLog {
	FieldIdSelenium selenium;
	MiscDriver misc;
	private String dataLogPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Data Log')]";
	
	public DataLog(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyDataLogPage() {
		misc.checkForErrorMessages("verifyDataLogPage");
		if(!selenium.isElementPresent(dataLogPageHeaderLocator)) {
			fail("Could not find the header for 'Data Log'.");
		}
	}
}
