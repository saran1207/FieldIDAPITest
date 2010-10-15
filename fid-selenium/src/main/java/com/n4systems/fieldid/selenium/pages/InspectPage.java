package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class InspectPage extends FieldIDPage {

	public InspectPage(Selenium selenium) {
		super(selenium);
		if (!checkOnInspectPage()) {
			fail("Expected to be on inpect page!");
		}
	}

	public boolean checkOnInspectPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Inspect')]") || selenium.isElementPresent("//form[@id='inspectionCreate']");
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
		selenium.click("//input[@id='subProductForm_label_save']");
		waitForPageToLoad();
	}

	public void clickSubStartEventLink() {
		selenium.click("//a[contains(.,'Start Event')]");
		selenium.click("//a[contains(.,'Bridge - Short')]");
	}

}
