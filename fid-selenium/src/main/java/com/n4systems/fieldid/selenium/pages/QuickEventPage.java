package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.*;

import com.thoughtworks.selenium.Selenium;

public class QuickEventPage extends FieldIDPage {

    public QuickEventPage(Selenium selenium) {
        super(selenium);
        verifyOnQuickEventPage();
    }

    public void verifyOnQuickEventPage() {
        checkForErrorMessages(null);
        if (!selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(.,'Perform an Event on ')]")) {
            fail("Expected to be on quick event page");
        }
    }
    
    public EventPage clickAdHocEventType(String eventTypeName) {
    	selenium.click("//a[contains(., '" + eventTypeName + "')]");
    	return new EventPage(selenium);
    }

}
