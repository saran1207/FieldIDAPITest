package com.n4systems.fieldid.selenium.pages.reporting;

import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.event.EventMassUpdatePage;
import com.n4systems.fieldid.selenium.pages.search.SearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class ReportingSearchResultsPage extends SearchResultsPage {

    public ReportingSearchResultsPage(Selenium selenium) {
        super(selenium);
    }

	public AssetPage clickReportLinkForResult(int resultNumber) {
		selenium.click("//table[@class='list']//tbody//tr[" + resultNumber + "]/td//a[@class='identifierLink']");
		return new AssetPage(selenium);
	}

	public EventMassUpdatePage clickEventMassUpdate() {
		selenium.click("//a[contains(.,'Mass Update')]");
		return new EventMassUpdatePage(selenium);
	}

}
