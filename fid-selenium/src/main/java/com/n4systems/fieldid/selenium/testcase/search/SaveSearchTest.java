package com.n4systems.fieldid.selenium.testcase.search;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;
import com.n4systems.fieldid.selenium.pages.ManageSavedItemsPage;
import com.n4systems.fieldid.selenium.pages.search.SaveSearchPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SaveSearchTest extends PageNavigatingTestCase<AssetsSearchPage> {

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.anAsset().ofType(scenario.assetType("test1", TEST_ASSET_TYPE_1)).withIdentifier("12345").build();
    }

    @Override
    protected AssetsSearchPage navigateToPage() {
        return startAsCompany("test1").login().clickAssetsLink();
    }

    @Test
    public void testSaveSimpleSearch() {
        page.enterIdentifier("12345");
        page.clickRunSearchButton();
        SaveSearchPage saveSearchPage = page.clickSaveSearch();
        saveSearchPage.setReportName("Simple Search");
        AssetsSearchPage assetsSearchPage = saveSearchPage.clickSave();

        assertTrue(assetsSearchPage.getActionMessages().contains("Your Search has been saved."));
        ManageSavedItemsPage manageSavedItemsPage = assetsSearchPage.clickSavedItems().clickManageSavedItems();

        final List<String> savedItemNames = manageSavedItemsPage.getSavedItemNames();
        assertEquals(1, savedItemNames.size());
        assertEquals("Simple Search", savedItemNames.get(0));
    }

}
