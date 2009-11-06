package com.n4systems.fieldid.selenium.login;

import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestBase;

public class SignUpPackagesPage extends SeleneseTestBase {

	private DefaultSelenium selenium;
	private static final String plansAndPricingLinkLocator = "css=#plansPricingButton > a";
	private static final Object stringForPageTitle = "Field ID : Safety Management -";	// TODO: WEB-1347
	private static final String columnHeaderFreeLocator = "css=#package_Free";
	private static final String columnHeaderBasicLocator = "css=#package_Basic";
	private static final String columnHeaderPlusLocator = "css=#package_Plus";
	private static final String columnHeaderEnterpriseLocator = "css=#package_Enterprise";
	private static final String columnHeaderUnlimitedLocator = "css=#package_Unlimited";
	private static final String chooseAnotherPackageLinkLocator = "//A[contains(text(),'choose another package')]";
	
	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public SignUpPackagesPage(DefaultSelenium selenium) {
		assertTrue("Instance of Selenium is null", selenium != null);
		this.selenium = selenium;
	}
	
	/**
	 * This method clicks the link on Sign Up Add to return back to the Sign
	 * Up Package page. It assumes you have selected a package from the Sign
	 * Up Package page and are on the page to input your information and the
	 * company information in order to create a new tenant account.
	 */
	public void gotoChooseAnotherPackage() {
		assertTrue("Could not find the link to return to the Sign Up Package page", selenium.isElementPresent(chooseAnotherPackageLinkLocator));
		selenium.click(chooseAnotherPackageLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
		assertPageContent();
	}
	
	private void assertPageTitle() {
		assertTrue(selenium.getTitle().equals(stringForPageTitle));
	}

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
		assertTrue("Could not find the link to Plans and Pricing. Maybe this tenant has PartnerCenter enabled.", selenium.isElementPresent(plansAndPricingLinkLocator));
		selenium.click(plansAndPricingLinkLocator);
		selenium.waitForPageToLoad(FieldIDTestCase.pageLoadDefaultTimeout);
		assertPageTitle();
		assertPageContent();
	}

	private void assertPageContent() {
		selenium.isElementPresent(columnHeaderFreeLocator);
		selenium.isElementPresent(columnHeaderBasicLocator);
		selenium.isElementPresent(columnHeaderPlusLocator);
		selenium.isElementPresent(columnHeaderEnterpriseLocator);
		selenium.isElementPresent(columnHeaderUnlimitedLocator);
	}
}
