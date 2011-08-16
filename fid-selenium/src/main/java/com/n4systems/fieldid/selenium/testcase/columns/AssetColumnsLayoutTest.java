package com.n4systems.fieldid.selenium.testcase.columns;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.SetupPage;
import com.n4systems.fieldid.selenium.pages.setup.ColumnLayoutPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ExtendedFeature;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AssetColumnsLayoutTest extends PageNavigatingTestCase<ColumnLayoutPage> {

    @Override
    public void setupScenario(Scenario scenario) {
scenario.primaryOrgFor("test1").setExtendedFeatures(setOf(ExtendedFeature.Integration));
    }

    @Override
    protected ColumnLayoutPage navigateToPage() {
        return startAsCompany("test1").login().clickSetupLink().clickEditAssetColumnLayout();
    }

    @Test
    public void test_view_default_layout() {
        List<String> currentColumns = page.getCurrentColumns();
        List<String> expectedDefaultColumns = Arrays.asList("ID Number", "Asset Type", "Asset Status", "Customer Name", "Location",  "Date Identified", "Last Event Date", "Next Scheduled Date");
        assertEquals(expectedDefaultColumns, currentColumns);
    }

    @Test
    public void test_remove_some_columns_from_default_layout_and_save() {
        page.clickRemoveSelectedColumn("ID Number");
        page.clickRemoveSelectedColumn("Asset Type");
        page.clickRemoveSelectedColumn("Asset Status");
        page.clickRemoveSelectedColumn("Customer Name");
        page.clickRemoveSelectedColumn("Location");
        page.clickRemoveSelectedColumn("Last Event Date");
        page.clickRemoveSelectedColumn("Next Scheduled Date");

        assertEquals(Arrays.asList("Date Identified"), page.getCurrentColumns());

        SetupPage setupPage = page.clickSave();
        assertEquals("Layout successfully saved", setupPage.getActionMessages().get(0));

        page = setupPage.clickEditAssetColumnLayout();

        assertEquals(Arrays.asList("Date Identified"), page.getCurrentColumns());
    }

    @Test
    public void test_add_some_columns_and_save() throws Exception {
        assertFalse(page.getCurrentColumns().contains("Order Description"));
        page.expandColumnGroup("Order Details");
        page.clickAddColumn("Order Description");
        assertTrue(page.getCurrentColumns().contains("Order Description"));

        page = page.clickSave().clickEditAssetColumnLayout();

        assertTrue(page.getCurrentColumns().contains("Order Description"));
    }

}
