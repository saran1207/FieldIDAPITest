package com.n4systems.fieldid.selenium.testcase.events;

import com.n4systems.fieldid.selenium.datatypes.Asset;
import com.n4systems.fieldid.selenium.pages.EventPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;

public class EventCreateEditRemoveTest extends FieldIDTestCase {

	private HomePage page;
	private AssetPage assetPage;
	private EventPage eventPage;
	private String masterSerial;
	private String subSerial;

	private String masterEventType = "Crane - Cab Controlled";

	@Before
	public void setUp() {
		page = startAsCompany("illinois").login();
		masterSerial = MiscDriver.getRandomString(10);
		subSerial = masterSerial + "_Sub";
		identifyAssetWithSerialNumber(masterSerial, "Gantry Crane - Cab Controlled", "PO 3", "OMG PLS");

		assetPage = page.search(masterSerial);
	}

	@After
	public void cleanUp() {
		assetPage = page.search(masterSerial);
		assetPage.clickEditTab().clickDelete();
	}

	@Test
	public void create_master_event_no_sub_events() {
		eventPage = assetPage.clickEventsTab().clickManageEvents().clickStartNewEvent(masterEventType);
		
		performMandatoryEvent();

		eventPage.clickSave();

		selenium.isElementPresent("//span[contains(.,'Master Event Saved.')]");
	}

	//Test is timing out...
	public void create_master_with_sub_event() {
		assetPage.clickSubComponentsTab();
		assetPage.addNewSubcomponent(subSerial);
		
		eventPage = assetPage.clickEventsTab().clickManageEvents().clickStartNewEvent(masterEventType);

		performMandatoryEvent();
		performSubEvent();

		eventPage.clickSave();

		selenium.isElementPresent("//span[contains(.,'Master Event Saved.')]");

		assetPage = page.search(subSerial);
		assetPage.clickEditTab().clickDelete();
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

	private void performMandatoryEvent() {
		eventPage.clickMandatoryEventToPerformLink();
		eventPage.clickStore();
	}

	private void performSubEvent() {
		eventPage.clickSubStartEventLink();
		eventPage.clickStore();
	}

}
