package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.*;

import com.n4systems.fieldid.selenium.pages.WebEntity;
import org.junit.Ignore;
import org.junit.Test;

import com.n4systems.fieldid.selenium.assets.page.AssetPage;
import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

@Ignore
public class QuickRegisterAssetTest extends LoggedInTestCase {

	private static final String VENDOR_CONTEXT_SELECTOR = "css=#vendorContextNameLink a";
	
	@Test
	public void should_have_the_copy_and_register_asset_available_when_the_asset_is_found_in_a_vendor_context_and_has_not_been_registered() throws Exception {
		String contextToSwitchTo = "Unirope Ltd. Edmonton";
		switchToVendorContext(contextToSwitchTo);
		
		smartSearchFor("alex-share-2");
		
		assertTrue("copy asset link should be on the screen", selenium.isElementPresent("copyAsset"));
	}
	
	@Test
	public void should_have_the_message_that_the_asset_is_already_registered_when_the_asset_is_found_in_a_vendor_context_and_has_been_registered() throws Exception {
		String contextToSwitchTo = "Unirope Ltd. Edmonton";
		switchToVendorContext(contextToSwitchTo);
		
		smartSearchFor("UR 13875");
		
		assertTrue("asset already registered message should be on the screen", selenium.isElementPresent("alreadyRegisteredMessage"));
	}
	
	@Test
	public void should_not_have_any_regerstration_message_if_tracibilty_is_accessed_and_user_is_in_their_default_context() throws Exception {
		AssetPage assetPage = new AssetPage(selenium, misc);
		
		smartSearchFor("UR 13875");
	
		assetPage.gotoTraceability();
		
		assertFalse("there should not be any register or copy message on the screen", selenium.isElementPresent("registrationMessage"));
	}
	
	@Test
	public void should_have_asset_registration_information_on_the_screen_when_following_the_copy_and_register_asset() throws Exception {
		String contextToSwitchTo = "Unirope Ltd. Edmonton";
		searchForSerialNumberInVendorContext("alex-share-2", contextToSwitchTo);
		
		selenium.click("copyAsset");
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
		
		assertTrue("linked asset registration should be visible", selenium.isVisible("linkedAssetContainer"));
		assertEquals("alex-share-2", selenium.getText("linkedAssetSerial"));
	}
	
	@Test
	public void should_pushed_the_registration_information_to_the_local_when_following_the_copy_and_register_asset() throws Exception {
		String contextToSwitchTo = "Unirope Ltd. Edmonton";
		searchForSerialNumberInVendorContext("alex-share-2", contextToSwitchTo);
		
		selenium.click("copyAsset");
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
		
		assertTrue("linked asset registration should be visible", selenium.isVisible("linkedAssetContainer"));
		assertEquals("alex-share-2", selenium.getValue("serialNumberText"));
	}
	
	private void searchForSerialNumberInVendorContext(String serialNumber, String contextToSwitchTo) {
		switchToVendorContext(contextToSwitchTo);
		smartSearchFor(serialNumber);
	}

	private void smartSearchFor(String serialNumber) {
		selenium.type("searchText", serialNumber);
		selenium.click("smartSearchButton");
		selenium.waitForPageToLoad("30000");
	}

	private void switchToVendorContext(String contextToSwitchTo) {
		selenium.open("/fieldid/home.action");
		selenium.click(VENDOR_CONTEXT_SELECTOR);
		selenium.select("vendorContext", "label=" + contextToSwitchTo);
		selenium.waitForPageToLoad("30000");
	}

}
