package com.n4systems.fieldid.selenium.pages.schedules;

import static org.junit.Assert.assertTrue;

import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class Schedules {
	private FieldIdSelenium selenium;
	private MiscDriver misc;
	
	// Locators
	private String schedulesPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Schedule Search')]";
	private String schedulesSearchResultPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Schedule Search Results')]";

	public Schedules(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	
	public void changeScheduleStatus(String statusName) {
		selenium.select("reportForm_criteria_status", "label=" + statusName);
	}
	
	public void assertSchedulesPageHeader() {
		assertTrue("Could not find the header for the Schedules page", selenium.isElementPresent(schedulesPageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
	
	public void assertSchedulesSearchResultsPageHeader() {
		assertTrue("Could not find the header for the Schedule Search Results page", selenium.isElementPresent(schedulesSearchResultPageHeaderLocator));
		misc.checkForErrorMessages(null);
	}

	public void runSchedules() {
		selenium.clickAndWaitForPageLoad("css=#reportForm_label_Run");
	}

	public int totalResults() {
		String reportTotal = selenium.getText("css=.total");
		return Integer.valueOf(reportTotal.trim().replace("Total Scheduled Inspections", "").trim());
	}
}
