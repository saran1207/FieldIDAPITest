package com.n4systems.fieldid.selenium.testcase.assets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.n4systems.fieldid.selenium.datatypes.Asset;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

public class AssetEditDeleteTest extends FieldIDTestCase {

	private HomePage page;
	private Asset asset;
	private AssetPage assetPage;
    private static final String TEST_SERIAL_NUMBER = "oldSerial";
    private static final String MERGE_SERIAL_NUMBER = "oldSerial_MERGED";
	private static final String NEW_SERIAL = "newSerial";
	private static final String NEW_PURCHASE_ORDER = "newPurchaseOrder";
	private static final String NEW_STATUS = "Out of Service";
	private static final String NEW_ASSET_TYPE = "Bridge";
	private static final String NEW_MAKEMODEL = "newAttribute1";
	private static final String NEW_ATTR_VALUE = "Val2";

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.anAssetStatus()
                .named("OMG PLS")
                .forTenant(scenario.defaultTenant())
                .build();

        scenario.anAssetStatus()
                .named(NEW_STATUS)
                .forTenant(scenario.defaultTenant())
                .build();

        InfoOptionBean opt1 = scenario.anInfoOption()
                .withName("Val1").build();

        InfoOptionBean opt2 = scenario.anInfoOption()
                .withName("Val2").build();

        InfoFieldBean field1 = scenario.anInfoField()
                .withName("Make/Model")
                .build();

        InfoFieldBean field2 = scenario.anInfoField()
                .withName("SelectOpt")
                .type(InfoFieldBean.SELECTBOX_FIELD_TYPE)
                .withOptions(opt1, opt2)
                .build();

        scenario.anAssetType()
                .named("Gantry Crane - Cab Controlled")
                .forTenant(scenario.defaultTenant())
                .withFields(field1, field2)
                .build();
    }

    @Before
	public void setUp() {
		page = startAsCompany("test1").login();
	}

	@Test
	public void delete_asset() {
		identifyAssetWithSerialNumber(TEST_SERIAL_NUMBER, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");

		assetPage = page.search(TEST_SERIAL_NUMBER);
		assetPage.clickEditTab().clickDelete();

		assertTrue("Asset wasn't successfully deleted", selenium.isElementPresent("//span[contains(., 'The asset has been deleted.')]"));
	}

	@Test
	public void edit_asset() {
		identifyAssetWithSerialNumber(TEST_SERIAL_NUMBER, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");

		assetPage = page.search(TEST_SERIAL_NUMBER).clickEditTab();
		assetPage.setMiscDriver(misc);

		prepareNewAssetFields();

		assetPage.clickSave();

		assertTrue("Serial unsuccessfully edited", assetPage.getSerialNumber().equals(NEW_SERIAL));

		AssetPage masterAssetPage = page.search(NEW_SERIAL);
		masterAssetPage.clickEditTab().clickDelete();
	}

	@Test
	public void edit_asset_attributes() {
		identifyAssetWithSerialNumber(TEST_SERIAL_NUMBER, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");

		assetPage = page.search(TEST_SERIAL_NUMBER).clickEditTab();

        assetPage.enterAttributeValue("Make/Model", NEW_MAKEMODEL);
        assetPage.selectAttributeValue("SelectOpt", NEW_ATTR_VALUE);

		assetPage.clickSave();
		assertEquals("attribute should be edited", NEW_MAKEMODEL, assetPage.getValueForAttribute("Make/Model"));
		assertEquals("attribute should be edited", NEW_ATTR_VALUE, assetPage.getValueForAttribute("SelectOpt"));

		AssetPage masterAssetPage = page.search(TEST_SERIAL_NUMBER);
		masterAssetPage.clickEditTab().clickDelete();
	}

	@Test
	public void merge_two_assets() {
		identifyAssetWithSerialNumber(TEST_SERIAL_NUMBER, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");
		identifyAssetWithSerialNumber(MERGE_SERIAL_NUMBER, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");

		assetPage = page.search(TEST_SERIAL_NUMBER).clickEditTab();

		assetPage.clickMerge();
		assetPage.loadAssetToMergeIntoAndSubmit(MERGE_SERIAL_NUMBER);

		assertTrue("Merge was not successful.", assetPage.wasMergeSuccessful(TEST_SERIAL_NUMBER, MERGE_SERIAL_NUMBER));

		page.search(MERGE_SERIAL_NUMBER);
	}

	private void identifyAssetWithSerialNumber(String serial, String assetType, String purchaseOrder, String status) {
		IdentifyPage identifyPage = page.clickIdentifyLink();
		asset = new Asset();
		asset.setSerialNumber(serial);
		asset.setAssetType(assetType);
		asset.setPurchaseOrder(purchaseOrder);
		asset.setAssetStatus(status);

		identifyPage.setAddAssetForm(asset, false);
		identifyPage.saveNewAsset();
	}

	private void prepareNewAssetFields() {
		asset = new Asset();
		asset.setSerialNumber(NEW_SERIAL);
		asset.setAssetType(NEW_ASSET_TYPE);
		asset.setPurchaseOrder(NEW_PURCHASE_ORDER);
		asset.setAssetStatus(NEW_STATUS);
		assetPage.setAssetForm(asset);
	}

}
