package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.fail;

public class QuickEventPage extends FieldIDPage {

    public QuickEventPage(Selenium selenium) {
        super(selenium);
        verifyOnQuickEventPage();
    }

    public void verifyOnQuickEventPage() {
        checkForErrorMessages(null);
        if (!selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(.,'Quick Event')]")) {
            fail("Expected to be on quick event page");
        }
    }


}
