package com.n4systems.fieldid.selenium.pages.schedules;

import com.n4systems.fieldid.selenium.pages.MassUpdatePage;
import com.thoughtworks.selenium.Selenium;

public class SchedulesMassUpdatePage extends MassUpdatePage<SchedulesSearchResultsPage> {

    public SchedulesMassUpdatePage(Selenium selenium) {
        super(selenium, SchedulesSearchResultsPage.class);
    }

    public void enterNextEventDate(String date) {
        selenium.type("//input[@id='input_nextDate']", date);
    }

}
