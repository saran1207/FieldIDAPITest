package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class EventsPerformedPage extends FieldIDPage {

	public EventsPerformedPage(Selenium selenium) {
		super(selenium);
	}

	public ManageEventsPage clickManageEvents() {
		selenium.click("//div/button");
		return new ManageEventsPage(selenium);
	}
}
