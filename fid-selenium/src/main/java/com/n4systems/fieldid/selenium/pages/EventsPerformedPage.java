package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.components.EventInfoPopup;
import com.thoughtworks.selenium.Selenium;

public class EventsPerformedPage extends FieldIDPage {

	public EventsPerformedPage(Selenium selenium) {
		super(selenium);
	}

	public ManageEventsPage clickManageEvents() {
		selenium.click("//button[contains(.,'manage events')]");
		return new ManageEventsPage(selenium);
	}

    public EventInfoPopup clickViewLatestEvent() {
        return clickViewEventNumber(getNumEvents());
    }

    public EventInfoPopup clickViewEventNumber(int eventIndex) {
        selenium.click("//table[@class='list']/tbody/tr["+(eventIndex+1)+"]//a[contains(.,'view')]");
        return new EventInfoPopup(selenium);
    }

    public int getNumEvents() {
        return selenium.getXpathCount("//table[@class='list']/tbody/tr").intValue() - 1;
    }

}
