package com.n4systems.fieldid.minimaldata.security;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;

@RunWith(Parameterized.class)
public class SafetyNetworkLookup extends LoggedInTestCase {

	@Parameters
	public static Collection<Object[]> data() {
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		data.add(new Object[]{ "admin", "makemore$" });  // primary org employee
		data.add(new Object[]{ "secondary1", "makemore$" });  // secondary org employee
		
		return data;
	}

	public SafetyNetworkLookup(String userName, String password) {
		super(userName, password);
		setInitialCompany("test-distributor");
	}

	@Test
	public void should_find_and_view_event_for_an_asset_available_when_published_against_the_primary_org() throws Exception {
		String serialNumber = "Vendor-00001-published";
		findAssetThroughSafetyNetworkSmartSearch(serialNumber);
		assertAssetFound(serialNumber);
		
		goToTheEventListForAsset();
		
		openViewOfEvent();
		
		assertEventIsDisplayed(serialNumber);
		
		closeViewOfEvent();
	}

	@Test
	public void should_find_and_view_event_for_an_asset_available_when_published_and_assigned_to_the_linked_customer() throws Exception {
		String serialNumber = "Vendor-00003-published";
		
		findAssetThroughSafetyNetworkSmartSearch(serialNumber);
		assertAssetFound(serialNumber);
		
		goToTheEventListForAsset();
		
		openViewOfEvent();
		
		assertEventIsDisplayed(serialNumber);
		
		closeViewOfEvent();
	}
	
	@Test
	public void should_find_and_view_event_for_an_asset_available_when_published_against_the_secondary_org() throws Exception {
		String serialNumber = "Vendor-00008-published";
		
		findAssetThroughSafetyNetworkSmartSearch(serialNumber);
		assertAssetFound(serialNumber);
		
		goToTheEventListForAsset();
		
		openViewOfEvent();
		
		assertEventIsDisplayed(serialNumber);
		
		closeViewOfEvent();
	}
	
	@Test
	public void should_find_and_view_event_for_an_asset_published_against_the_primary_vendor_and_registerd_to_the_primary_distributor() throws Exception {
		String serialNumber = "Vendor-00009-registerd";
		
		findAssetThroughSafetyNetworkSmartSearch(serialNumber);
		assertAssetFound(serialNumber);
		
		goToTheEventListForAsset();
		
		openViewOfEvent();
		
		assertEventIsDisplayed(serialNumber);
		
		closeViewOfEvent();
	}
	
	@Test
	public void should_find_and_view_event_for_an_asset_available_when_published_and_assigned_to_the_linked_customer_and_registered() throws Exception {
		String serialNumber = "Vendor-00010-registerd-assigned";
		
		findAssetThroughSafetyNetworkSmartSearch(serialNumber);
		assertAssetFound(serialNumber);
		
		goToTheEventListForAsset();
		
		openViewOfEvent();
		
		assertEventIsDisplayed(serialNumber);
		
		closeViewOfEvent();
	}
	
	@Test
	public void should_find_and_view_event_for_an_asset_available_when_published_against_the_secondary_org_and_registered_against_the_primary_distributor() throws Exception {
		String serialNumber = "Vendor-00011-registerd-secondary";
		
		findAssetThroughSafetyNetworkSmartSearch(serialNumber);
		assertAssetFound(serialNumber);
		
		goToTheEventListForAsset();
		
		openViewOfEvent();
		
		assertEventIsDisplayed(serialNumber);
		
		closeViewOfEvent();
	}
	
	private void assertEventIsDisplayed(String serialNumber) {
		assertTrue("serial number was not displayed on the event", selenium.isTextPresent(serialNumber));
		assertTrue("event type was not displayed on the event", selenium.isTextPresent("Chain Visual"));
	}

	private void closeViewOfEvent() {
		selenium.selectFrame("relative=up");
	}

	private void openViewOfEvent() {
		selenium.click("link=view");
		selenium.waitForElementToBePresent("css=#lightviewContent");
		
		selenium.selectFrame("css=#lightviewContent");
		selenium.waitForElementToBePresent("css=#event");
	}

	private void goToTheEventListForAsset() {
		selenium.clickAndWaitForPageLoad("link=Events");
		assertTrue(selenium.isTextPresent("Chain Visual"));
	}

	private void findAssetThroughSafetyNetworkSmartSearch(String serialNumber) {
		misc.setVendorContext("Test Vendor");
		misc.setSmartSearch(serialNumber);
		misc.submitSmartSearch();
	}

	private void assertAssetFound(String serialNumber) {
		assertTrue("Asset could not be found, Serial Number is not in the title", selenium.isElementPresent("css=h1:contains('"+ serialNumber + "')"));
	}
	
}
