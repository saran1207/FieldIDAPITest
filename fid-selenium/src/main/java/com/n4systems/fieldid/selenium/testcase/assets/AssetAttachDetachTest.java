package com.n4systems.fieldid.selenium.testcase.assets;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.Product;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;

public class AssetAttachDetachTest extends FieldIDTestCase {

	private HomePage page;
	private String masterSerial;
	private String subSerial;
	private static String INSPECTION_TYPE = "Gantry Crane - Cab Controlled";

	@Before
	public void setUp() {
		page = startAsCompany("illinois").login();
		masterSerial = MiscDriver.getRandomString(10);
		subSerial = masterSerial + "_Attached_Sub";
	}

	@After
	public void cleanUp() {
		// Remove sub asset.
		AssetPage assetToRemove = page.search(subSerial);
		assetToRemove.clickEditTab().clickDelete();

		// Remove master asset
		assetToRemove = page.search(masterSerial);
		assetToRemove.clickEditTab().clickDelete();
	}

	@Test
	public void attach_a_newly_created_subproduct() {
		identifyAssetWithSerialNumber(masterSerial, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");

		AssetPage masterAssetPage = page.search(masterSerial);

		assertTrue("Master component wasn't successfully created ", selenium.isElementPresent("//h1[contains(.,'Asset - " + masterSerial + "')]"));

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.addNewSubcomponent(subSerial);

		assertTrue("Sub component wasn't successfully created and attached", selenium.isElementPresent("//div[@class='subComponent']"));
	}

	@Test
	public void detach_a_subproduct() {

		identifyAssetWithSerialNumber(masterSerial, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");
		AssetPage masterAssetPage = page.search(masterSerial);

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.addNewSubcomponent(subSerial);

		masterAssetPage.clickRemoveSubComponent();
		masterAssetPage.clickViewTab();
		assertFalse("Subcomponent wasn't successfully detached", selenium.isElementPresent("//h2[contains(., 'Sub-Products')]"));
	}

	// Lightbox is timing out after clicking find existing link...
	public void attach_an_existing_subproduct() {
		identifyAssetWithSerialNumber(masterSerial, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");
		identifyAssetWithSerialNumber(subSerial, "Bridge", "PO 3", "OMG PLS");

		AssetPage masterAssetPage = page.search(masterSerial);

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.attachExistingSubcomponent(subSerial);

		assertTrue("Sub component wasn't successfully found and attached", selenium.isElementPresent("//div[@class='subComponent']"));
	}

	private void identifyAssetWithSerialNumber(String serial, String productType, String purchaseOrder, String status) {
		IdentifyPage identifyPage = page.clickIdentifyLink();
		Product asset = new Product();
		asset.setSerialNumber(serial);
		asset.setProductType(productType);
		asset.setPurchaseOrder(purchaseOrder);
		asset.setProductStatus(status);

		identifyPage.setAddAssetForm(asset, false);
		identifyPage.saveNewAsset();
	}
}
