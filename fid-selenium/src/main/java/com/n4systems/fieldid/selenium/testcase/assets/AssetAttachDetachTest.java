package com.n4systems.fieldid.selenium.testcase.assets;

import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.datatypes.Asset;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Before;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;

public class AssetAttachDetachTest extends FieldIDTestCase {

	private HomePage page;
	private static final String TEST_MASTER_IDENTIFIER = "123";
    private static final String TEST_SUB_IDENTIFIER = "123_SUB";
    private static final String TEST_ASSET_STATUS = "TEST STATUS";

	@Before
	public void setUp() {
		page = startAsCompany("test1").login();
	}

    @Override
    public void setupScenario(Scenario scenario) {
//        AssetType subType = scenario.anAssetType().named("Sub Type").build();
//        scenario.anAssetType()
//                .withSubTypes(subType)
//                .named("Master Type").build();
//
//        scenario.anAssetStatus()
//                .named(TEST_ASSET_STATUS).build();
    }

    //@Test
	public void attach_a_newly_created_subasset() {
		identifyAssetWithIdentifier(TEST_MASTER_IDENTIFIER, "Master Type", "PO 3", TEST_ASSET_STATUS);

		AssetPage masterAssetPage = page.search(TEST_MASTER_IDENTIFIER);

		assertTrue("Master component wasn't successfully created ", selenium.isElementPresent("//h1[contains(.,'Asset - " + TEST_MASTER_IDENTIFIER + "')]"));

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.addNewSubcomponent(TEST_SUB_IDENTIFIER);

		assertTrue("Sub component wasn't successfully created and attached", selenium.isElementPresent("//div[@class='subComponent']"));
	}

	//@Test
	public void detach_a_subasset() {
		identifyAssetWithIdentifier(TEST_MASTER_IDENTIFIER, "Master Type", "PO 3", TEST_ASSET_STATUS);
		
		AssetPage masterAssetPage = page.search(TEST_MASTER_IDENTIFIER);

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.addNewSubcomponent(TEST_SUB_IDENTIFIER);

		masterAssetPage.clickRemoveSubComponent();
		masterAssetPage.clickViewTab();
		
		assertFalse("Subcomponent wasn't successfully detached", selenium.isElementPresent("//h2[contains(., 'Sub-Assets')]"));
	}

	//@Test
	public void attach_an_existing_subasset() {
		identifyAssetWithIdentifier(TEST_MASTER_IDENTIFIER, "Master Type", "PO 3", TEST_ASSET_STATUS);
		identifyAssetWithIdentifier(TEST_SUB_IDENTIFIER, "Sub Type", "PO 3", TEST_ASSET_STATUS);

		AssetPage masterAssetPage = page.search(TEST_MASTER_IDENTIFIER);

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.attachExistingSubcomponent(TEST_SUB_IDENTIFIER);

		assertTrue("Sub component wasn't successfully found and attached", selenium.isElementPresent("//div[@class='subComponent']"));
	}

	private void identifyAssetWithIdentifier(String identifier, String assetType, String purchaseOrder, String status) {
		IdentifyPage identifyPage = page.clickIdentifyLink();
		Asset asset = new Asset();
		asset.setIdentifier(identifier);
		asset.setAssetType(assetType);
		asset.setPurchaseOrder(purchaseOrder);
		asset.setAssetStatus(status);

		identifyPage.setAddAssetForm(asset, false);
		identifyPage.saveNewAsset();
	}
}
