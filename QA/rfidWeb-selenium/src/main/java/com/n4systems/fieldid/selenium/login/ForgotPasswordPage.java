package com.n4systems.fieldid.selenium.login;

import static org.junit.Assert.assertTrue;

import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.DefaultSelenium;

public class ForgotPasswordPage {

	private DefaultSelenium selenium;
	private static final String stringForPageTitle = "Field ID : Safety Management - Forgot Password";
	private static final String usernameLocator = "css=#userName";
	private static final String forgotMyPasswordLinkLocator = "//A[contains(text(),'I forgot my password')]";
	
	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public ForgotPasswordPage(DefaultSelenium selenium) {
		assertTrue("Instance of Selenium is null", selenium != null);
		this.selenium = selenium;
	}
	
	private void assertPageTitle() {
		assertTrue(selenium.getTitle().equals(stringForPageTitle));
	}

	/**
	 * Enter text into the User Name field.
	 * 
	 * @param username
	 */
	public void setUserName(String username) {
		assertTrue("User name cannot be null", username != null);
		assertTrue("Could not find the User Name text field", selenium.isElementPresent(usernameLocator));
		selenium.type(usernameLocator, username);
	}

	/**
	 * Clicks on the link for I forgot my password from the Login page.
	 */
	public void gotoIForgotMyPassword() {
		assertTrue("Could not find the link to I forgot my password", selenium.isElementPresent(forgotMyPasswordLinkLocator));
		selenium.click(forgotMyPasswordLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}
}
