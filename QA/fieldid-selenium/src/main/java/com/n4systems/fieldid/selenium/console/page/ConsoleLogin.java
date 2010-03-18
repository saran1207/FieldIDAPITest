package com.n4systems.fieldid.selenium.console.page;

import static org.junit.Assert.assertFalse;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.n4systems.fieldid.selenium.testcase.LoggedInTestCase;

public class ConsoleLogin {
	FieldIdSelenium selenium;
	Misc misc;
	
	
	// Locators
	private String adminConsoleURL = "/fieldidadmin/";
	private String userNameTextFieldLocator = "xpath=//INPUT[@id='signIntoSystem_username']";
	private String passwordTextFieldLocator = "xpath=//INPUT[@id='signIntoSystem_password']";
	private String submitButtonLocator = "xpath=//INPUT[@id='signIntoSystem_0']";
	private String organizationLinkLocator = "xpath=//UL[@id='nav']/LI/A[contains(text(),'Organizations')]";

	public ConsoleLogin(FieldIdSelenium selenium, Misc misc) {
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
		gotoPage("");
	}
	
	/**
	 * Enter a username for logging into the Administration Console.
	 * 
	 * @param username
	 */
	public void setUserName(String username) {
		misc.info("Set the username to '" + username + "'");
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
		misc.info("Set the password to '" + password + "'");
		if(selenium.isElementPresent(passwordTextFieldLocator)) {
			selenium.type(passwordTextFieldLocator, password);
		}
	}
	
	/**
	 * Click the submit button on the Login page for Administration Console.
	 */
	public void gotoLogin() {
		misc.info("Click the Submit button and log into the Administration Console");
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
		misc.info("Click the Organizations tab");
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
		misc.info("Confirm we logged in okay. Should be at the list of tenants.");
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
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		return this;
	}
}

