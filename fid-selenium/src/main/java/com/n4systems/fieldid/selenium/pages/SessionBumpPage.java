package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.assertTrue;

public class SessionBumpPage extends FieldIDPage {

    public SessionBumpPage(Selenium selenium) {
        this(selenium, true);
    }

    public SessionBumpPage(Selenium selenium, boolean waitForPageToLoad) {
        super(selenium, waitForPageToLoad);
        assertTrue("not confirm page.", selenium.isElementPresent("css=#signInConfirm"));
    }

    public HomePage confirmKickingSession() {
        selenium.click("kickOtherUserOut");
        return new HomePage(selenium);
    }

}
