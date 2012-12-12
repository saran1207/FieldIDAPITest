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

    public void selectAssign() {
        selenium.click("//input[@value='Assign']");
        waitForWicketAjax();
        selenium.click("//input[@value='Next']");
        waitForElementToBePresent("//label[.='Assignee']");
    }

    public void selectClose() {
        selenium.click("//input[@value='Close']");
        waitForWicketAjax();
        selenium.click("//input[@value='Next']");
        waitForElementToBePresent("//label[.='Closed By']");
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

    public void enterNextEventDate(String date) {
        selenium.type("//input[@name='nextEventDate:dateField']", date);
        selenium.check("//input[@name='nextEventDateCheck']");
    }

    public void saveEditDetails() {
        selenium.click("//input[@value='Next']");
        waitForElementToBePresent("//form[@class='confirmEditForm']");
    }

    public void selectAssignee(String assignee) {
        selenium.select("//select[@name='assignee']", assignee);
    }

    public void clickAssign() {
        selenium.click("//input[@value='Assign']");
        waitForElementToBePresent("//form[@class='confirmEditForm']");
    }
    
    public void selectClosedBy(String resolver) {
        selenium.select("//select[@name='resolver']", resolver);
    }

    public void clickCloseEvent() {
        selenium.click("//input[@value='Close Event']");
        waitForElementToBePresent("//form[@class='confirmCloseForm']");
    }

    public ReportingPage clickPerformMassUpdate() {
        selenium.click("//input[@value='Perform Mass Update']");
        return new ReportingPage(selenium);
    }
}
