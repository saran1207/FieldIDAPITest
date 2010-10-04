package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class InspectPage extends FieldIDPage {

	public InspectPage(Selenium selenium) {
		super(selenium);
		if(!checkOnInspectPage()){
			fail("Expected to be on asset page!");
		}
	}

	public boolean checkOnInspectPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Inspect')]");
	}

}
