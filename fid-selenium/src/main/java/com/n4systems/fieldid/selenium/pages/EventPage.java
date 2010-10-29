package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class EventPage extends FieldIDPage {

	public EventPage(Selenium selenium) {
		super(selenium);
		if (!checkOnEventPage()) {
			fail("Expected to be on event page!");
		}
	}

	public boolean checkOnEventPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Event -')]") 
			|| selenium.isElementPresent("//form[@id='inspectionCreate']") 
			|| selenium.isElementPresent("//form[@id='eventUpdate']");
	}

	public void clickAssetInformationTab() {
		clickNavOption("Asset Information");
	}

	public void clickMandatoryEventToPerformLink() {
		selenium.click("//a[@class='exitLink']");
		waitForPageToLoad();
	}

	public void clickStore() {
		selenium.click("//input[@value='Store']");
		waitForPageToLoad();
	}

	public void clickSave() {
		selenium.click("//input[@id='subAssetForm_label_save']");
		waitForPageToLoad();
	}

	public void clickSubStartEventLink() {
		selenium.click("//a[contains(.,'Start Event')]");
		selenium.click("//a[contains(.,'Bridge - Short')]");
	}

}
