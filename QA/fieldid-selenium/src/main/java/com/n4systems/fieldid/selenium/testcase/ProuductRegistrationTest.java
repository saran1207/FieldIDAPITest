package com.n4systems.fieldid.selenium.testcase;

import org.junit.Ignore;
import org.junit.Test;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.n4systems.fieldid.selenium.util.SeleniumJavaScriptInteraction;

@Ignore
public class ProuductRegistrationTest extends LoggedInTest {

	public ProuductRegistrationTest() {
		super("n4systems", "makemore$");
	}
	

	
	
	@Test
	public void should_fill_in_serial_number_on_asset_when_regerstering_an_asset() throws Exception {
		goToProductAdd();
		lookupRegisterAsset("alex-share-2");
		
		assertEquals("alex-share-2", selenium.getValue("serialNumberText"));
	}

	private void goToProductAdd() {
		selenium.open("/fieldid/productAdd.action");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
	}
	
	@Test
	public void should_fill_in_rfid_number_on_asset_when_regerstering_an_asset() throws Exception {
		goToProductAdd();
		
		lookupRegisterAsset("RFID-000000000000001");
		
		assertEquals("RFID-000000000000001", selenium.getValue("rfidNumber"));
	}
	
	@Test
	public void should_not_repopluate_registered_product_information_to_the_local_product_on_validation_error() throws Exception {
		goToProductAdd();
		
		lookupRegisterAsset("alex-share-2");
		
		String invalidSerialNumber = "";
		selenium.type("serialNumberText", invalidSerialNumber);
		selenium.click("saveButton");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		assertEquals("", selenium.getValue("serialNumberText"));
		
	}
	
	
	@Test
	public void should_fill_in_reference_number_on_asset_when_regerstering_an_asset() throws Exception {
		goToProductAdd();
		
		lookupRegisterAsset("CUSTOMER NUMBER-0000001");
		
		assertEquals("CUSTOMER NUMBER-0000001", selenium.getValue("customerRefNumber"));
	}


	private void lookupRegisterAsset(String identifyingNumber) {
		selenium.click("showSmartSearchLink");
		selenium.type("snSmartSearchText", identifyingNumber);
		selenium.click("snSmartSearchSubmit");
		selenium.waitForCondition("!" + SeleniumJavaScriptInteraction.CURRENT_WINDOW_IN_JAVASCRIPT + "$('networkSmartSearchContainer').visible()", Misc.defaultTimeout);
	}


}
