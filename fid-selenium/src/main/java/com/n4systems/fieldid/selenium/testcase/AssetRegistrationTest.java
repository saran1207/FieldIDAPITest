package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;

import com.n4systems.fieldid.selenium.pages.WebEntity;
import org.junit.Ignore;
import org.junit.Test;

import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.util.SeleniumJavaScriptInteraction;

@Ignore
public class AssetRegistrationTest extends LoggedInTestCase {

	@Test
	public void should_fill_in_serial_number_on_asset_when_registering_an_asset() throws Exception {
		goToAssetAdd();
		lookupRegisterAsset("alex-share-2");
		
		assertEquals("alex-share-2", selenium.getValue("serialNumberText"));
	}

	private void goToAssetAdd() {
		selenium.open("/fieldid/productAdd.action");
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
	}
	
	@Test
	public void should_fill_in_rfid_number_on_asset_when_registering_an_asset() throws Exception {
		goToAssetAdd();
		
		lookupRegisterAsset("RFID-000000000000001");
		
		assertEquals("RFID-000000000000001", selenium.getValue("rfidNumber"));
	}
	
	@Test
	public void should_not_repopluate_registered_asset_information_to_the_local_asset_on_validation_error() throws Exception {
		goToAssetAdd();
		
		lookupRegisterAsset("alex-share-2");
		
		String invalidSerialNumber = "";
		selenium.type("serialNumberText", invalidSerialNumber);
		selenium.click("saveButton");
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
		assertEquals("", selenium.getValue("serialNumberText"));
	}
	
	@Test
	public void should_fill_in_reference_number_on_asset_when_registering_an_asset() throws Exception {
		goToAssetAdd();
		
		lookupRegisterAsset("CUSTOMER NUMBER-0000001");
		
		assertEquals("CUSTOMER NUMBER-0000001", selenium.getValue("customerRefNumber"));
	}

	private void lookupRegisterAsset(String identifyingNumber) {
		selenium.click("showSmartSearchLink");
		selenium.type("snSmartSearchText", identifyingNumber);
		selenium.click("snSmartSearchSubmit");
		selenium.waitForCondition("!" + SeleniumJavaScriptInteraction.CURRENT_WINDOW_IN_JAVASCRIPT + "$('networkSmartSearchContainer').visible()", WebEntity.DEFAULT_TIMEOUT);
	}

}
