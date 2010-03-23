package com.n4systems.fieldid.selenium.login.page;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;


public class Choose {

	private FieldIdSelenium selenium;
	private Misc misc;
	
	// Locators
	private String companyIDLocator = "xpath=//INPUT[@id='companyId']";
	private String findSignInPageButtonLocator = "xpath=//INPUT[@id='signInToCompany_label_find_sign_in']";

	public Choose(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}

	/**
	 * This sets the Company ID field. If the company is set to www
	 * Field ID should automatically forward you to the Choose A Company
	 * page. 
	 * 
	 * @param s
	 */
	public void setCompanyID(String s) {
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
		assertTrue(selenium.isElementPresent(companyIDLocator));
		assertTrue(selenium.isElementPresent(findSignInPageButtonLocator));
	}
}
