package com.n4systems.fieldid.selenium.testcase.columns;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;
import com.n4systems.fieldid.selenium.pages.assets.AssetsSearchResultsPage;
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

        AssetsSearchResultsPage searchResultsPage = searchPage.clickRunSearchButton();

        assertEquals(Arrays.asList("ID Number", "RFID Number"), searchResultsPage.getColumnNames());
        assertEquals("ID Number", searchResultsPage.getSortColumn());
        assertEquals(SortDirection.ASC, searchResultsPage.getSortDirection());
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

        AssetsSearchResultsPage resultsPage = columnLayoutPage.clickAssetsLink().clickRunSearchButton();

        assertEquals("RFID Number", resultsPage.getSortColumn());
        assertEquals(SortDirection.DESC, resultsPage.getSortDirection());
    }

    @Test
    public void test_sorting_in_results_pages() {
        AssetsSearchPage searchPage = startAsCompany("test1").login().clickAssetsLink();
        AssetsSearchResultsPage resultsPage = searchPage.clickRunSearchButton();

        assertEquals("ID Number", resultsPage.getSortColumn());
        assertEquals(SortDirection.ASC, resultsPage.getSortDirection());
        assertEquals("456789", resultsPage.getValueInCell(1, 1));
        assertEquals("456999", resultsPage.getValueInCell(2, 1));

        resultsPage.clickSortColumn("ID Number");
        
        assertEquals("ID Number", resultsPage.getSortColumn());
        assertEquals(SortDirection.DESC, resultsPage.getSortDirection());
        assertEquals("456999", resultsPage.getValueInCell(1, 1));
        assertEquals("456789", resultsPage.getValueInCell(2, 1));
    }

}
