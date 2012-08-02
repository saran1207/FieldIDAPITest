package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.components.EventInfoPopup;
import com.thoughtworks.selenium.Selenium;

import java.util.List;

public class EventsPerformedPage extends FieldIDPage {

	public EventsPerformedPage(Selenium selenium) {
		super(selenium);
	}
	
	public EventPage clickStartEventWithOnlyOneEventType() {
		selenium.click("//a[@id='startEvent']");
		return new EventPage(selenium);
	}
	
	public QuickEventPage clickStartEventWithMultipleEventTypes() {
		selenium.click("//a[@id='startEvent']");
		return new QuickEventPage(selenium);
	}

	public ManageEventsPage clickViewEventsByDateGroup() {
		selenium.click("//a[@id='groupByDateButton']");
		return new ManageEventsPage(selenium);
	}

    public EventInfoPopup clickViewLatestEvent() {
        return clickViewEventNumber(getNumEvents());
    }

    public EventInfoPopup clickViewEventNumber(int eventIndex) {
        selenium.click("//table[@class='list']/tbody/tr["+(eventIndex+1)+"]//a[contains(.,'View')]");
        return new EventInfoPopup(selenium);
    }

    public int getNumEvents() {
        return selenium.getXpathCount("//table[@class='list']/tbody/tr").intValue() - 1;
    }
    
    public List<String> getEventTypes() {
    	return collectTableValuesUnderCellForCurrentPage(1, 5, "");
    }
    
    public void clickSortColumn(String columnName) {
    	selenium.click("//th//a[.='" + columnName + "']");
    	waitForPageToLoad();
    }

}
