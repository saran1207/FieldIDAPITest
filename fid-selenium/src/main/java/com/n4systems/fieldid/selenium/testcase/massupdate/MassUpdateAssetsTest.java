package com.n4systems.fieldid.selenium.testcase.massupdate;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.Product;
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
    public void test_mass_update_product() throws Exception {
        // TODO: Replace identification at the beginning with data setup.
        String productSerialNumber = MiscDriver.getRandomString(10);

        identifyAssetWithSerialNumber(productSerialNumber, "Workman Harness", "PO 3", "In Service");
        identifyAssetWithSerialNumber(productSerialNumber, "Workman Harness", "PO 4", "In Service");

        AssetsSearchPage assetsSearchPage = page.clickAssetsLink();

        assetsSearchPage.enterSerialNumber(productSerialNumber);
        AssetsSearchResultsPage resultsPage = assetsSearchPage.clickRunSearchButton();

        assertEquals(2, resultsPage.getTotalResultsCount());

        AssetsMassUpdatePage massUpdatePage = resultsPage.clickMassUpdate();
        massUpdatePage.setProductStatus("Out of Service");
        massUpdatePage.setPurchaseOrder("PO 5");

        resultsPage = massUpdatePage.clickSaveButton();

        AssetPage assetPage = resultsPage.clickAssetLinkForResult(1);

        assertEquals("PO 5", assetPage.getPurchaseOrder());
        assertEquals("Out of Service", assetPage.getProductStatus());

        assetPage.goBack();

        resultsPage.clickAssetLinkForResult(2);

        assertEquals("PO 5", assetPage.getPurchaseOrder());
        assertEquals("Out of Service", assetPage.getProductStatus());
    }

    private void identifyAssetWithSerialNumber(String serial, String productType, String purchaseOrder, String status) {
        IdentifyPage identifyPage = page.clickIdentifyLink();
        Product asset = new Product();
        asset.setSerialNumber(serial);
        asset.setProductType(productType);
        asset.setPurchaseOrder(purchaseOrder);
        asset.setProductStatus(status);

        identifyPage.setAddAssetForm(asset, false);
        identifyPage.saveNewAsset();
    }

}
