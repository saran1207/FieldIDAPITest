package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.reporting.page.ReportingSearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class ReportingPage extends FieldIDPage {

	public ReportingPage(Selenium selenium) {
		super(selenium);
	}

	public ReportingSearchResultsPage clickRunSearchButton() {
		selenium.click("//input[@type='submit' and @value='Run']");
		return new ReportingSearchResultsPage(selenium);
	}

}
