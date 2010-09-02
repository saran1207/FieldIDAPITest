package com.n4systems.fieldid.selenium.console.page;

import static org.junit.Assert.assertFalse;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class ConsoleLogin {
	FieldIdSelenium selenium;
	MiscDriver misc;
	
	
	// Locators
	private String adminConsoleURL = "/fieldid/admin/";
	private String userNameTextFieldLocator = "xpath=//INPUT[@id='signIntoSystem_username']";
	private String passwordTextFieldLocator = "xpath=//INPUT[@id='signIntoSystem_password']";
	private String submitButtonLocator = "xpath=//INPUT[@id='signIntoSystem_0']";
	private String organizationLinkLocator = "xpath=//UL[@id='nav']/LI/A[contains(text(),'Organizations')]";

	public ConsoleLogin(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	/**
	 * Goes to the Administration Console. Currently this is the context root
	 * /fieldidadmin/. This area is accessed by N4 Systems employees only.
	 * It assumes you do the right thing. It is not bullet proof. You can do
	 * serious harm to the system by doing the wrong things in this area.
	 * 
	 */
	public void gotoAdminConsole() {
		gotoPage("signIn.action");
	}
	
	/**
	 * Enter a username for logging into the Administration Console.
	 * 
	 * @param username
	 */
	public void setUserName(String username) {
		if(selenium.isElementPresent(userNameTextFieldLocator)) {
			selenium.type(userNameTextFieldLocator, username);
		}
	}
	
	/**
	 * Enter a password for logging into the Administration Console.
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		if(selenium.isElementPresent(passwordTextFieldLocator)) {
			selenium.type(passwordTextFieldLocator, password);
		}
	}
	
	/**
	 * Click the submit button on the Login page for Administration Console.
	 */
	public void gotoLogin() {
		if(selenium.isElementPresent(submitButtonLocator)) {
			selenium.click(submitButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}
	
	/**
	 * Go to the Organizations tab. If you are not logged in this will take
	 * you to the login page.
	 */
	public void gotoOrganizations() {
		if(selenium.isElementPresent(organizationLinkLocator)) {
			selenium.click(organizationLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		}
	}

	/**
	 * Verify the login happened okay. We assume we are logged in if the
	 * inputs for username and password are not present.
	 */
	public void verifyLogin() {
		assertFalse(selenium.isElementPresent(userNameTextFieldLocator));
		assertFalse(selenium.isElementPresent(passwordTextFieldLocator));
	}

	public ConsoleLogin gotoAdminConsoleAndLogin() {
		gotoAdminConsole();
		setUserName(LoggedInTestCase.SYSTEM_USER_NAME);
		setPassword(LoggedInTestCase.SYSTEM_USER_PASSWORD);
		gotoLogin();
		verifyLogin();
		return this;
	}

	public ConsoleLogin gotoPage(String pageUrl) {
		selenium.open(adminConsoleURL + pageUrl);
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		return this;
	}
}

