package com.n4systems.fieldid.selenium.home;

import static org.junit.Assert.assertTrue;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;


public class Home {

	private Selenium selenium;
	private Misc misc;
	
	// Locators
	private String homePageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Home')]";
	

	public Home(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void verifyHomePageHeader() {
		assertTrue("Could not find the header for the Home page", selenium.isElementPresent(homePageHeaderLocator));
		misc.checkForErrorMessages(null);
	}
}
