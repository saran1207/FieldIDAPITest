package com.n4systems.fieldid.selenium.testcase.columns;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.ReportingPage;
import com.n4systems.fieldid.selenium.pages.reporting.ReportingSearchResultsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventType;
import com.n4systems.model.Tenant;
import com.n4systems.model.columns.ColumnLayout;
import com.n4systems.model.columns.ReportType;
import com.n4systems.model.columns.SystemColumnMapping;
import com.n4systems.util.persistence.search.SortDirection;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SearchEventsWithCustomLayoutTest extends FieldIDTestCase {

    @Override
    public void setupScenario(Scenario scenario) {
        Tenant test1 = scenario.tenant("test1");

        SystemColumnMapping serialNumberColumn = scenario.systemColumnMapping("event_search_serialnumber");
        SystemColumnMapping rfidNumberColumn = scenario.systemColumnMapping("event_search_rfidnumber");

        AssetType type = scenario.anAssetType().named("TestType").build();

        Asset asset1 = scenario.anAsset().ofType(type).withSerialNumber("456999").rfidNumber("5678").build();
        Asset asset2 = scenario.anAsset().ofType(type).withSerialNumber("456789").rfidNumber("9876").build();

        EventForm eventForm = scenario.anEventForm().build();

        EventType eventType = scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test Event Type")
                .build();

		EventGroup group = scenario.anEventGroup()
				.forTenant(scenario.defaultTenant())
		        .build();

        createEvent(scenario, asset1, eventType, group);
        createEvent(scenario, asset2, eventType, group);

        ColumnLayout layout = scenario.aColumnLayout()
                .reportType(ReportType.EVENT)
                .sortColumn(serialNumberColumn)
                .sortDirection(SortDirection.ASC)
                .tenant(test1)
                .build();

        scenario.anActiveColumnMapping().order(1).columnLayout(layout).columnMapping(serialNumberColumn).build();
        scenario.anActiveColumnMapping().order(2).columnLayout(layout).columnMapping(rfidNumberColumn).build();
    }

    private void createEvent(Scenario scenario, Asset asset1, EventType eventType, EventGroup group) {
        scenario.anEvent().on(asset1)
                .ofType(eventType)
                .withPerformedBy(scenario.defaultUser())
                .withOwner(scenario.defaultPrimaryOrg())
                .withTenant(scenario.defaultTenant())
                .withGroup(group)
                .build();
    }

    @Test
    public void test_search_events_with_custom_layout() throws Exception {
        ReportingPage reportingPage = startAsCompany("test1").login().clickReportingLink();
        ReportingSearchResultsPage searchResultsPage = reportingPage.clickRunSearchButton();
        searchResultsPage.getColumnNames();

        assertEquals(Arrays.asList("Serial Number", "RFID Number"), searchResultsPage.getColumnNames());
        assertEquals("456789", searchResultsPage.getValueInCell(1, 1));
        assertEquals("456999", searchResultsPage.getValueInCell(2, 1));

        assertEquals("9876", searchResultsPage.getValueInCell(1, 2));
        assertEquals("5678", searchResultsPage.getValueInCell(2, 2));
    }

}
