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
    public void test_default_reporting_columns() {
        List<String> currentColumns = page.getCurrentColumns();
        List<String> expectedDefaultColumns = Arrays.asList("Event Type", "Result", "Serial Number", "Customer Name", "Asset Type", "Asset Status");
        assertEquals(expectedDefaultColumns, currentColumns);
    }

}
