package com.n4systems.fieldid.selenium.testcase.massupdate;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AssetPage;
import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.assets.AssetsMassUpdatePage;
import com.n4systems.fieldid.selenium.pages.assets.AssetsSearchResultsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import com.n4systems.model.builders.AssetBuilder;
import org.junit.Before;
import org.junit.Test;
import rfid.ejb.entity.AssetStatus;

import static org.junit.Assert.assertEquals;

public class MassUpdateAssetsTest extends FieldIDTestCase {

    private HomePage page;

    @Override
    public void setupScenario(Scenario scenario) {
        AssetType type = scenario.anAssetType()
                .named("Workman Harness")
                .build();

        AssetStatus status1 = scenario.anAssetStatus()
                .named("In Service")
                .build();

        scenario.anAssetStatus()
                .named("Out of Service")
                .build();

        AssetBuilder anAsset = scenario.anAsset()
                .withSerialNumber("123456")
                .ofType(type)
                .havingStatus(status1);

        anAsset.purchaseOrder("PO 3").build();
        anAsset.purchaseOrder("PO 4").build();
    }

    @Before
    public void setUp() {
        page = startAsCompany("test1").login();
    }

    @Test
    public void test_mass_update_asset() throws Exception {
        AssetsSearchPage assetsSearchPage = page.clickAssetsLink();

        assetsSearchPage.enterSerialNumber("123456");
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

}
