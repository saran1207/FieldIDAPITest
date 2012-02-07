package com.n4systems.fieldid.selenium.pages.schedules;

import com.n4systems.fieldid.selenium.pages.MassUpdatePage;
import com.n4systems.fieldid.selenium.pages.SchedulesSearchPage;
import com.thoughtworks.selenium.Selenium;

public class SchedulesMassUpdatePage extends MassUpdatePage<SchedulesSearchPage> {

    public SchedulesMassUpdatePage(Selenium selenium) {
        super(selenium, SchedulesSearchPage.class);
    }

    public void enterNextEventDate(String date) {
        selenium.type("//input[@id='input_nextDate']", date);
    }

    @Override
    protected String getSubmitButtonLabel() {
        return "Perform Mass Update";
    }
}
