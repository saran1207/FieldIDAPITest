package com.n4systems.fieldid.selenium.login;

import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.Selenium;

public class LoginPage extends FieldIDTestCase {

	private Selenium selenium;
	private static final String startStringForPageTitle = "Field ID : Safety Management - ";
	private static final String endStringForPageTitle = " Sign In";
	private static final String securityRFIDNumberLocator = "css=#secureRfidNumber";
	private static final String usernameLocator = "css=#userName";
	private static final String passwordLocator = "css=#password";
	private static final String returnToSignInLinkLocator = "//A[contains(text(),'Return to Sign In')]";
	private static final String signInWithSecurityRFIDNumberLocator = "//A[contains(text(),'Sign in with Security RFID Number')]";
	private static final String signInWithUserNameLocator = "//A[contains(text(),'Sign in with User Name')]";
	private static final String rememberMySignInInformationLocator = "css=#signInForm_rememberMe";
	private static final String plansAndPricingLinkLocator = "css=#plansPricingButton > a";
	private static final String requestAnAccountLinkLocator = "css=#requestAccountButton > a";
	private static final String returnToSignInFromSendPasswordButtonLocator = "//input[@value='Return to Sign In')]";
	private static final String returnToSignInFromSignUpPackagesLinkLocator = "//A[contains(text(),'Return to Sign In')]";
	private static final String returnToSignInFromRegiserNewUserButtonLocator = "//input[@value='Return to Sign In')]";
	private static final String n4systemsLinkLocator = "//A[contains(text(),'N4 Systems Inc.')]";
	private static final Object n4systemsWebSiteTitleString = "N4 Systems | Safety Through Innovation";
	private static final String poweredByFieldIDImageLocator = "css=.poweredBy > img";
	private static final String thawteSiteSealLinkLocator = "//*[@id='sslCert']/A/IMG[@alt='Click to verify']/..";
	private static final Object thawteSiteSealWindowTitleString = "thawte : siteseal";
	private static final String thawteOrganizationString = "N4 Systems Inc.";
	private static final String thawteDomainString = "*.fieldid.com";
	private static final String thawteCountryString = "Canada";
	private static final String thawteStatusString = "Valid";
	private static final String returnToSignInFromChooseACompanyButtonLocator = "css=#signInToCompany_label_find_sign_in";
	
	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public LoginPage(Selenium selenium) {
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
	 * @return true if the go to was successful
	 */
	public void gotoLoginPage() {
		selenium.open("/fieldid/");
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
		assertPageContent();
	}
	
	private void assertPageContent() {
		assertTrue("Could not find link to N4 Systems on the login page", selenium.isElementPresent(n4systemsLinkLocator));
		assertTrue("Could not find User Name input on the login page", selenium.isElementPresent(usernameLocator));
		assertTrue("Could not find Password input on the login page", selenium.isElementPresent(passwordLocator));
		assertTrue("Could not find Security RFID input on the login page", selenium.isElementPresent(securityRFIDNumberLocator));
		assertTrue("Could not find RFID Sign In button on the login page", selenium.isElementPresent(signInWithSecurityRFIDNumberLocator));
		assertTrue("Could not find User Name Sign In button on the login page", selenium.isElementPresent(signInWithUserNameLocator));
		assertTrue("Could not find remember sign in information checkbox on the login page", selenium.isElementPresent(rememberMySignInInformationLocator));
		assertTrue("Could not find Powered By Field ID image on the login page", selenium.isElementPresent(poweredByFieldIDImageLocator));

	}
	
	public void gotoReturnToSignInFromChooseACompany() {
		assertTrue("Could not find the Find Sign In Page button", selenium.isElementPresent(returnToSignInFromChooseACompanyButtonLocator));
		selenium.click(returnToSignInFromChooseACompanyButtonLocator);
		assertPageTitle();
		assertPageContent();
	}
	
	/**
	 * If this is run against production or we fake running against production
	 * the Thawte Logo appears and this will click on the Logo to open a new
	 * window with our site seal. To fake production, set the DNS resolution
	 * to make www.fieldid.com and ${tenant}.fieldid.com point to the test
	 * machine. On a Windows computer you can do this by editing the HOSTS
	 * file located in C:\Windows\system32\drivers\etc\.
	 */
	public void gotoThawteCertificate() {
		if(selenium.isElementPresent(thawteSiteSealLinkLocator)) {
			selenium.click(thawteSiteSealLinkLocator);
			verifyThawteSiteSealPresent();
		}
	}
	
	private void verifyThawteSiteSealPresent() {
		String titles[] = selenium.getAllWindowTitles();
		boolean found = false;
		for(int i = 0; i < titles.length; i++) {
			if(titles[i].equals(thawteSiteSealWindowTitleString)) {
				found = true;
				break;
			}
		}
		assertTrue("Could not find the window with <title> '" + n4systemsWebSiteTitleString + "'", found);
		assertTrue("Could not find the organization information", selenium.isTextPresent(thawteOrganizationString));
		assertTrue("Could not find the domain information", selenium.isTextPresent(thawteDomainString));
		assertTrue("Could not find the country information", selenium.isTextPresent(thawteCountryString));
		assertTrue("Could not find the current status information", selenium.isTextPresent(thawteStatusString));
	}

	public void gotoN4Systems() {
		assertTrue("Could not find the link to open the N4 Systems website", selenium.isElementPresent(n4systemsLinkLocator));
		selenium.click(n4systemsLinkLocator);
		verifyN4SystemsWebSiteTitle();
	}
	
	private void verifyN4SystemsWebSiteTitle() {
		String titles[] = selenium.getAllWindowTitles();
		boolean found = false;
		for(int i = 0; i < titles.length; i++) {
			if(titles[i].equals(n4systemsWebSiteTitleString)) {
				found = true;
				break;
			}
		}
		assertTrue("Could not find the window with <title> '" + n4systemsWebSiteTitleString + "'", found);
	}

	public void gotoReturnToSignInFromSendPasswordPage() {
		assertTrue("Could not find the link to return to sign in", selenium.isElementPresent(returnToSignInFromSendPasswordButtonLocator));
		selenium.click(returnToSignInFromSendPasswordButtonLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}
	
	public void gotoReturnToSignInFromRegisterNewUserPage() {
		assertTrue("Could not find the link to return to sign in", selenium.isElementPresent(returnToSignInFromRegiserNewUserButtonLocator));
		selenium.click(returnToSignInFromRegiserNewUserButtonLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}
	
	public void gotoReturnToSignInFromSignUpPackagesPage() {
		assertTrue("Could not find the link to return to sign in", selenium.isElementPresent(returnToSignInFromSignUpPackagesLinkLocator));
		selenium.click(returnToSignInFromSignUpPackagesLinkLocator);
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

	public void gotoReturnToSignInFromForgotPassword() {
		assertTrue("Could not find the link to return to sign in", selenium.isElementPresent(returnToSignInLinkLocator));
		selenium.click(returnToSignInLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}
	
	public void gotoReturnToSignInFromRegisterNewUser() {
		assertTrue("Could not find the link to return to sign in", selenium.isElementPresent(returnToSignInLinkLocator));
		selenium.click(returnToSignInLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
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
	
	// This stuff should go to the SignUpPage class
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
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		// TODO: verify we arrived at the correct page
	}
	
	// This stuff should go to the RequestAccountPage class
	/**
	 * Go to Request an Account. This is requesting a customer account with
	 * the current tenant. If the tenant has PartnerCenter enabled, this link
	 * is available. Otherwise, this method will throw an exception.
	 */
	public void gotoRequestAnAccount() {
		assertTrue("Could not find the link to Request an Account", selenium.isElementPresent(requestAnAccountLinkLocator));
		selenium.click(requestAnAccountLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		// TODO: verify we arrived at the correct page
	}
}
