package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.pages.schedules.SchedulesSearchResultsPage;
import com.thoughtworks.selenium.Selenium;

public class SchedulesSearchPage extends EntitySearchPage<SchedulesSearchResultsPage> {

    public SchedulesSearchPage(Selenium selenium) {
        super(selenium, SchedulesSearchResultsPage.class);
    }

}
