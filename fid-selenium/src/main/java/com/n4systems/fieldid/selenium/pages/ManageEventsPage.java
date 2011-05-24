package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class ManageEventsPage extends FieldIDPage {

	public ManageEventsPage(Selenium selenium) {
		super(selenium);
		if (!checkOnMassEventPage()) {
			fail("Expected to be on mass event page!");
		}
	}
	
	public boolean checkOnMassEventPage(){
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Mass Event')]");
	}

	public EventPage clickFirstEventLink() {
		selenium.click("//span[@class='eventType']/a");
		return new EventPage(selenium);
	}

	public EventPage clickStartNewEvent(String eventType) {
		selenium.click("//a[contains(., 'Start New Event')]");
		waitForPageToLoad();
		selenium.click("//a[contains(., '" + eventType + "')]");
		return new EventPage(selenium);
	}

}
