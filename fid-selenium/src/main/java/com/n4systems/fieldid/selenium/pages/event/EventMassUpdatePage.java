package com.n4systems.fieldid.selenium.pages.event;

import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.n4systems.fieldid.selenium.pages.WicketFieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class EventMassUpdatePage extends WicketFieldIDPage {

	public EventMassUpdatePage(Selenium selenium) {
        super(selenium);
	}

    public void selectEdit() {
    	selenium.click("//input[@value='Edit']");
        waitForWicketAjax();
    	selenium.click("//input[@value='Next']");
    	waitForElementToBePresent("//form[@class='editForm']");
    }
	public void selectDelete() {
    	selenium.click("//input[@value='Delete']");
        waitForWicketAjax();
    	selenium.click("//input[@value='Next']");
    	waitForElementToBePresent("//div[@class='deleteDetails']");
    }

    public void saveDeleteDetails() {
        selenium.click("//input[@value='Next']");
        waitForElementToBePresent("//form[@class='confirmDeleteForm']");
    }

    public ReportingPage clickConfirmDelete() {
        selenium.type("//input[@name='confirmationField']", "delete");
        selenium.fireEvent("//input[@name='confirmationField']", "keyup");
        waitForWicketAjax();
        selenium.click("//input[@value='Delete']");
        return new ReportingPage(selenium);
    }
}
