package com.n4systems.fieldid.selenium.login;

import static org.junit.Assert.*;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

public class Choose {

	private Selenium selenium;
	private Misc misc;
	
	// Locators
	private String companyIDLocator = "xpath=//INPUT[@id='companyId']";
	private String findSignInPageButtonLocator = "xpath=//INPUT[@id='signInToCompany_label_find_sign_in']";

	public Choose(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	/**
	 * This sets the Company ID field.
	 * 
	 * @param s
	 */
	public void setCompanyID(String s) {
		misc.info("Set Company ID to '" + s + "'");
		if(selenium.isElementPresent(companyIDLocator)) {
			selenium.type(companyIDLocator, s);
		} else {
			fail("Could not locate the text box for Company ID");
		}
	}

	/**
	 * This clicks the Find Sign In Page button.s
	 */
	public void gotoFindSignInPage() {
		misc.info("Click Find Sign In Page button");
		if(selenium.isElementPresent(findSignInPageButtonLocator )) {
			selenium.click(findSignInPageButtonLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not locate the Find Sign In Page button");
		}
	}
	
	/**
	 * This verifies the elements on the page exist.
	 */
	public void verifyChooseCompany() {
		misc.captureScreenshot("VerifyChooseCompany.png");
		misc.info("Verify there is a Company ID text box.");
		assertTrue(selenium.isElementPresent(companyIDLocator));
		misc.info("Verify there is a Find Sign in Page button.");
		assertTrue(selenium.isElementPresent(findSignInPageButtonLocator));
	}
}
