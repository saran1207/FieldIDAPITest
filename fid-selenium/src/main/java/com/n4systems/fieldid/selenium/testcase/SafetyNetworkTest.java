package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkImportPage;
import com.n4systems.fieldid.selenium.safetynetwork.page.VendorConnectionProfilePage;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkCatalogPage;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkSettingsPage;
import com.n4systems.fieldid.selenium.pages.SafetyNetworkPage;

public class SafetyNetworkTest extends FieldIDTestCase {

	// Locators
	private String safetyNetworkPageHeaderLocator = "//h1[contains(text(),'Safety Network')]";
	private String safetyNetworkInboxLocator = "//a[@id='inbox']";
	private String safetyNetworkCatalogLocator = "//a[@id='manageCatalog']";
	private String safetyNetworkSettingsLocator = "//a[@id='privacySettings']";
	private String safetyNetworkOverviewLocator = "//a[@id='help_link']";
	private String safetyNetworkVideoLocator = "//a[@id='video_link']";
	private String connectionProfileName = "//p[.='Sea-Fit']";

	// Product Types
	private static final String PRODUCT_TYPE_CHECKBOX_NAME_1 = "Gravity Harness";

	// Connection names
	private static final String CONNECTION_NAME_1 = "Sea-Fit";
	private static final String CONNECTION_NAME_2 = "CM";
	private static final String ASSET_SERIAL_1 = "A09";

	@Test
	public void test_static_page_contents() {
		SafetyNetworkPage safetyNetworkPage = startAsCompany("msa").login().clickSafetyNetworkLink();
		assertTrue(safetyNetworkPage != null);
		assertTrue("Could not find the Safety Network main page", selenium.isElementPresent(safetyNetworkPageHeaderLocator));
		assertTrue("Could not find the link for Inbox", selenium.isElementPresent(safetyNetworkInboxLocator));
		assertTrue("Could not find the link for Catalog", selenium.isElementPresent(safetyNetworkCatalogLocator));
		assertTrue("Could not find the link for PrivacySettings", selenium.isElementPresent(safetyNetworkSettingsLocator));
		assertTrue("Could not find the link for Overview", selenium.isElementPresent(safetyNetworkOverviewLocator));
		assertTrue("Could not find the link for Video", selenium.isElementPresent(safetyNetworkVideoLocator));
	}

	@Test
	public void test_select_vendor() {
		SafetyNetworkPage safetyNetworkPage = startAsCompany("halo").login().clickSafetyNetworkLink();
		safetyNetworkPage.selectVendorConnection(CONNECTION_NAME_1);
		assertTrue("Could open vendor connection profile", selenium.isElementPresent(connectionProfileName));
	}
	
	@Test
	public void test_select_customer() {
		SafetyNetworkPage safetyNetworkPage = startAsCompany("cglift").login().clickSafetyNetworkLink();
		SafetyNetworkImportPage safetyNetworkImportPage = safetyNetworkPage.selectCustomerConnection(CONNECTION_NAME_2);
		assertTrue("Could open customer Import page", safetyNetworkImportPage!=null);
	}

	//TODO wait for functionality.
	public void register_an_asset() {

	}

	public void test_view_preassigned_assets() {

	}

	// TODO Wait for functionality.
	public void test_search_for_assets_to_register_from_vendor() {
		SafetyNetworkPage safetyNetworkPage = startAsCompany("halo").login().clickSafetyNetworkLink();
		VendorConnectionProfilePage connectionProfilePage = safetyNetworkPage.selectVendorConnection(CONNECTION_NAME_1);

		connectionProfilePage.setAssetToSearchFor(ASSET_SERIAL_1);

	}

	// TODO Wait for functionality.
	public void test_view_all_assets_to_register_from_vendor() {
		SafetyNetworkPage safetyNetworkPage = startAsCompany("halo").login().clickSafetyNetworkLink();
		VendorConnectionProfilePage connectionProfilePage = safetyNetworkPage.selectVendorConnection(CONNECTION_NAME_1);

		connectionProfilePage.clickViewPreassignedAssets();
	}

	// TODO Next.
	public void test_view_catalog_and_import() {

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
		SafetyNetworkSettingsPage safetyNetworkSettingsPage = startAsCompany("msa").login().clickSafetyNetworkLink().clickSettings();

		// Starts out checked, so uncheck for testing.
		safetyNetworkSettingsPage.unCheckAutoAcceptCheckBox();
		safetyNetworkSettingsPage.unCheckAutoPublishCheckBox();

		safetyNetworkSettingsPage.saveSettings();

		assertTrue("Could not toggle Auto Accept", !safetyNetworkSettingsPage.isAutoAcceptChecked());
		assertTrue("Could not toggle Auto Publish", !safetyNetworkSettingsPage.isAutoPublishChecked());

		// Check for retesting...
		safetyNetworkSettingsPage.checkPublishAssetsBox();
		safetyNetworkSettingsPage.checkAutoAcceptBox();

		safetyNetworkSettingsPage.saveSettings();
	}

}
