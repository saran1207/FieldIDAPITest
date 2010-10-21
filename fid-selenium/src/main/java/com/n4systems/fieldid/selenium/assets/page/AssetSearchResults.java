package com.n4systems.fieldid.selenium.assets.page;

import static org.junit.Assert.fail;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class AssetSearchResults {
	FieldIdSelenium selenium;
	MiscDriver misc;
	
	// Locators
	private String productSearchResultsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Asset Search Results')]";

	public AssetSearchResults(FieldIdSelenium selenium, MiscDriver misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	/**
	 * Checks to see if there are any error messages on the page and checks
	 * for the header "Asset Search Results" on the page.
	 */
	public void verifyProductSearchResultsPage() {
		misc.checkForErrorMessages("verifyAssetsPage");
		if(!selenium.isElementPresent(productSearchResultsPageHeaderLocator)) {
			fail("Could not find the header for 'Asset Search'.");
		}
	}

	public int totalResults() {
		String reportTotal = selenium.getText("css=.total");
		return Integer.valueOf(reportTotal.trim().replace("Total Products", "").trim());
	}
}
