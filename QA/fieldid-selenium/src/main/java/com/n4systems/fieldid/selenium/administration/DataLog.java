package com.n4systems.fieldid.selenium.administration;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.*;

public class DataLog {
	Selenium selenium;
	Misc misc;
	private String dataLogPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Data Log')]";
	
	public DataLog(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void verifyDataLogPage() {
		misc.info("Verify going to Data Log page went okay.");
		misc.checkForErrorMessages("verifyDataLogPage");
		if(!selenium.isElementPresent(dataLogPageHeaderLocator)) {
			fail("Could not find the header for 'Data Log'.");
		}
	}
}
