package com.n4systems.fieldid.selenium.inspect.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class InspectPage {
	FieldIdSelenium selenium;
	MiscDriver misc;
	
	// Locators
	private String inspectHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Inspect - ')]";

	public InspectPage(FieldIdSelenium selenium, MiscDriver misc) {
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
