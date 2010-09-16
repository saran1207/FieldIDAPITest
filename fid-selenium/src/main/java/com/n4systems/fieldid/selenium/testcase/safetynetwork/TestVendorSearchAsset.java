package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkVendorAssetListPage;
import com.n4systems.fieldid.selenium.safetynetwork.page.VendorConnectionProfilePage;

public class TestVendorSearchAsset extends PageNavigatingTestCase<VendorConnectionProfilePage> {

	@Override
	protected VendorConnectionProfilePage navigateToPage() {
        return startAsCompany("halo").login().clickSafetyNetworkLink().selectVendorConnection("Sea-Fit");
    }
	
	@Test
	public void test_search_with_no_result() throws Exception {
		page.setAssetToSearchFor("g");
		SafetyNetworkVendorAssetListPage result = page.clickSearch();
		assertFalse(result.hasAssetList());
	}
	
	@Test
	public void test_search_with_result() throws Exception {
		page.setAssetToSearchFor("A04");
		SafetyNetworkVendorAssetListPage result = page.clickSearch();
		assertTrue(result.hasAssetList());	
		assertEquals(1, result.getAssetList().size());
		assertEquals("A04", result.getAssetList().get(0).getSerialNumber());
	}
	
	@Test
	public void test_display_preassigned_assets() throws Exception {
		SafetyNetworkVendorAssetListPage list = page.clickViewPreassignedAssets();
		assertTrue(list.hasAssetList());
	}
}
