package com.n4systems.fieldid.selenium.home.page;

import static org.junit.Assert.assertTrue;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;


public class Home {

	private FieldIdSelenium selenium;
	private Misc misc;
	
	// Locators
	private String homePageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Home')]";
	

	public Home(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void verifyHomePageHeader() {
		assertTrue("Could not find the header for the Home page", selenium.isElementPresent(homePageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
}
