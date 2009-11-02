package com.n4systems.fieldid.selenium.login;

import static org.junit.Assert.assertTrue;

import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.DefaultSelenium;

public class LoginPage {

	private DefaultSelenium selenium;
	private static final String startStringForPageTitle = "Field ID : Safety Management - ";
	private static final String endStringForPageTitle = " Sign In";
	private static final String securityRFIDNumberLocator = "css=#secureRfidNumber";
	private static final String usernameLocator = "css=#userName";
	private static final String passwordLocator = "css=#password";
	private static final String signInUserNameButtonLocator = "css=#signInButton";
	private static final String signInWithSecurityRFIDNumberButtonLocator = "css=#signInWithSecurityButton";
	private static final String signInWithSecurityRFIDNumberLocator = "//A[contains(text(),'Sign in with Security RFID Number')]";
	private static final String signInWithUserNameLocator = "//A[contains(text(),'Sign in with User Name')]";
	private static final String forgotMyPasswordLinkLocator = "//A[contains(text(),'I forgot my password')]";
	private static final String notTheCompanyIWantLinkLocator = "//A[contains(text(),'is not the company I want.')]";
	private static final String rememberMySignInInformationLocator = "css=#signInForm_rememberMe";
	private static final String plansAndPricingLinkLocator = "css=#plansPricingButton > a";
	private static final String requestAnAccountLinkLocator = "css=#requestAccountButton > a";
	
	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public LoginPage(DefaultSelenium selenium) {
		assertTrue("Instance of Selenium is null", selenium != null);
		this.selenium = selenium;
	}
	
	/**
	 * Go to the login page. If your seleniumURL variable is set to 
	 * https://www.team.n4systems.com/ then this will be redirected
	 * to the Choose A Company page. You either want to have the seleniumURL
	 * set a a particular tenant, e.g. https://unirope.team.n4systems.com/, and
	 * pass in the context root, e.g. /fieldid/, or pass in the absolute URL,
	 * e.g. https://unirope.team.n4systems.com/fieldid/.
	 * 
	 * @param url
	 */
	public void gotoLoginPage(String url) {
		selenium.open(url);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}
	
	/**
	 * This method assumes that the seleniumURL variable was set correctly
	 * and Selenium has opened a page with the correct domain.
	 * 
	 */
	public void gotoLoginPage() {
		selenium.open("/fieldid/");
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}
	
	private void assertPageTitle() {
		assertTrue(selenium.getTitle().startsWith(startStringForPageTitle));
		assertTrue(selenium.getTitle().endsWith(endStringForPageTitle));
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
	 * Enter text into the Password field.
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		assertTrue("Password cannot be null", password != null);
		assertTrue("Could not find the Password text field", selenium.isElementPresent(passwordLocator));
		selenium.type(passwordLocator, password);
	}
	
	/**
	 * Enter text into the Security RFID Number field.
	 * 
	 * @param rfid
	 */
	public void setSecurityRFIDNumber(String rfid) {
		assertTrue("RFID Number cannot be null", rfid != null);
		assertTrue("Could not find the Security RFID Number text field", selenium.isElementPresent(securityRFIDNumberLocator ));
		selenium.type(securityRFIDNumberLocator, rfid);
	}
	
	/**
	 * Click the sign in button for signing in with User Name and Password.
	 * If the user name and password are not set, this will generate an
	 * error.
	 */
	public void gotoSignInUserName() {
		assertTrue("Could not find the Sign In button", selenium.isElementPresent(signInUserNameButtonLocator));
		selenium.click(signInUserNameButtonLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		// TODO: verify we arrived at the correct page
	}
	
	/**
	 * Click the sign in button for signing in with Security RFID Number.
	 * If the Security RFID Number is not set, this will generate an
	 * error.
	 */
	public void gotoSignInSecurityRFIDNumber() {
		assertTrue("Could not find the Sign In button", selenium.isElementPresent(signInWithSecurityRFIDNumberButtonLocator));
		selenium.click(signInWithSecurityRFIDNumberButtonLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		// TODO: verify the switch went okay
	}
	
	/**
	 * Clicks the link to sign in with a Security RFID Number.
	 * Assumes you are on the page to sign in with user name and password.
	 */
	public void gotoSignInWithSecurityRFIDNumberLink() {
		assertTrue("Could not find the link to switch to Sign in with Security RFID Number", selenium.isElementPresent(signInWithSecurityRFIDNumberLocator));
		selenium.click(signInWithSecurityRFIDNumberLocator);
		assertPageTitle();
	}

	/**
	 * Clicks the link to sign in with User Name and Password.
	 * Assumes you are on the page to sign in with an RFID number.
	 */
	public void gotoSignInWithUserNameLink() {
		assertTrue("Could not find the link to switch to Sign in with User Name", selenium.isElementPresent(signInWithUserNameLocator));
		selenium.click(signInWithUserNameLocator);
		assertPageTitle();
	}

	/**
	 * Clicks on the link for I forgot my password.
	 */
	public void gotoIForgotMyPassword() {
		assertTrue("Could not find the link to I forgot my password", selenium.isElementPresent(forgotMyPasswordLinkLocator));
		selenium.click(forgotMyPasswordLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		// TODO: verify we arrived at the correct page
	}

	/**
	 * Clicks on the link to take us to the Choose A Company page.
	 */
	public void gotoNotTheCompanyIWant() {
		assertTrue("Could not find the link to 'tenant' is not the company I want", selenium.isElementPresent(notTheCompanyIWantLinkLocator));
		selenium.click(notTheCompanyIWantLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		// TODO: verify we arrived at the correct page
	}
	
	/**
	 * Toggles the remember my sign in information checkbox.
	 */
	public void setRememberMySignInInformation() {
		assertTrue("Could not find the checkbox for remember my sign in information", selenium.isElementPresent(rememberMySignInInformationLocator));
		selenium.click(rememberMySignInInformationLocator);
	}
	
	/**
	 * Explicitly set/unset the remember my sign in information checkbox.
	 *
	 * @param b If false, unset the field. Otherwise, set the field.
	 */
	public void setRememberMySignInInformation(boolean b) {
		assertTrue("Could not find the checkbox for remember my sign in information", selenium.isElementPresent(rememberMySignInInformationLocator));
		if((selenium.isChecked(rememberMySignInInformationLocator) && !b) || (!selenium.isChecked(rememberMySignInInformationLocator) && b)) {
			setRememberMySignInInformation();
		}
	}
	
	/**
	 * Go to the Sign Up page. Not all tenants have the link to the Sign Up
	 * page. If the tenant does NOT have the extended feature PartnerCenter
	 * enabled, the link to Plans and Pricing will be available. Currently,
	 * this list includes:
	 * 
	 * 		fieldid
	 * 		jergens
	 * 		msa
	 * 		rtc
	 * 		seafit
	 * 
	 */
	public void gotoPlansAndPricing() {
		assertTrue("Could not find the link to Plans and Pricing", selenium.isElementPresent(plansAndPricingLinkLocator));
		selenium.click(plansAndPricingLinkLocator);
		// TODO: verify we arrived at the correct page
	}
	
	/**
	 * Go to Request an Account. This is requesting a customer account with
	 * the current tenant. If the tenant has PartnerCenter enabled, this link
	 * is available. Otherwise, this method will throw an exception.
	 */
	public void gotoRequestAnAccount() {
		assertTrue("Could not find the link to Request an Account", selenium.isElementPresent(requestAnAccountLinkLocator));
		selenium.click(requestAnAccountLinkLocator);
		// TODO: verify we arrived at the correct page
	}
}
