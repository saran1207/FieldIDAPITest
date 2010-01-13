package com.n4systems.fieldid.selenium.assets;

import org.junit.Assert;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

public class ProductSearchResults extends Assert {
	Selenium selenium;
	Misc misc;
	
	// Locators
	private String productSearchResultsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Product Search Results')]";

	public ProductSearchResults(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	/**
	 * Checks to see if there are any error messages on the page and checks
	 * for the header "Product Search Results" on the page.
	 */
	public void verifyProductSearchResultsPage() {
		misc.info("Verify Product Search went okay.");
		misc.checkForErrorMessages("verifyAssetsPage");
		if(!selenium.isElementPresent(productSearchResultsPageHeaderLocator)) {
			fail("Could not find the header for 'Product Search'.");
		}
	}
}
