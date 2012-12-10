package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.components.EventViewPage;
import com.thoughtworks.selenium.Selenium;

import java.util.List;

public class EventsPerformedPage extends FieldIDPage {

	public EventsPerformedPage(Selenium selenium) {
		super(selenium);
	}
	
	public EventPage clickStartEventWithOnlyOneEventType() {
		selenium.click("//div[@class='actionButtons']/a[contains(., 'Start Event')]");
		return new EventPage(selenium);
	}
	
	public QuickEventPage clickStartEventWithMultipleEventTypes() {
		selenium.click("//a[@id='startEvent']");
		return new QuickEventPage(selenium);
	}

    public EventViewPage clickViewLatestEvent() {
        return clickViewEventNumber(getNumEvents());
    }

    public EventViewPage clickViewEventNumber(int eventIndex) {
        selenium.click("//table[contains(@class, 'list')]/tbody/tr["+(eventIndex)+"]//a[contains(.,'View')]");
        return new EventViewPage(selenium);
    }

    public int getNumEvents() {
        return selenium.getXpathCount("//table[contains(@class, 'list')]/tbody/tr").intValue();
    }
    
    public List<String> getEventTypes() {
    	return collectTableValuesUnderCellForCurrentPage(1, 5, "");
    }
    
    public void clickSortColumn(String columnName) {
    	selenium.click("//th//a[.='" + columnName + "']");
    	waitForPageToLoad();
    }

}
