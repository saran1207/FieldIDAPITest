package com.n4systems.fieldid.selenium.login;

import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.Selenium;

public class ChooseACompanyPage extends FieldIDTestCase {

	private Selenium selenium;
	private static final String stringForPageTitle = "Field ID : Safety Management - Choose A Company";
	private static final String notTheCompanyIWantLinkLocator = "//A[contains(text(),'is not the company I want.')]";
	private static final String companyIDFieldLocator = "css=#companyId";
	
	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public ChooseACompanyPage(Selenium selenium) {
		assertTrue("Instance of Selenium is null", selenium != null);
		this.selenium = selenium;
	}
	
	private void assertPageTitle() {
		assertTrue(selenium.getTitle().equals(stringForPageTitle));
	}

	/**
	 * Clicks on the link to take us to the Choose A Company page.
	 */
	public void gotoNotTheCompanyIWant() {
		assertTrue("Could not find the link to 'tenant' is not the company I want", selenium.isElementPresent(notTheCompanyIWantLinkLocator));
		selenium.click(notTheCompanyIWantLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
	}
	
	public void setCompanyID(String tenant) {
		assertTrue("Company ID cannot be null", tenant != null);
		assertTrue("Could not find the input for Company ID", selenium.isElementPresent(companyIDFieldLocator));
		selenium.type(companyIDFieldLocator, tenant);
	}
}
