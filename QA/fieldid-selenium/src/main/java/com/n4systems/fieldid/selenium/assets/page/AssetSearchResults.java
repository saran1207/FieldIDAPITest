package com.n4systems.fieldid.selenium.assets.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.Misc;

public class AssetSearchResults {
	FieldIdSelenium selenium;
	Misc misc;
	
	// Locators
	private String productSearchResultsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Product Search Results')]";

	public AssetSearchResults(FieldIdSelenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	/**
	 * Checks to see if there are any error messages on the page and checks
	 * for the header "Product Search Results" on the page.
	 */
	public void verifyProductSearchResultsPage() {
		misc.checkForErrorMessages("verifyAssetsPage");
		if(!selenium.isElementPresent(productSearchResultsPageHeaderLocator)) {
			fail("Could not find the header for 'Product Search'.");
		}
	}
}
