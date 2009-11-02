package com.n4systems.fieldid.selenium.login;

import static org.junit.Assert.assertTrue;

import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.DefaultSelenium;

public class SendPasswordPage {

	private DefaultSelenium selenium;
	private static final String stringForPageTitle = "Field ID : Safety Management - Password Reset Email Sent";
	private static final String resetPasswordButtonLocator = "css=#sendPassword_label_reset_password";
	
	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public SendPasswordPage(DefaultSelenium selenium) {
		assertTrue("Instance of Selenium is null", selenium != null);
		this.selenium = selenium;
	}
	
	/**
	 * This method clicks the Reset Password button the Forgot Password page.
	 * It assumes you have gotten to the Forgot Password page. Even though the
	 * link is on the Forgot Password page, we put the code in here because
	 * once we click the button we need the assertPageTitle here to confirm
	 * we arrived at the correct page.
	 */
	public void gotoResetPassword() {
		assertTrue("Could not find the Reset Password button", selenium.isElementPresent(resetPasswordButtonLocator));
		selenium.click(resetPasswordButtonLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}
	
	private void assertPageTitle() {
		assertTrue(selenium.getTitle().equals(stringForPageTitle));
	}
}
