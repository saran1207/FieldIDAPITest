package com.n4systems.fieldid.selenium.pages.search;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.thoughtworks.selenium.Selenium;

public class SaveReportForm extends FieldIDPage {

	public SaveReportForm(Selenium selenium) {
		super(selenium);
		if(!checkOnSaveReportForm()){
			fail("Expected to be on save report form page!");
		}
	}

	private boolean checkOnSaveReportForm() {
		return selenium.isElementPresent("//form[@id='savedReportCreate']");
	}
	
	public void setReportName(String name) {
		selenium.type("//input[@id='savedReportCreate_name']", name);
	}

	public ReportingPage clickSave() {
		selenium.click("//input[@id='savedReportCreate_label_save']");
		return new ReportingPage(selenium);
	}

	public ReportingPage clickCancel() {
		selenium.click("//a[contains(.,'Cancel')]");
		return new ReportingPage(selenium);
	}
}
