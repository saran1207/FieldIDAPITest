package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.lib.DefaultFieldIdSelenium;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.thoughtworks.selenium.Selenium;

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

	public void signOut() {
		legacyLogin.signOut();
	}
	
	public boolean isPlansAndPricingAvailable() {
		return selenium.isElementPresent("//div[@id='plansPricingButton']/a");
	}
	
	public SelectPackagePage clickPlansAndPricingLink() {
		selenium.click("//div[@id='plansPricingButton']/a");
		return new SelectPackagePage(selenium);
	}

	public EULAPage loginToEula(String username, String password) {
		selenium.type("//input[@id='userName']", username);
		selenium.type("//input[@id='password']", password);
		selenium.click("//input[@id='signInButton']");
		return new EULAPage(selenium);
	}
	
}
