package com.n4systems.fieldid.selenium.reporting.page;

import static org.junit.Assert.assertTrue;

import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;

public class Reporting {
	private FieldIdSelenium selenium;
	private Misc misc;
	
	// Locators
	private String reportingPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Reporting')]";
	private String reportingResultPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Reporting Results')]";

	public Reporting(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void assertReportingPageHeader() {
		assertTrue("Could not find the header for the Reporting page", selenium.isElementPresent(reportingPageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
	
	public void assertReportingSearchResultsPageHeader() {
		assertTrue("Could not find the header for the Reporting Results page", selenium.isElementPresent(reportingResultPageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
}
