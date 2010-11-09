package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertFalse;

import com.n4systems.fieldid.selenium.pages.setup.ManageAssetTypesPage;
import org.junit.Before;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;

public abstract class ManageAssetTypesTestCase extends PageNavigatingTestCase<ManageAssetTypesPage> {
	
	protected static final String TEST_ASSET_TYPE_NAME = "Test";
	protected static final String TEST_ASSET_TYPE_WARNINGS = "Test Warnings";
	protected static final String TEST_ASSET_TYPE_INSTRUCTIONS = "Test Instructions";
	protected static final String TEST_ASSET_TYPE_CAUTIONS_URL = "http://cautions.url.com/";
	protected static final String TEST_ASSET_TYPE_MANUFACTURER_CERTIFICATE_TEXT = "Test Manufacturer Certificate Text";
	protected static final String TEST_ASSET_TYPE_ASSET_DESCRIPTION_TEMPLATE = "Test Asset Description Template";

	@Override
	protected ManageAssetTypesPage navigateToPage() {
		return startAsCompany("aacm").login().clickSetupLink().clickAssetTypes();
	}
	
	@Before
	public void cleanUp() {
		// TODO: Remove when data setup is done.
		removeTestAssetTypeIfNecessary(TEST_ASSET_TYPE_NAME);
	}
	
	protected void addTestAssetType() {
		page.clickAddTab();
		page.enterName(TEST_ASSET_TYPE_NAME);
		page.enterWarnings(TEST_ASSET_TYPE_WARNINGS);
		page.enterInstructions(TEST_ASSET_TYPE_INSTRUCTIONS);
		page.enterCautionsUrl(TEST_ASSET_TYPE_CAUTIONS_URL);
		page.checkHasManufacturerCertificate();
		page.enterManufacturerCertificateText(TEST_ASSET_TYPE_MANUFACTURER_CERTIFICATE_TEXT);
		page.enterAssetDescriptionTemplate(TEST_ASSET_TYPE_ASSET_DESCRIPTION_TEMPLATE);
		page.clickSaveAssetType();
		page.clickViewAllTab();
	}
	
	private void removeTestAssetTypeIfNecessary(String typeName) {
		if(page.getAssetTypes().contains(typeName)) {
			page.clickEditAssetType(typeName);
			page.clickDeleteAssetType();
		}
		assertFalse(page.getAssetTypes().contains(typeName));
	}

}
