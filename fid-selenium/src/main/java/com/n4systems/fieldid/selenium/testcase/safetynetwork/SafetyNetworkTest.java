package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.SafetyNetworkPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.CustomerConnectionProfilePage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.SafetyNetworkCatalogImportPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.SafetyNetworkCatalogPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.SafetyNetworkInvitePage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.SafetyNetworkSettingsPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.VendorConnectionProfilePage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.model.safetynetwork.TypedOrgConnection;

public class SafetyNetworkTest extends FieldIDTestCase {

	// Locators
	private String safetyNetworkPageHeaderLocator = "//h1[contains(text(),'Safety Network')]";
	private String safetyNetworkInboxLocator = "//a[@id='inbox']";
	private String safetyNetworkCatalogLocator = "//a[@id='manageCatalog']";
	private String safetyNetworkSettingsLocator = "//a[@id='privacySettings']";
	private String safetyNetworkOverviewLocator = "//a[@id='help_link']";
	private String safetyNetworkVideoLocator = "//a[@id='video_link']";

	// Asset Types
	private static final String ASSET_TYPE_CHECKBOX_NAME_1 = "TestType1";

	// Connection names
	private static String COMPANY1 = "test1";
	private static String COMPANY2 = "test2";
	
	@Override
	public void setupScenario(Scenario scenario) {
        scenario.aSafetyNetworkConnection()
        		.from(scenario.primaryOrgFor(COMPANY1))
        		.to(scenario.primaryOrgFor(COMPANY2))
        		.type(TypedOrgConnection.ConnectionType.VENDOR)
        		.build();
		
		Catalog catalog = new Catalog(scenario.tenant(COMPANY1));
		
		Set<AssetType> assetTypes = new HashSet<AssetType>();
		assetTypes.add(scenario.assetType(COMPANY1, "TestType1"));
		assetTypes.add(scenario.assetType(COMPANY1, "TestType2"));
		catalog.setPublishedAssetTypes(assetTypes);
		
		scenario.save(catalog);		
	}

	@Test
	public void test_invite_page() {
		SafetyNetworkPage safetyNetworkPage = startAsCompany(COMPANY1).login().clickSafetyNetworkLink();
		SafetyNetworkInvitePage invitePage = safetyNetworkPage.clickInvite();
		invitePage.sendEmail();
		assertTrue("Could not open Invitation page", invitePage != null);

	}

	@Test
	public void test_static_page_contents() {
		SafetyNetworkPage safetyNetworkPage = startAsCompany(COMPANY1).login().clickSafetyNetworkLink();
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
		SafetyNetworkPage safetyNetworkPage = startAsCompany(COMPANY1).login().clickSafetyNetworkLink();
		VendorConnectionProfilePage profile = safetyNetworkPage.selectVendorConnection(COMPANY2);
		assertEquals("Could open vendor connection profile", profile.getHeaderText(), COMPANY2);
	}

	@Test
	public void test_select_customer() {
		SafetyNetworkPage safetyNetworkPage = startAsCompany(COMPANY2).login().clickSafetyNetworkLink();
		CustomerConnectionProfilePage safetyNetworkCustomerPage = safetyNetworkPage.selectCustomerConnection(COMPANY1);
		assertTrue("Could open customer connection profile", safetyNetworkCustomerPage != null);
	}

	@Test
	public void test_view_catalog_and_import() {
		SafetyNetworkPage safetyNetworkPage = startAsCompany(COMPANY2).login().clickSafetyNetworkLink();
		CustomerConnectionProfilePage safetyNetworkCustomerPage = safetyNetworkPage.selectCustomerConnection(COMPANY1);

		SafetyNetworkCatalogImportPage safetyNetworkCatalogImportPage = safetyNetworkCustomerPage.clickViewCatalog();
		
		safetyNetworkCatalogImportPage.clickSelectItemsToImportButton();
		
		safetyNetworkCatalogImportPage.clickFirstAssetTypeCheckBox();
		
		safetyNetworkCatalogImportPage.clickContinue();
		
		safetyNetworkCatalogImportPage.clickStartImport();
		
		assertTrue("Could not complete catalog import", selenium.isElementPresent("//input[@id='importDone']"));
		
	}

	@Test
	public void test_publish_catalog() {
		SafetyNetworkCatalogPage safetyNetworkCatalogPage = startAsCompany(COMPANY2).login().clickSafetyNetworkLink().goToCatalog();

		safetyNetworkCatalogPage.checkAssetTypeCheckBox(ASSET_TYPE_CHECKBOX_NAME_1);

		safetyNetworkCatalogPage.submitForm();
		assertTrue("Could not toggle Asset Type", safetyNetworkCatalogPage.isChecked(ASSET_TYPE_CHECKBOX_NAME_1));

		// Uncheck for retesting...
		safetyNetworkCatalogPage.unCheckAssetTypeCheckBox(ASSET_TYPE_CHECKBOX_NAME_1);
		safetyNetworkCatalogPage.submitForm();
	}

	@Test
	public void test_settings_and_privacy() {
		SafetyNetworkSettingsPage safetyNetworkSettingsPage = startAsCompany(COMPANY1).login().clickSafetyNetworkLink().clickSettings();

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
