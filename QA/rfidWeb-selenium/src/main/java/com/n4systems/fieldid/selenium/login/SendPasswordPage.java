package com.n4systems.fieldid.selenium.login;

import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.Selenium;

public class SendPasswordPage extends FieldIDTestCase {

	private Selenium selenium;
	private static final String stringForPageTitle = "Field ID : Safety Management - Password Reset Email Sent";
	private static final String resetPasswordButtonLocator = "css=#sendPassword_label_reset_password";
	private static final String messageBodyLocator = "ccs=p.actionInstructions";
	private static final CharSequence messageBodyString = "Thank you.  An email has been sent to you with instructions on resetting your password.";
	
	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public SendPasswordPage(Selenium selenium) {
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
		assertMessageBody();
	}
	
	private void assertMessageBody() {
		assertTrue("Could not find the message about an email being sent", selenium.isElementPresent(messageBodyLocator));
		String s = selenium.getText(messageBodyLocator);
		assertTrue(s.contains(messageBodyString));
	}

	private void assertPageTitle() {
		assertTrue(selenium.getTitle().equals(stringForPageTitle));
	}
}
