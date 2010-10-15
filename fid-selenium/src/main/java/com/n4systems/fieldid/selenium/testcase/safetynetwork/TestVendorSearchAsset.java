package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.safetynetwork.AssetPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.SafetyNetworkVendorAssetListPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.VendorConnectionProfilePage;

public class TestVendorSearchAsset extends PageNavigatingTestCase<VendorConnectionProfilePage> {

	private static String PREASSIGNED_ASSET = "seaFitAsset";
	private static String NON_PUBLISHED_ASSET = "nonPublishedAsset";
	private static String NON_PREASSIGNED_ASSET = "nonPreassignedAsset";

	@Override
	protected VendorConnectionProfilePage navigateToPage() {
		return startAsCompany("seafit").login().clickSafetyNetworkLink().selectVendorConnection("HALO");
	}

	@Test
	public void test_search_with_no_result() throws Exception {
		page.setAssetToSearchFor("g");
		SafetyNetworkVendorAssetListPage result = page.clickSearchWithListResult();
		assertFalse(result.hasAssetList());
	}

	@Test
	public void test_simple_search_with_result() throws Exception {
		page.setAssetToSearchFor(PREASSIGNED_ASSET);
		AssetPage result = page.clickSearchWithSingleResult();

		assertTrue(result.checkHeader(PREASSIGNED_ASSET));
	}

	@Test
	// i.e no customer set on this asset, only organization.
	public void test_search_for_non_preassigned_asset() {
		page.setAssetToSearchFor(NON_PREASSIGNED_ASSET);
		AssetPage result = page.clickSearchWithSingleResult();

		assertTrue(result.checkHeader(NON_PREASSIGNED_ASSET));
	}

	@Test
	public void test_search_for_non_published_preassigned_asset() {
		page.setAssetToSearchFor(NON_PUBLISHED_ASSET);
		AssetPage result = page.clickSearchWithSingleResult();
		assertFalse("Unpublished asset found...", result.checkHeader(NON_PUBLISHED_ASSET));
	}

	@Test
	public void test_display_preassigned_assets() throws Exception {
		SafetyNetworkVendorAssetListPage list = page.clickViewPreassignedAssets();
		assertTrue(list.hasAssetList());
	}
}
