package com.n4systems.fieldid.selenium.testcase.assets;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;

public class AssetEditDeleteTest extends FieldIDTestCase {

	private HomePage page;
	private String masterSerial;
	private Product asset;
	private AssetPage assetPage;
	private static String NEW_SERIAL = "newSerial";
	private static String NEW_PURCHASE_ORDER = "newPurchaseOrder";
	private static String NEW_STATUS = "Out of Service";
	private static String NEW_PRODUCT_TYPE = "Bridge";
	private static String NEW_MAKEMODEL = "newAttribute1";
	private static String NEW_TYPE = "newAttribute2";
	private static String NEW_DRIVETYPE = "newAttribute3";

	@Before
	public void setUp() {
		page = startAsCompany("illinois").login();
		masterSerial = MiscDriver.getRandomString(10);
	}

	@Test
	public void delete_asset() {
		identifyAssetWithSerialNumber(masterSerial, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");
		assetPage = page.search(masterSerial);
		assetPage.clickEditTab().clickDelete();

		assertTrue("Asset wasn't successfully deleted", selenium.isElementPresent("//span[contains(., 'The product has been deleted.')]"));
	}

	@Test
	public void edit_asset() {
		identifyAssetWithSerialNumber(masterSerial, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");
		assetPage = page.search(masterSerial).clickEditTab();
		assetPage.setMiscDriver(misc);

		prepareNewAssetFields();

		assetPage.clickSave();

		assertTrue("Serial successfully edited", assetPage.getSerialNumber().equals(NEW_SERIAL));

		AssetPage masterAssetPage = page.search(NEW_SERIAL);
		masterAssetPage.clickEditTab().clickDelete();
	}

	public void edit_asset_attributes() {
		identifyAssetWithSerialNumber(masterSerial, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");
		
		assetPage = page.search(masterSerial).clickEditTab();
		
		prepareNewAssetAttributes();
		
		assetPage.clickSave();
	}

	private void identifyAssetWithSerialNumber(String serial, String productType, String purchaseOrder, String status) {
		IdentifyPage identifyPage = page.clickIdentifyLink();
		asset = new Product();
		asset.setSerialNumber(serial);
		asset.setProductType(productType);
		asset.setPurchaseOrder(purchaseOrder);
		asset.setProductStatus(status);

		identifyPage.setAddAssetForm(asset, false);
		identifyPage.saveNewAsset();
	}

	private void prepareNewAssetFields() {
		asset = new Product();
		asset.setSerialNumber(NEW_SERIAL);
		asset.setProductType(NEW_PRODUCT_TYPE);
		asset.setPurchaseOrder(NEW_PURCHASE_ORDER);
		asset.setProductStatus(NEW_STATUS);
		assetPage.setAssetForm(asset);
	}

	private void prepareNewAssetAttributes() {
		selenium.type("//div[@infofieldname='Make/Model']/span/input", NEW_MAKEMODEL);
		selenium.type("//div[@infofieldname='Type']/span/input", NEW_TYPE);
		selenium.type("//div[@infofieldname='Drive Type']/span/input", NEW_DRIVETYPE);
	}
}
