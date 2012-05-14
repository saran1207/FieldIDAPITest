package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.assertTrue;

public class LoginPage extends WebPage {
	
	public static final String SYSTEM_USER_NAME = "n4systems";
	public static final String SYSTEM_USER_PASSWORD = "f0rM@t!!";
	
	public LoginPage(Selenium selenium) {
		this(selenium, true);
	}
	
	public LoginPage(Selenium selenium, boolean waitForPageToLoad) {
		super(selenium, waitForPageToLoad);
	}
	
	public HomePage login() {
		return signInAllTheWayToHome(SYSTEM_USER_NAME, SYSTEM_USER_PASSWORD);
	}
	
	public HomePage login(String username, String password) {
		return signInAllTheWayToHome(username, password);
	}
	
	public HomePage systemLogin() {
		return login();
	}

	public AccountSetupWizardPage signInAllTheWayToWizard(String userName, String password) {
        signIn(userName, password);
		return new AccountSetupWizardPage(selenium, false);
	}

	public SessionBumpPage signInToSessionBump(String userName, String password) {
		enterCredentialsAndSubmit(userName, password);
        return new SessionBumpPage(selenium, false);
	}

	public boolean isPlansAndPricingAvailable() {
		return selenium.isElementPresent("//div[@id='plansPricingButton']/a");
	}

	public boolean isRequestAnAccountAvailable() {
		return selenium.isElementPresent("xpath=//DIV[@id='requestAccountButton']/A");
	}
	
	public RegistrationRequestPage clickRequestAnAccount(){
		selenium.click("//div[@id='requestAccountButton']/a");
		return new RegistrationRequestPage(selenium);
	}
	
	public SelectPackagePage clickPlansAndPricingLink() {
		selenium.click("//div[@id='plansPricingButton']/a");
		return new SelectPackagePage(selenium);
	}

	public EULAPage loginToEula(String username, String password) {
        enterCredentialsAndSubmit(username, password);
		return new EULAPage(selenium, false);
	}

    private void enterCredentialsAndSubmit(String userName, String password) {
		selenium.type("//input[@id='userName']", userName);
        selenium.focus("//input[@id='password']");
        selenium.type("//input[@id='password']", password);
        selenium.focus("//input[@id='password']");
        selenium.type("//input[@id='password']", password);
		selenium.click("//input[@id='signInButton']");
        waitForPageToLoad();
    }

    public void verifySessionKickMessageDisplayed() {
        assertTrue("message saying you were kicked out is not displayed", selenium.isTextPresent("signed in with the same username causing you to be signed out"));
    }

	private void signIn(String username, String password) {
		enterCredentialsAndSubmit(username, password);
		kickOtherSessionIfNeeded();

		acceptEULAIfNeed();
	}

	private void kickOtherSessionIfNeeded() {
		if (selenium.isElementPresent("kickOtherUserOut")) {
			confirmKickingSession();
		}
	}

    private void confirmKickingSession() {
		selenium.click("kickOtherUserOut");
        waitForPageToLoad();
	}

	private void acceptEULAIfNeed() {
		if (onEULAPage()) {
			selenium.runScript("toggleEula();");
            selenium.click("//input[@id='acceptEula']");
            waitForPageToLoad();
        }
	}

    private boolean onEULAPage() {
        return selenium.isElementPresent("//h1[contains(text(),'Field ID End User Licence Agreement')]");
    }

    private HomePage signInAllTheWayToHome(String username, String password) {
		signIn(username, password);
		return new HomePage(selenium, false);
	}

    public ForgotPasswordPage clickForgotMyPassword() {
        selenium.click("//a[.='Forgot your Password?']");
        return new ForgotPasswordPage(selenium);
    }

}
