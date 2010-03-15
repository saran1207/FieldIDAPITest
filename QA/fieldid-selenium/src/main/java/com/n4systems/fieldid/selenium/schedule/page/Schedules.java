package com.n4systems.fieldid.selenium.schedule.page;

import static org.junit.Assert.assertTrue;

import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;

public class Schedules {
	private FieldIdSelenium selenium;
	private Misc misc;
	
	// Locators
	private String schedulesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Schedule Search')]";
	private String schedulesSearchResultPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Schedule Search Results')]";

	public Schedules(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void assertSchedulesPageHeader() {
		assertTrue("Could not find the header for the Schedules page", selenium.isElementPresent(schedulesPageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
	
	public void assertSchedulesSearchResultsPageHeader() {
		assertTrue("Could not find the header for the Schedule Search Results page", selenium.isElementPresent(schedulesSearchResultPageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
}
