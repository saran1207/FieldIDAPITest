package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class ManageEventsPage extends FieldIDPage {

	public ManageEventsPage(Selenium selenium) {
		super(selenium);
	}
	
	public EventPage clickFirstEventLink() {
		selenium.click("//span[@class='eventType']/a");
		return new EventPage(selenium);
	}

	public EventPage clickStartNewEvent(String eventType) {
		selenium.click("//a[@id='startEvent']");
		waitForPageToLoad();
		selenium.click("//a[contains(., '" + eventType + "')]");
		return new EventPage(selenium);
	}

}
