package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LoginPage extends WebPage {
	
	public static final String SYSTEM_USER_NAME = "n4systems";
	public static final String SYSTEM_USER_PASSWORD = "Xk43g8!@";
	
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
		return signInAllTheWayToWizard(userName, password);
	}

	public SessionBumpPage signInToSessionBump(String userName, String password) {
		enterCredentialsAndSubmit(userName, password);
        return new SessionBumpPage(selenium);
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
        enterCredentialsAndSubmit(username, password);
		return new EULAPage(selenium);
	}

    private void enterCredentialsAndSubmit(String userName, String password) {
		selenium.type("//input[@id='userName']", userName);
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
		if (isEULA()) {
			scrollToBottomOfEULA();
			gotoAcceptEULA();
		}
	}

    public boolean isEULA() {
        return selenium.isElementPresent("//h1[contains(text(),'Field ID End User Licence Agreement')]");
    }

	public void scrollToBottomOfEULA() {
        selenium.runScript("toggleEula();");
	}

	public void gotoAcceptEULA() {
        selenium.click("//input[@id='acceptEula']");
	}

    private HomePage signInAllTheWayToHome(String username, String password) {
		signIn(username, password);
		return new HomePage(selenium, false);
	}

    public ForgotPasswordPage clickForgotMyPassword() {
        selenium.click("//a[.='I forgot my password']");
        return new ForgotPasswordPage(selenium);
    }

}
