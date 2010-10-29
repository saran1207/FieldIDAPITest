package com.n4systems.fieldid.selenium.testcase.assets;

import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.datatypes.Asset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;

public class AssetAttachDetachTest extends FieldIDTestCase {

	private HomePage page;
	private String masterSerial;
	private String subSerial;

	@Before
	public void setUp() {
		page = startAsCompany("illinois").login();
		masterSerial = MiscDriver.getRandomString(10);
		subSerial = masterSerial + "_Attached_Sub";
	}

	@After
	public void cleanUp() {
		// Remove master asset
		AssetPage assetToRemove = page.search(masterSerial);
		assetToRemove.clickEditTab().clickDelete();

		// Remove sub asset.
		assetToRemove = page.search(subSerial);
		assetToRemove.clickEditTab().clickDelete();
	}
    

	@Test
	public void attach_a_newly_created_subasset() {
		identifyAssetWithSerialNumber(masterSerial, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");

		AssetPage masterAssetPage = page.search(masterSerial);

		assertTrue("Master component wasn't successfully created ", selenium.isElementPresent("//h1[contains(.,'Asset - " + masterSerial + "')]"));

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.addNewSubcomponent(subSerial);

		assertTrue("Sub component wasn't successfully created and attached", selenium.isElementPresent("//div[@class='subComponent']"));
	}

	@Test
	public void detach_a_subasset() {
		identifyAssetWithSerialNumber(masterSerial, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");
		
		AssetPage masterAssetPage = page.search(masterSerial);

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.addNewSubcomponent(subSerial);

		masterAssetPage.clickRemoveSubComponent();
		masterAssetPage.clickViewTab();
		
		assertFalse("Subcomponent wasn't successfully detached", selenium.isElementPresent("//h2[contains(., 'Sub-Assets')]"));
	}

	@Test
	public void attach_an_existing_subasset() {
		identifyAssetWithSerialNumber(masterSerial, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");
		identifyAssetWithSerialNumber(subSerial, "Bridge", "PO 3", "OMG PLS");

		AssetPage masterAssetPage = page.search(masterSerial);

		masterAssetPage.clickSubComponentsTab();
		masterAssetPage.attachExistingSubcomponent(subSerial);

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
