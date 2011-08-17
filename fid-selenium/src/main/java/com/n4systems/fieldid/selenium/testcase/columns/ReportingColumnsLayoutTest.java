package com.n4systems.fieldid.selenium.testcase.columns;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ColumnLayoutPage;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReportingColumnsLayoutTest extends PageNavigatingTestCase<ColumnLayoutPage> {

    @Override
    protected ColumnLayoutPage navigateToPage() {
        return startAsCompany("test1").login().clickSetupLink().clickEditReportingColumnLayout();
    }

    @Test
    public void test_default_reporting_columns() throws Exception {
        List<String> currentColumns = page.getCurrentColumns();
        List<String> expectedDefaultColumns = Arrays.asList("Date Performed", "ID Number", "Customer Name", "Location", "Asset Type", "Asset Status", "Event Type", "Performed By", "Result");
        assertEquals(expectedDefaultColumns, currentColumns);
    }

    @Test
    public void test_reorder_reporting_columns_and_save() throws Exception {
        page.clearLayout();
        page.expandColumnGroup("Event Information");
        page.clickAddColumn("Date Performed");
        page.clickAddColumn("Event Book");
        page.clickAddColumn("Performed By");

        assertEquals(Arrays.asList("Performed By", "Event Book", "Date Performed"), page.getCurrentColumns());

        page.moveColumnToPosition("Date Performed", 1);

        assertEquals(Arrays.asList("Date Performed", "Performed By", "Event Book"), page.getCurrentColumns());

        page = page.clickSave().clickEditReportingColumnLayout();
        assertEquals(Arrays.asList("Date Performed", "Performed By", "Event Book"), page.getCurrentColumns());
    }

}
