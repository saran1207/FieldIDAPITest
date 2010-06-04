package com.n4systems.fieldid.selenium.login.page;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.List;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.n4systems.fieldid.selenium.testcase.LoggedInTestCase;

public class Login {
	FieldIdSelenium selenium;
	Misc misc;
	
	// Locators
	private String companyNameLocator = "xpath=//UL[@id='otherActions']/LI/SPAN/A[contains(text(),'is not the company I want.')]/../../LABEL";
	private String userNameLocator = "xpath=//INPUT[@id='userName']";
	private String passwordLocator = "xpath=//INPUT[@id='password']";
	private String signInButtonLocator = "xpath=//INPUT[@id='signInButton']";
	private String securityRFIDNumberLocator = "xpath=//INPUT[@id='secureRfidNumber']";
	private String signInWithSecurityRFIDNumberLinkLocator = "xpath=//A[contains(text(),'Sign in with Security RFID Number')]";
	private String requestAnAccountLinkLocator = "xpath=//DIV[@id='requestAccountButton']/A";
	private String planAndPricingLinkLocator = "xpath=//DIV[@id='plansPricingButton']/A";
	private String rememberMySignInInformationLocator = "css#signInForm_signIn_rememberMe";
	private String signInWithUserNameLinkLocator = "xpath=//A[contains(text(),'Sign in with User Name')]";
	private String forgotMyPasswordLinkLocator = "xpath=//UL[@id='otherActions']/LI/SPAN/A[contains(text(),'I forgot my password')]";
	private String isNotTheCompanyIWantLinkLocator = "xpath=//UL[@id='otherActions']/LI/SPAN/A[contains(text(),'is not the company I want.')]";	

