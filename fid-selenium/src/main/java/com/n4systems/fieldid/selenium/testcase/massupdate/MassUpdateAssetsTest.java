package com.n4systems.fieldid.selenium.testcase.massupdate;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.Asset;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.IdentifyPage;
import com.n4systems.fieldid.selenium.pages.assets.AssetsMassUpdatePage;
import com.n4systems.fieldid.selenium.pages.assets.AssetsSearchResultsPage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MassUpdateAssetsTest extends FieldIDTestCase {

    private HomePage page;

    @Before
    public void setUp() {
        page = startAsCompany("msa").login();
    }

    @Test
    public void test_mass_update_asset() throws Exception {
        // TODO: Replace identification at the beginning with data setup.
        String assetSerialNumber = MiscDriver.getRandomString(10);

        identifyAssetWithSerialNumber(assetSerialNumber, "Workman Harness", "PO 3", "In Service");
        identifyAssetWithSerialNumber(assetSerialNumber, "Workman Harness", "PO 4", "In Service");

        AssetsSearchPage assetsSearchPage = page.clickAssetsLink();

        assetsSearchPage.enterSerialNumber(assetSerialNumber);
        AssetsSearchResultsPage resultsPage = assetsSearchPage.clickRunSearchButton();

        assertEquals(2, resultsPage.getTotalResultsCount());

        resultsPage.selectAllItemsOnPage();

        AssetsMassUpdatePage massUpdatePage = resultsPage.clickMassUpdate();
        massUpdatePage.setAssetStatus("Out of Service");
        massUpdatePage.setPurchaseOrder("PO 5");

        resultsPage = massUpdatePage.clickSaveButtonAndConfirm();

        AssetPage assetPage = resultsPage.clickAssetLinkForResult(1);

        assertEquals("PO 5", assetPage.getPurchaseOrder());
        assertEquals("Out of Service", assetPage.getAssetStatus());

        assetPage.goBack();

        resultsPage.clickAssetLinkForResult(2);

        assertEquals("PO 5", assetPage.getPurchaseOrder());
        assertEquals("Out of Service", assetPage.getAssetStatus());
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
