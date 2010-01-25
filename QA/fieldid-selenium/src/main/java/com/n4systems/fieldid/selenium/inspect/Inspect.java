package com.n4systems.fieldid.selenium.inspect;

import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

public class Inspect {
	Selenium selenium;
	Misc misc;
	
	// Locators
	private String inspectHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Inspect - ')]";

	public Inspect(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	/**
	 * Checks to see if there are any error messages on the page and checks
	 * for the header containing "Inspect - ${serialNumber}" on the page. Does
	 * not check the dynamic contents of the Inspect page.
	 * 
	 * @param serialNumber 
	 */
	public void verifyInspectPage(String serialNumber) {
		misc.info("Verify going to Inspect page went okay.");
		misc.checkForErrorMessages("verifyInspectPage");
		verifyInspectPageHeader(serialNumber);
	}

	/**
	 * Checks to see if the Asset - ${serialNumber}" header exists.
	 * 
	 * @param serialNumber
	 */
	private void verifyInspectPageHeader(String serialNumber) {
		if(!(selenium.isElementPresent(inspectHeaderLocator) && selenium.getText(inspectHeaderLocator).contains(serialNumber))) {
			fail("Could not find the header for 'Inspect - " + serialNumber + "'.");
		}
	}
}