	public Login(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	public void gotoSignInPage() {
		selenium.open("/fieldid/login.action");
	}	
	/**
	 * Gets the tenant id from the login page. This assumes the tenant id is
	 * located on the page as:
	 * 
	 * 		${tenant_id} is not the company I want.
	 * 
	 * where 'is not the company I want.' is a link to change the tenant.
	 * If the location of the tenant id changes on the page, this method
	 * will return null. If you receive a null back from this, check the
	 * locator to make sure the tenant id is still in that location.
	 * 
	 * @return tenant id or null of the tenant id could not be found.
	 */
	public String getCompanyName() {
		String s = null;
		if(selenium.isElementPresent(companyNameLocator)) {
			s = selenium.getText(companyNameLocator);
		}
		return s;
	}
	
	/**
	 * Sets the User Name field to the given string. If the User Name field
	 * cannot be located this method will call fail(). If you want to clear
	 * the User Name, call this method with a blank string, e.g. "".
	 * 
	 * Additionally, if the User Name field is not visible this method will
	 * call fail(). Basic logic is if the user could not do it then it should
	 * be a fail.
	 * 
	 * @param s the user name you want to put in text field
	 */
	public void setUserName(String s) {
		if(selenium.isElementPresent(userNameLocator) && selenium.isVisible(userNameLocator)) {
			selenium.type(userNameLocator, s);
		} else {
			fail("Could not locate the User Name field");
		}
	}

	/**
	 * Sets the Password field to the given string. If the Password field
	 * cannot be located this method will call fail(). If you want to clear
	 * the Password, call this method with a blank string, e.g. "".
	 * 
	 * Additionally, if the Password field is not visible this method will
	 * call fail(). Basic logic is if the user could not do it then it should
	 * be a fail.
	 * 
	 * @param s the password you want to put in text field
	 */
	public void setPassword(String s) {
		if(selenium.isElementPresent(passwordLocator) && selenium.isVisible(passwordLocator)) {
			selenium.type(passwordLocator, s);
		} else {
			fail("Could not locate the Password field");
		}
	}

	/**
	 * Retrieve the user name from the User Name field. Normally, this field
	 * should be blank unless you set the 'remember my sign in information'
	 * checkbox.
	 *  
	 * @return the contents of the User Name field.
	 */
	public String getUserName() {
		String s = null;
		if(selenium.isElementPresent(userNameLocator)) {
			s = selenium.getValue(userNameLocator);
		} else {
			fail("Could not locate the User Name field");
		}
		return s;
	}
	
	/**
	 * Sets the Security RFID field to the given string. If the Security RFID
	 * field cannot be located this method will call fail(). If you want to
	 * clear the Security RFID, call this method with a blank string, e.g. "".
	 * 
	 * Additionally, if the Security RFID field is not visible this method will
	 * call fail(). Basic logic is if the user could not do it then it should
	 * be a fail.
	 * 
	 * @param s the Security RFID you want to put in text field
	 */
	public void setSecurityRFIDNumber(String s) {
		if(selenium.isElementPresent(securityRFIDNumberLocator )) {
			selenium.type(securityRFIDNumberLocator, s);
		} else {
			fail("Could not locate the Security RFID Number field");
		}
	}
	
	/**
	 * Check the box for remember my sign in information if input is true.
	 * Otherwise, uncheck the box.
	 * 
	 * @param b
	 */
	public void setRememberMySignInInformation(boolean b) {
		if(selenium.isElementPresent(rememberMySignInInformationLocator)) {
			if(b)	selenium.check(rememberMySignInInformationLocator);
			else	selenium.uncheck(rememberMySignInInformationLocator);
		} else {
			fail("Could not locate the remember my sign in information check box");
		}
	}

	/**
	 * Returns the state of the remember my sign in information check box.
	 * 
	 * @return true if checked, false if unchecked
	 */
	public boolean getRememberMySignInInformation() {
		boolean b = false;
		if(selenium.isElementPresent(rememberMySignInInformationLocator)) {
			String s = selenium.getValue(rememberMySignInInformationLocator);
			b = s.equals("on");
		} else {
			fail("Could not locate the remember my sign in information check box");
		}
		return b;
	}
	
	/**
	 * Clicks the link to switch the login display from username/password to
	 * security RFID number. If the link cannot be found or is not visible
	 * this method will fail.
	 * 
	 */
	public void clickSignInWithSecurityRFIDNumber() {
		if(selenium.isElementPresent(signInWithSecurityRFIDNumberLinkLocator) && selenium.isVisible(signInWithSecurityRFIDNumberLinkLocator)) {
			selenium.click(signInWithSecurityRFIDNumberLinkLocator);
		} else {
			fail("Could not locate the link to switch to Sign in with Security RFID Number");
		}
	}
	
	/**
	 * Clicks the link to go to Sign in with User Name. Assumes you are on the
	 * page with security RFID number login.
	 * 
	 */
	public void clickSignInWithUserName() {
		if(selenium.isElementPresent(signInWithUserNameLinkLocator) && selenium.isVisible(signInWithUserNameLinkLocator)) {
			selenium.click(signInWithUserNameLinkLocator);
		} else {
			fail("Could not locate the link to switch to Sign in with User Name");
		}
	}
	
	/**
	 * Clicks the link to Request An Account. Assumes the link exists. If it
	 * doesn't, fail will be called.
	 */
	public void gotoRequestAnAccount() {
		if(selenium.isElementPresent(requestAnAccountLinkLocator)) {
			selenium.click(requestAnAccountLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Request An Account");
		}
	}
	
	/**
	 * Clicks the link to Plans and Pricing. Assumes the link exists. If it
	 * doesn't, fail will be called.
	 */
	public void gotoPlansAndPricing() {
		if(selenium.isElementPresent(planAndPricingLinkLocator)) {
			selenium.click(planAndPricingLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to Plans and Pricing");
		}
	}
	
	/**
	 * Clicks on the link to go to the reset password page.
	 */
	public void gotoIForgotMyPassword() {
		if(selenium.isElementPresent(forgotMyPasswordLinkLocator)) {
			selenium.click(forgotMyPasswordLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to I forgot my password");
		}
	}
	
	/**
	 * Clicks on the link to change the company id.
	 * @deprecated
	 */
	public void gotoIsNotTheCompanyIWant() {
		if(selenium.isElementPresent(isNotTheCompanyIWantLinkLocator)) {
			selenium.click(isNotTheCompanyIWantLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find the link to is not the company I want");
		}
	}

	/**
	 * Clicks the Sign In button. If you want to actually sign in, you need to
	 * make sure that the username/password or security RFID are filled in with
	 * proper information before calling this method.
	 * 
	 */
	public void gotoSignIn() {
		if(selenium.isElementPresent(signInButtonLocator)) {
			selenium.click(signInButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * This verifies the login when okay. It just checks for errors on the
	 * page or the Oops page. If you know the login will go to Home use
	 * verifySignInNoEULA. If you are signing in as the admin user for the
	 * first time, you should expect the End User License Agreement (EULA)
	 * and call verifySignInWithEULA.
	 * 
	 * If we get errors or the Oops page this method will fail the test case.
	 * 
	 */
	public void verifySignedIn() {
		misc.checkForErrorMessages("VerifySignedIn");
	}
	
	/**
	 * Verifies that sign in went okay. Currently, we check for the Oops page
	 * and that the Home icon exists. If you are logging in as the tenant admin
	 * and you have not accepted the EULA, this will fail.
	 * 
	 * @throws Exception
	 */
	public void verifySignedInHomePage() {
		verifySignedIn();
		if (!misc.isHome()) {
			fail("Could not find the Home icon.");
		}
	}

	/**
	 * Verifies that sign in went okay. Currently, we check for the Oops page.
	 * If you are logging in as the tenant admin and you have accepted the
	 * EULA, this will fail. If you login not as admin user this will fail.
	 * 
	 * @throws Exception
	 */
	public void verifySignedInWithEULA() {
		verifySignedIn();
		if (!misc.isEULA()) {
			fail("Could not find the EULA.");
		}
	}

	/**
	 * If the current tenant does not have PartnerCenter then this will return
	 * false. If PartnerCenter is off then the link is for Plans And Pricing.
	 * Otherwise this will return true and the link to Request An Account will
	 * exist.
	 * 
	 * @return true if link exists, i.e. PartnerCenter is on.
	 */
	public boolean isRequestAnAccountAvailable() {
		boolean b = selenium.isElementPresent(requestAnAccountLinkLocator);
		return b;
	}
	
	/**
	 * If the current tenant does have PartnerCenter then this will return
	 * false. If PartnerCenter is on then the link is for Request An Account.
	 * Otherwise this will return true and the link to Plans And Pricing will
	 * exist.
	 * 
	 * @return true if link exists, i.e. PartnerCenter is off.
	 */
	public boolean isPlansAndPricingAvailable() {
		boolean b = selenium.isElementPresent(planAndPricingLinkLocator);
		return b;
	}
	
	/**
	 * returns the state of the remember my sign in information check box.
	 * 
	 * @return
	 */
	public boolean isRememberMySignInInformationSet() {
		boolean b = false;
		if(selenium.isElementPresent(rememberMySignInInformationLocator)) {
			b = selenium.isChecked(rememberMySignInInformationLocator);
		} else {
			fail("Could not find the check box for remember my sign in information");
		}
		return b;
	}
	
	/**
	 * This method will run through a basic smoke test of all the methods in
	 * this class.
	 * 
	 * @throws Exception
	 */
	public void validate() throws Exception {
		String username = LoggedInTestCase.SYSTEM_USER_NAME;
		String password = LoggedInTestCase.SYSTEM_USER_PASSWORD;
		String rfid = "DEADBEEFCAFEF00D";
		boolean b;
		
		String s = getCompanyName();
		
		clickSignInWithSecurityRFIDNumber();
		setSecurityRFIDNumber(rfid);
		clickSignInWithUserName();

		gotoIForgotMyPassword();
		// TODO: verify
		selenium.goBack();
		misc.waitForPageToLoadAndCheckForOopsPage();
		
		gotoIsNotTheCompanyIWant();
		// TODO: verify
		selenium.goBack();
		misc.waitForPageToLoadAndCheckForOopsPage();
		
		if(isPlansAndPricingAvailable()) {
			gotoPlansAndPricing();
		} else if(isRequestAnAccountAvailable()) {
			gotoRequestAnAccount();
		} else {
			fail("Neither Request An Account or Plans And Pricing are available");
		}
		selenium.goBack();
		misc.waitForPageToLoadAndCheckForOopsPage();
		
		assertFalse("By default the remember my sign in information should be off", isRememberMySignInInformationSet());
		setRememberMySignInInformation(true);
		b = getRememberMySignInInformation();
		assertTrue("Turning on remember my sign in information failed.", b);
		setRememberMySignInInformation(false);
		b = getRememberMySignInInformation();
		assertFalse("Turning off remember my sign in information failed.", b);
		
		setUserName(username);
		s = getUserName();
		assertEquals("Did not get the user name I just set", username, s);
		setPassword(password);
		gotoSignIn();
		verifySignedIn();
	}

	/**
	 * Verify all the elements on the Login page. Creates a screen capture,
	 * checks for error messages then checks for various elements on the
	 * page. If any of the expected elements are missing it will fail.
	 * Otherwise we assume everything is okay.
	 */
	public void verifyLoginPageWithNoErrors() {
		List<String> errorMessages = misc.getFormErrorMessages();
		if(errorMessages.size() > 0) {
			String errors = misc.convertListToString(errorMessages);
			fail("There were errors on the last action\n" + errors);
		}
		verifySignInPage();
	}

	private void verifySignInPage() {
		assertTrue(selenium.isElementPresent(userNameLocator));
		assertTrue(selenium.isElementPresent(passwordLocator));
		assertTrue(selenium.isElementPresent(signInButtonLocator));
		assertTrue(selenium.isElementPresent(signInWithSecurityRFIDNumberLinkLocator));
		assertTrue(selenium.isElementPresent(forgotMyPasswordLinkLocator));
	}

	public void signInAllTheWay(String username, String password) {
		submitSignIn(username, password);
		
		kickOtherSessionIfNeeded();
		
		acceptEULAIfNeed();
		
		
		verifySignedIn();
	}

	public void submitSignIn(String username, String password) {
		setUserName(username);
		setPassword(password);
		gotoSignIn();
	}

	private void kickOtherSessionIfNeeded() {
		if (selenium.isElementPresent("kickOtherUserOut")) {
			confirmKickingSession();
		}
	}

	public void confirmKickingSession() {
		selenium.click("kickOtherUserOut");
		selenium.waitForPageToLoad(Misc.DEFAULT_TIMEOUT);
	}

	private void acceptEULAIfNeed() {
		if (misc.isEULA()) {
			misc.scrollToBottomOfEULA();
			misc.gotoAcceptEULA();
		}
	}
	
	public void signInWithSystemAccount() {
		signInAllTheWay(LoggedInTestCase.SYSTEM_USER_NAME, LoggedInTestCase.SYSTEM_USER_PASSWORD);
	}
	
	
	public void signOut() {
		selenium.openAndWaitForPageLoad("/fieldid/logout.action"); 
	}

	public void assertOnConfirmSessionKick() {
		assertTrue("not confirm page.", selenium.isElementPresent("css=#signInConfirm"));
		
	}

	public void verifyLoginPageWithKickMessage() {
		verifySignInPage();
		assertTrue("message saying you were kicked out is not displayed", selenium.isTextPresent("signed in with the same username causing you to be signed out"));
		
	}
}
