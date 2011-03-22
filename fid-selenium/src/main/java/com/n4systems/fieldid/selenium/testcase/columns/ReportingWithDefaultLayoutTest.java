package com.n4systems.fieldid.selenium.testcase.columns;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.n4systems.fieldid.selenium.pages.reporting.ReportingSearchResultsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ReportingWithDefaultLayoutTest extends FieldIDTestCase {

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.aSimpleEvent().build();
    }

    @Test
    public void test_reporting_with_default_layout() throws Exception {
        ReportingPage reportingPage = startAsCompany("test1").login().clickReportingLink();

        ReportingSearchResultsPage reportingResultsPage = reportingPage.clickRunSearchButton();

        assertEquals(Arrays.asList("Event Type", "Result", "Serial Number", "Customer Name", "Asset Type", "Asset Status"), reportingResultsPage.getColumnNames());
    }

}
