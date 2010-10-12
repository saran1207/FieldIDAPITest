package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.pages.reporting.ReportingSearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class ReportingPage extends EntitySearchPage<ReportingSearchResultsPage> {

    public ReportingPage(Selenium selenium) {
        super(selenium, ReportingSearchResultsPage.class);
    }

}
