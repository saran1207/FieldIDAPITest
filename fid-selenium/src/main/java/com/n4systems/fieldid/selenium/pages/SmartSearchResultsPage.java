package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class SmartSearchResultsPage extends FieldIDPage {

	public SmartSearchResultsPage(Selenium selenium) {
		super(selenium);
		if (!checkOnSmartSearchResultsPage()) {
			fail("Expected to be on Smart Search Results Page!");
		}
	}

	public boolean checkOnSmartSearchResultsPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='results']/h2[contains(text(),'We found multiple assets, select the one you want')]");
	}

	public boolean checkNumberOfPages(int numberOfPages) {
		return selenium.isElementPresent("//div[@id='results']/div[1]/ul/li['" + numberOfPages + "']/a");
	}

	public SmartSearchResultsPage clickNextLink() {
		checkForErrorMessages(null);
		selenium.click("//div[@id='results']/div[1]/ul/li[3]/a");
		return new SmartSearchResultsPage(selenium);
	}
}
