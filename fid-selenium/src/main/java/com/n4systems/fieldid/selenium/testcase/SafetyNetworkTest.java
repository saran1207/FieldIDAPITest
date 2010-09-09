package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkCatalogPage;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkSettingsPage;
import com.n4systems.fieldid.selenium.pages.SafetyNetworkPage;

public class SafetyNetworkTest extends FieldIDTestCase {

	// Locations
	private String safetyNetworkPageHeaderLocator = "//h1[contains(text(),'Safety Network')]";
	private String safetyNetworkInboxLocator = "//a[@id='inbox']";
	private String safetyNetworkCatalogLocator = "//a[@id='manageCatalog']";
	private String safetyNetworkSettingsLocator = "//a[@id='privacySettings']";
	private String safetyNetworkOverviewLocator = "//a[@id='help_link']";
	private String safetyNetworkVideoLocator = "//a[@id='video_link']";

	// Product Types
	private static final String PRODUCT_TYPE_CHECKBOX_NAME_1 = "Gravity Harness";

	public void test_not_null() throws Exception {
		SafetyNetworkPage safetyNetworkPage = startAsCompany("msa").login().clickSafetyNetworkLink();
		assertTrue(safetyNetworkPage != null);
	}

	public void test_static_page_contents() {
		assertTrue("Could not find the Safety Network main page", selenium.isElementPresent(safetyNetworkPageHeaderLocator));
		assertTrue("Could not find the link for Inbox", selenium.isElementPresent(safetyNetworkInboxLocator));
		assertTrue("Could not find the link for Catalog", selenium.isElementPresent(safetyNetworkCatalogLocator));
		assertTrue("Could not find the link for PrivacySettings", selenium.isElementPresent(safetyNetworkSettingsLocator));
		assertTrue("Could not find the link for Overview", selenium.isElementPresent(safetyNetworkOverviewLocator));
		assertTrue("Could not find the link for Video", selenium.isElementPresent(safetyNetworkVideoLocator));
	}

	public void test_select_vendor() {
		// Need connections
	}

	public void test_import() {
		// Need connections
	}

	@Test
	public void test_publish_catalog() {
		SafetyNetworkCatalogPage safetyNetworkCatalogPage = startAsCompany("msa").login().clickSafetyNetworkLink().goToCatalog();

		safetyNetworkCatalogPage.checkProductTypeCheckBox(PRODUCT_TYPE_CHECKBOX_NAME_1);

		safetyNetworkCatalogPage.submitForm();
		assertTrue("Could not toggle Product Type", safetyNetworkCatalogPage.isChecked(PRODUCT_TYPE_CHECKBOX_NAME_1));

		// Uncheck for retesting...
		safetyNetworkCatalogPage.unCheckProductTypeCheckBox(PRODUCT_TYPE_CHECKBOX_NAME_1);
		safetyNetworkCatalogPage.submitForm();
	}

	@Test
	public void test_settings_and_privacy() {
		SafetyNetworkSettingsPage safetyNetworkSettingsPage = startAsCompany("msa").login().clickSafetyNetworkLink().goToSettings();
		
		//Starts out checked, so uncheck for testing.
		safetyNetworkSettingsPage.unCheckAutoAcceptCheckBox();
		safetyNetworkSettingsPage.unCheckAutoPublishCheckBox();
		
		safetyNetworkSettingsPage.submitForm();
		
		assertTrue("Could not toggle Auto Accept", !safetyNetworkSettingsPage.isAutoAcceptChecked());
		assertTrue("Could not toggle Auto Publish", !safetyNetworkSettingsPage.isAutoPublishChecked());
		
		// Check for retesting...
		safetyNetworkSettingsPage.checkPublishAssetsBox();
		safetyNetworkSettingsPage.checkAutoAcceptBox();
		
		safetyNetworkSettingsPage.submitForm();
	}

}
