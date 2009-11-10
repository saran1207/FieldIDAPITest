package com.n4systems.fieldid.selenium.home;

import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.SeleneseTestBase;
import com.thoughtworks.selenium.Selenium;

public class HomePage extends SeleneseTestBase {

	private static final String signInUserNameButtonLocator = "css=#signInButton";
	private static final String signInWithSecurityRFIDNumberButtonLocator = "css=#signInWithSecurityButton";
	private static final String stringForPageTitle = "Field ID : Safety Management - Home";

	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public HomePage(Selenium selenium) {
		assertTrue("Instance of Selenium is null", selenium != null);
		this.selenium = selenium;
	}
	
	/**
	 * Click the sign in button for signing in with User Name and Password.
	 * If the user name and password are not set, this will generate an
	 * error. Assumes you are on the Login page. This is part of the HomePage
	 * class because it validates it arrived at the correct page.
	 */
	public void gotoSignInUserName() {
		assertTrue("Could not find the Sign In button", selenium.isElementPresent(signInUserNameButtonLocator));
		selenium.click(signInUserNameButtonLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}
	
	/**
	 * Click the sign in button for signing in with Security RFID Number.
	 * If the Security RFID Number is not set, this will generate an
	 * error. Assumes you are on the Login page. This is part of the HomePage
	 * class because it validates it arrived at the correct page.
	 */
	public void gotoSignInSecurityRFIDNumber() {
		assertTrue("Could not find the Sign In button", selenium.isElementPresent(signInWithSecurityRFIDNumberButtonLocator));
		selenium.click(signInWithSecurityRFIDNumberButtonLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}

	private void assertPageTitle() {
		assertTrue(selenium.getTitle().startsWith(stringForPageTitle));
	}
}
