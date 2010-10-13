package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class InspectionsPerformedPage extends FieldIDPage {

	public InspectionsPerformedPage(Selenium selenium) {
		super(selenium);
	}

	public ManageInspectionsPage clickManageInspections() {
		selenium.click("//div/button");
		return new ManageInspectionsPage(selenium);
	}
}
