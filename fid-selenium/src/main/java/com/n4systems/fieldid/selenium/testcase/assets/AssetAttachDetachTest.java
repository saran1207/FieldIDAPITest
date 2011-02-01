package com.n4systems.fieldid.selenium.testcase.assets;

import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.datatypes.Asset;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;

public class AssetAttachDetachTest extends FieldIDTestCase {

	private HomePage page;
	private static final String TEST_MASTER_SERIAL = "123";
    private static final String TEST_SUB_SERIAL = "123_SUB";
    private static final String TEST_ASSET_STATUS = "TEST STATUS";

	@Before
	public void setUp() {
		page = startAsCompany("test1").login();
	}

    @Override
    public void setupScenario(Scenario scenario) {
        AssetType subType = scenario.anAssetType().named("Sub Type").build();
        scenario.anAssetType()
                .withSubTypes(subType)
                .named("Master Type").build();

        scenario.anAssetStatus()
                .named(TEST_ASSET_STATUS).build();
    }

    @Test
	public void attach_a_newly_created_subasset() {
		identifyAssetWithSerialNumber(TEST_MASTER_SERIAL, "Master Type", "PO 3", TEST_ASSET_STATUS);

		AssetPage masterAssetPage = page.search(TEST_MASTER_SERIAL);

		assertTrue("Master component wasn't successfully created ", selenium.isElementPresent("//h1[contains(.,'Asset - " + TEST_MASTER_SERIAL + "')]"));

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.addNewSubcomponent(TEST_SUB_SERIAL);

		assertTrue("Sub component wasn't successfully created and attached", selenium.isElementPresent("//div[@class='subComponent']"));
	}

	@Test
	public void detach_a_subasset() {
		identifyAssetWithSerialNumber(TEST_MASTER_SERIAL, "Master Type", "PO 3", TEST_ASSET_STATUS);
		
		AssetPage masterAssetPage = page.search(TEST_MASTER_SERIAL);

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.addNewSubcomponent(TEST_SUB_SERIAL);

		masterAssetPage.clickRemoveSubComponent();
		masterAssetPage.clickViewTab();
		
		assertFalse("Subcomponent wasn't successfully detached", selenium.isElementPresent("//h2[contains(., 'Sub-Assets')]"));
	}

	@Test
	public void attach_an_existing_subasset() {
		identifyAssetWithSerialNumber(TEST_MASTER_SERIAL, "Master Type", "PO 3", TEST_ASSET_STATUS);
		identifyAssetWithSerialNumber(TEST_SUB_SERIAL, "Sub Type", "PO 3", TEST_ASSET_STATUS);

		AssetPage masterAssetPage = page.search(TEST_MASTER_SERIAL);

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.attachExistingSubcomponent(TEST_SUB_SERIAL);

		assertTrue("Sub component wasn't successfully found and attached", selenium.isElementPresent("//div[@class='subComponent']"));
	}

	private void identifyAssetWithSerialNumber(String serial, String assetType, String purchaseOrder, String status) {
		IdentifyPage identifyPage = page.clickIdentifyLink();
		Asset asset = new Asset();
		asset.setSerialNumber(serial);
		asset.setAssetType(assetType);
		asset.setPurchaseOrder(purchaseOrder);
		asset.setAssetStatus(status);

		identifyPage.setAddAssetForm(asset, false);
		identifyPage.saveNewAsset();
	}
}
