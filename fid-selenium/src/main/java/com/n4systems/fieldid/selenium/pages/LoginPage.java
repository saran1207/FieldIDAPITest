package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.lib.DefaultFieldIdSelenium;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.assertTrue;

public class LoginPage extends WebPage {
	
	public static final String SYSTEM_USER_NAME = "n4systems";
	public static final String SYSTEM_USER_PASSWORD = "Xk43g8!@";
	
	private Login legacyLogin;
	
	public LoginPage(Selenium selenium) {
		this(selenium, true);
	}
	
	public LoginPage(Selenium selenium, boolean waitForPageToLoad) {
		super(selenium, waitForPageToLoad);
		legacyLogin = new Login(new DefaultFieldIdSelenium(selenium), new MiscDriver(new DefaultFieldIdSelenium(selenium)));
	}
	
	public HomePage login() {
		return legacyLogin.signInAllTheWayToHome(SYSTEM_USER_NAME, SYSTEM_USER_PASSWORD);
	}
	
	public HomePage login(String username, String password) {
		return legacyLogin.signInAllTheWayToHome(username, password);
	}
	
	public HomePage systemLogin() {
		return legacyLogin.signInWithSystemAccount();
	}

	public AccountSetupWizardPage signInAllTheWayToWizard(String userName, String password) {
		return legacyLogin.signInAllTheWayToWizard(userName, password);
	}

	public SessionBumpPage signInToSessionBump(String userName, String password) {
		doSignIn(userName, password);
        return new SessionBumpPage(selenium);
	}

	public void signOut() {
		legacyLogin.signOut();
	}
	
	public boolean isPlansAndPricingAvailable() {
		return selenium.isElementPresent("//div[@id='plansPricingButton']/a");
	}

	public boolean isRequestAnAccountAvailable() {
		return selenium.isElementPresent("xpath=//DIV[@id='requestAccountButton']/A");
	}
	
	public SelectPackagePage clickPlansAndPricingLink() {
		selenium.click("//div[@id='plansPricingButton']/a");
		return new SelectPackagePage(selenium);
	}

	public EULAPage loginToEula(String username, String password) {
        doSignIn(username, password);
		return new EULAPage(selenium);
	}

    private void doSignIn(String userName, String password) {
		selenium.type("//input[@id='userName']", userName);
		selenium.type("//input[@id='password']", password);
		selenium.click("//input[@id='signInButton']");
    }

    public void verifySessionKickMessageDisplayed() {
        assertTrue("message saying you were kicked out is not displayed", selenium.isTextPresent("signed in with the same username causing you to be signed out"));
    }

}
