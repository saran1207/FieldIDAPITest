package com.n4systems.fieldid.selenium.testcase.safetynetwork;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.safetynetwork.AssetPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.SafetyNetworkVendorAssetListPage;
import com.n4systems.fieldid.selenium.pages.safetynetwork.VendorConnectionProfilePage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestVendorSearchAsset_DbSetup extends PageNavigatingTestCase<VendorConnectionProfilePage> {

    private static String PREASSIGNED_ASSET = "seaFitAsset";
	private static String NON_PUBLISHED_ASSET = "nonPublishedAsset";
	private static String NON_PREASSIGNED_ASSET = "nonPreassignedAsset";

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.aSafetyNetworkConnection()
                .from(scenario.primaryOrgFor("seafit"))
                .to(scenario.primaryOrgFor("iti"))
                .type(TypedOrgConnection.ConnectionType.VENDOR)
                .build();

        AssetBuilder builder = scenario.anAsset()
                .forTenant(scenario.tenant("iti"))
                .ofType(scenario.assetType("iti", TEST_ASSET_TYPE_1));

        builder.published(true)
                .withOwner(scenario.customerOrg("iti", "seafit"))
                .withSerialNumber(PREASSIGNED_ASSET)
                .build();

        builder.published(true)
                .withOwner(scenario.primaryOrgFor("iti"))
                .withSerialNumber(NON_PREASSIGNED_ASSET)
                .build();

        builder.published(false)
                .withOwner(scenario.customerOrg("iti", "seafit"))
                .withSerialNumber(NON_PUBLISHED_ASSET)
                .build();
    }

	@Override
	protected VendorConnectionProfilePage navigateToPage() {
		return startAsCompany("seafit").login().clickSafetyNetworkLink().selectVendorConnection("iti");
	}

	@Test
    @Ignore
	public void test_search_with_no_result() throws Exception {
		page.setAssetToSearchFor("g");
		SafetyNetworkVendorAssetListPage result = page.clickSearchWithListResult();
		assertFalse(result.hasAssetList());
	}

	@Test
    @Ignore
	public void test_simple_search_with_result() throws Exception {
		page.setAssetToSearchFor(PREASSIGNED_ASSET);
		AssetPage result = page.clickSearchWithSingleResult();

		assertTrue(result.checkHeader(PREASSIGNED_ASSET));
	}

	@Test
    @Ignore
	// i.e no customer set on this asset, only organization.
	public void test_search_for_non_preassigned_asset() {
		page.setAssetToSearchFor(NON_PREASSIGNED_ASSET);
		AssetPage result = page.clickSearchWithSingleResult();

		assertTrue(result.checkHeader(NON_PREASSIGNED_ASSET));
	}

	@Test
    @Ignore
	public void test_search_for_non_published_preassigned_asset() {
		page.setAssetToSearchFor(NON_PUBLISHED_ASSET);
		AssetPage result = page.clickSearchWithSingleResult();
		assertFalse("Unpublished asset found...", result.checkHeader(NON_PUBLISHED_ASSET));
	}

	@Test
    @Ignore // Preassigned list not working with data setup yet....
	public void test_display_preassigned_assets() throws Exception {
		SafetyNetworkVendorAssetListPage list = page.clickViewPreassignedAssets();
		assertTrue(list.hasAssetList());
	}
}
