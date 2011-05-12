package com.n4systems.fieldid.selenium.testcase.columns;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ColumnLayoutPage;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ScheduleColumnsLayoutTest extends PageNavigatingTestCase<ColumnLayoutPage> {

    @Override
    protected ColumnLayoutPage navigateToPage() {
        return startAsCompany("test1").login().clickSetupLink().clickEditScheduleColumnLayout();
    }

    @Test
    public void test_default_reporting_columns() {
        List<String> currentColumns = page.getCurrentColumns();
        List<String> expectedDefaultColumns = Arrays.asList("Scheduled Date", "Status", "Days Past Due", "Serial Number", "Asset Type", "Customer Name", "Location", "Event Type", "Last Event Date");
        assertEquals(expectedDefaultColumns, currentColumns);
    }

}
