package com.n4systems.fieldid.selenium.login.page;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.List;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;

public class Forgot {

	private FieldIdSelenium selenium;
	private Misc misc;
	
	// Locators
	private String userNameLocator = "xpath=//INPUT[@id='userName']";
	private String resetPasswordButtonLocator = "xpath=//INPUT[@id='sendPassword_label_reset_password']";
	private String anEmailHasBeenSentLocator = "xpath=//*[@class='actionInstructions' and contains(text(),'Thank you.') and contains(text(),'An email has been sent to you with instructions on resetting your password.')]";
	private String returnToSignInButtonLocator = "xpath=//INPUT[@value='Return to Sign In']";
	private String userNameIsRequiredErrorMessage = "User Name is required.";
	private String returnToSignInLinkLocator = "xpath=//A[contains(text(),'Return to Sign In')]";

	public Forgot(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	/**
	 * Fill in the User Name input for the Forgot Password page.
	 * 
	 * @param s - user name to type into text field.
	 */
	public void setUserName(String s) {
		misc.info("Set User Name to '" + s + "'");
		if(selenium.isElementPresent(userNameLocator)) {
			selenium.type(userNameLocator, s);
		} else {
			fail("Could not find the User Name on Forgot Password");
		}
	}
	
	/**
	 * Clicks the Reset Password button. Assumes you have already
	 * filled in a user name. You can also call this without a
	 * user name and it will give you an error message. You need
	 * to verify the reset went okay if you want to fail the test.
	 */
	public void gotoResetPassword() {
		misc.info("Click Reset Password button.");
		if(selenium.isElementPresent(resetPasswordButtonLocator)) {
			selenium.click(resetPasswordButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * This verifies that no error was presented to the user. If the
	 * username was incorrect, there is no error. The user just does
	 * not receive an email.
	 * 
	 */
	public void verifyResetPassword() {
		misc.info("Verify static text indicating an email has been sent.");
		misc.captureScreenshot("VerifyResetPassword.png");
		assertTrue(selenium.isElementPresent(anEmailHasBeenSentLocator));
		misc.info("Verify there is a button to return to Sign In");
		assertTrue(selenium.isElementPresent(returnToSignInButtonLocator));
	}

	/**
	 * After you reset the password, this can be used to the Login page.
	 */
	public void gotoReturnToSignInAfterResetPassword() {
		misc.info("Click Return to Sign In button");
		if(selenium.isElementPresent(returnToSignInButtonLocator)) {
			selenium.click(returnToSignInButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not locate the Return to Sign In button.");
		}
	}

	/**
	 * If you call gotoResetPassword, without giving a user name, this will
	 * verify the user was given the appropriate error message.
	 */
	public void verifyUserNameIsRequired() {
		misc.info("Confirm error message '" + userNameIsRequiredErrorMessage + "' appears on page.");
		misc.captureScreenshot("verifyUserNameIsRequired.png");
		List<String> errors = misc.getErrorMessages();
		if(errors.size() == 0) {
			fail("Was expecting the error message: '" + userNameIsRequiredErrorMessage  + "' but didn't get anything.");
		}
		if(!errors.contains(userNameIsRequiredErrorMessage)) {
			fail("Was expecting the error message: '" + userNameIsRequiredErrorMessage  + "' but got: " + misc.convertListToString(errors));
		}
	}

	/**
	 * If you go to Forgot Password, you can call this to return to Login Page
	 * without actually resetting the password. This will fail if we don't arrive
	 * back at the Login page.
	 */
	public void gotoReturnToSignInWithoutResettingPassword() {
		misc.info("Click link to take you back to the Sign In page.");
		if(selenium.isElementPresent(returnToSignInLinkLocator )) {
			selenium.click(returnToSignInLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Return to Sign In");
		}
	}
}
