package com.n4systems.fieldid.selenium.testcase.columns;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;
import com.n4systems.fieldid.selenium.pages.setup.ColumnLayoutPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import com.n4systems.model.columns.ColumnLayout;
import com.n4systems.model.columns.ReportType;
import com.n4systems.model.columns.SystemColumnMapping;
import com.n4systems.util.persistence.search.SortDirection;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SearchAssetsWithCustomLayoutTest extends FieldIDTestCase {

    @Override
    public void setupScenario(Scenario scenario) {
        Tenant test1 = scenario.tenant("test1");

        SystemColumnMapping identifierColumn = scenario.systemColumnMapping("asset_search_identifier");
        SystemColumnMapping rfidNumberColumn = scenario.systemColumnMapping("asset_search_rfidnumber");

        AssetType type = scenario.anAssetType().named("TestType").build();

        scenario.anAsset().ofType(type).withIdentifier("456999").rfidNumber("5678").build();
        scenario.anAsset().ofType(type).withIdentifier("456789").rfidNumber("9876").build();
        ColumnLayout layout = scenario.aColumnLayout()
                .reportType(ReportType.ASSET)
                .sortColumn(identifierColumn)
                .sortDirection(SortDirection.ASC)
                .tenant(test1)
                .build();

        scenario.anActiveColumnMapping().order(1).columnLayout(layout).columnMapping(identifierColumn).build();
        scenario.anActiveColumnMapping().order(2).columnLayout(layout).columnMapping(rfidNumberColumn).build();
    }

    @Test
    public void test_search_with_custom_layout() {
        AssetsSearchPage searchPage = startAsCompany("test1").login().clickAssetsLink();
        searchPage.clickRunSearchButton();

        assertEquals(Arrays.asList("ID Number", "RFID Number"), searchPage.getColumnNames());
        assertEquals("ID Number", searchPage.getSortColumn());
        assertEquals(SortDirection.ASC, searchPage.getSortDirection());
    }

    @Test
    public void test_change_sort_column_and_direction() {
        ColumnLayoutPage columnLayoutPage = startAsCompany("test1").login().clickSetupLink().clickEditAssetColumnLayout();

        assertEquals("ID Number", columnLayoutPage.getSortColumnName());
        assertEquals(SortDirection.ASC, columnLayoutPage.getSortDirection());

        columnLayoutPage.selectSortColumn("RFID Number");
        columnLayoutPage.selectSortDirection(SortDirection.DESC);
        columnLayoutPage = columnLayoutPage.clickSave().clickEditAssetColumnLayout();

        assertEquals("RFID Number", columnLayoutPage.getSortColumnName());
        assertEquals(SortDirection.DESC, columnLayoutPage.getSortDirection());

        final AssetsSearchPage assetsSearchPage = columnLayoutPage.clickAssetsLink();
        assetsSearchPage.clickRunSearchButton();

        assertEquals("RFID Number", assetsSearchPage.getSortColumn());
        assertEquals(SortDirection.DESC, assetsSearchPage.getSortDirection());
    }

    @Test
    public void test_sorting_in_results_pages() {
        AssetsSearchPage searchPage = startAsCompany("test1").login().clickAssetsLink();
        searchPage.clickRunSearchButton();

        assertEquals("ID Number", searchPage.getSortColumn());
        assertEquals(SortDirection.ASC, searchPage.getSortDirection());
        assertEquals("456789", searchPage.getValueInCell(1, 1));
        assertEquals("456999", searchPage.getValueInCell(2, 1));

        searchPage.clickSortColumn("ID Number");
        
        assertEquals("ID Number", searchPage.getSortColumn());
        assertEquals(SortDirection.DESC, searchPage.getSortDirection());
        assertEquals("456999", searchPage.getValueInCell(1, 1));
        assertEquals("456789", searchPage.getValueInCell(2, 1));
    }

}
