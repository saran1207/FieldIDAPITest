package com.n4systems.fieldid.selenium.reporting.page;

import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ReportingSearchResultsPage extends FieldIDPage {

	public ReportingSearchResultsPage(Selenium selenium) {
		super(selenium);
	}

	public AssetPage clickReportLinkForResult(int resultNumber) {
		int rowNumber = resultNumber + 1;
		selenium.click("//table[@id='resultsTable']//tr[" + rowNumber + "]/td[contains(@id, 'serialnumber')]//a");
		return new AssetPage(selenium);
	}

}
