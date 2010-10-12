package com.n4systems.fieldid.selenium.pages.schedules;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class SchedulesSearchResultsPage extends FieldIDPage {

    public SchedulesSearchResultsPage(Selenium selenium) {
        super(selenium);
    }

    public SchedulesMassUpdatePage clickMassUpdate() {
        selenium.click("//a[.='Mass Update']");
        return new SchedulesMassUpdatePage(selenium);
    }

    public int getTotalResultsCount() {
        return selenium.getXpathCount("//table[@id='resultsTable']//tr").intValue() - 1;
    }

    public String getScheduledDateForResult(int resultNumber) {
        int rowNumber = resultNumber + 1;
        return selenium.getText("//table[@id='resultsTable']//tr["+rowNumber+"]/td[contains(@id, 'nextdate')]");
    }

}
