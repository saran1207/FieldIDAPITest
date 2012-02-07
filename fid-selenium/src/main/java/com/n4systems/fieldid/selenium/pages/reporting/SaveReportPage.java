package com.n4systems.fieldid.selenium.pages.reporting;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.fail;

public class SaveReportPage extends FieldIDPage {

	public SaveReportPage(Selenium selenium) {
		super(selenium);
		if(!checkOnSaveReportForm()){
			fail("Expected to be on save report form page!");
		}
	}

	private boolean checkOnSaveReportForm() {
		return selenium.isElementPresent("//h2[.='Saved Report Details']");
	}
	
	public void setReportName(String name) {
		selenium.type("//input[@name='name']", name);
	}

	public ReportingPage clickSave() {
		selenium.click("//input[@value='Save']");
		return new ReportingPage(selenium);
	}

	public ReportingPage clickCancel() {
		selenium.click("//a[contains(.,'Cancel')]");
		return new ReportingPage(selenium);
	}
}
