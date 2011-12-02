package com.n4systems.services.reporting;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.Status;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.dashboard.FieldIdUnitTest;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;


public class DashboardReportingServiceTest extends FieldIdUnitTest {

	private static final String STATUS_FOO = "Foo";
	private static final String STATUS_BAR = "bar";
	private static final String STATUS_HELLO = "hello";
	private static final String STATUS_WORLD = "world";
	
	@TestTarget private DashboardReportingService dashboardService; 
	@TestMock private AssetService assetService;
	@TestMock private EventService eventService;
	
	private BaseOrg owner;

	
	@Override
	@Before 
	public void setUp() {
		super.setUp();
		owner = OrgBuilder.aCustomerOrg().build();
	}

	@Ignore // will be fixed after 2443 finished.
	@Test
	public void test_getUpcomingScheduledEvents() { 
		Integer period = 30;
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		List<UpcomingScheduledEventsRecord> events = createUpcomingEventResults();
		expect(eventService.getUpcomingScheduledEvents(period, owner)).andReturn(events);
		replay(eventService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getUpcomingScheduledEvents(period, owner);
		
		assertEquals("only one ChartSeries in result set", 1, results.size());		
		assertEquals("expecting 30 days of points", period+1, results.get(0).size());  // currently code is inclusive hence the "period+1" 
	}
	
	@Test 
	public void test_getAssetsStatus() { 		
		ChartDateRange dateRange = ChartDateRange.FOREVER;
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();		
		List<AssetsStatusReportRecord> assetStatuses = createAssetStatusResults();
		expect(assetService.getAssetsStatus(dateRange.getFromDate(), dateRange.getToDate(), owner)).andReturn(assetStatuses);
		replay(assetService);
		
		List<ChartSeries<String>> results = dashboardService.getAssetsStatus(dateRange, owner);
		
		assertEquals(1, results.size());
		assertEquals(assetStatuses.size()+1, results.get(0).size());  // note that it currently adds "Other" section to results so expect one more.		
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void test_getAssetsStatus_null_range() { 
		List<ChartSeries<String>> results = dashboardService.getAssetsStatus(null, owner);		
	}	

	@Test
	public void test_getAssetsIdentified() { 
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		List<AssetsIdentifiedReportRecord> assets = createAssetsIdentifiedResults();
		ChartGranularity granularity = ChartGranularity.WEEK;
		ChartDateRange dateRange = ChartDateRange.THIS_YEAR;
		expect(assetService.getAssetsIdentified(granularity, granularity.roundDown(dateRange.getFrom()).toDate(), granularity.roundUp(dateRange.getTo()).toDate(), owner)).andReturn(assets);
		replay(assetService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getAssetsIdentified(dateRange, granularity, owner);
		
		assertEquals("only one ChartSeries in result set", 1, results.size());		
		assertEquals("expecting 54 points in the ChartSeries", 54, results.get(0).size());  //0-53		
	}

	@Test(expected=IllegalArgumentException.class)
	public void test_getAssetsIdentified_null_date() { 
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		ChartGranularity granularity = ChartGranularity.DAY;
		
		List<ChartSeries<LocalDate>> results = dashboardService.getAssetsIdentified(null, granularity, owner);		
	}	
	
	
	@Test
	public void test_getCompletedEvents() { 
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		ChartGranularity granularity = ChartGranularity.DAY;
		ChartDateRange dateRange = ChartDateRange.THIS_YEAR;
		
		int failedCount = 3;
		int passCount = 7;
		int naCount = 1;
		int allCount = failedCount + naCount + passCount;
		List<CompletedEventsReportRecord> failedEvents = createCompletedEventsResults(Status.FAIL, failedCount);
		List<CompletedEventsReportRecord> passedEvents = createCompletedEventsResults(Status.PASS, passCount);
		List<CompletedEventsReportRecord> naEvents = createCompletedEventsResults(Status.NA, naCount);
		List<CompletedEventsReportRecord> allEvents = new ArrayList<CompletedEventsReportRecord>();
		allEvents.addAll(failedEvents);
		allEvents.addAll(naEvents);
		allEvents.addAll(passedEvents);
		
		expect(eventService.getCompletedEvents(dateRange.getFromDate(), dateRange.getToDate(), owner, (Status)null, granularity)).andReturn(allEvents);
		expect(eventService.getCompletedEvents(dateRange.getFromDate(), dateRange.getToDate(), owner, Status.FAIL, granularity)).andReturn(failedEvents);
		expect(eventService.getCompletedEvents(dateRange.getFromDate(), dateRange.getToDate(), owner, Status.NA, granularity)).andReturn(naEvents);
		expect(eventService.getCompletedEvents(dateRange.getFromDate(), dateRange.getToDate(), owner, Status.PASS, granularity)).andReturn(passedEvents);
		replay(eventService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getCompletedEvents(dateRange, granularity, owner);
		
		assertEquals("expect ChartSeries for All, Pass, NA, Fail", 4, results.size());
		assertEquals("All", results.get(0).getLabel());
		assertEquals(Status.PASS.getDisplayName(), results.get(1).getLabel());
		assertEquals(Status.FAIL.getDisplayName(), results.get(2).getLabel());
		assertEquals(Status.NA.getDisplayName(), results.get(3).getLabel());
	}
		
	@Test 
	public void test_EventCompleteness() { 
		ChartGranularity granularity = ChartGranularity.WEEK;
		ChartDateRange dateRange = ChartDateRange.LAST_MONTH;
		BaseOrg org = OrgBuilder.aDivisionOrg().build();
		
		List<EventCompletenessReportRecord> completedEvents = createEventCompletenessResults(granularity, 21L);
		List<EventCompletenessReportRecord> allEvents = Lists.newArrayList();
		allEvents.addAll(completedEvents);
		allEvents.addAll(createEventCompletenessResults(granularity, 888L, 574L, 924L));
		expect(eventService.getEventCompleteness(granularity, granularity.roundDown(dateRange.getFrom()).toDate(), granularity.roundUp(dateRange.getTo()).toDate(), org)).andReturn(allEvents);
		expect(eventService.getEventCompleteness(ScheduleStatus.COMPLETED, granularity, dateRange.getFromDate(), dateRange.getToDate(), org)).andReturn(completedEvents);
		replay(eventService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getEventCompletenessEvents(granularity, dateRange, org);
		
		assertEquals(2, results.size());
		assertEquals("All", results.get(0).getLabel());
		assertEquals("Completed", results.get(1).getLabel());
	}
	
	private List<EventCompletenessReportRecord> createEventCompletenessResults(ChartGranularity granularity, Long... values) {
		List<EventCompletenessReportRecord> results = Lists.newArrayList();
		for (Long value:values) { 
			EventCompletenessReportRecord record = new EventCompletenessReportRecord(value, granularity.toString(),2011,1,1);
			results.add(record);
		}
		return results;
	}

	@Test(expected=IllegalArgumentException.class)
	public void test_getEventCompleteness_null_range() { 
		List<ChartSeries<LocalDate>> results = dashboardService.getEventCompletenessEvents(ChartGranularity.DAY, null, owner);		
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void test_getCompletedEvents_null_range() { 
		List<ChartSeries<LocalDate>> results = dashboardService.getCompletedEvents(null, ChartGranularity.DAY, owner);		
	}	

	private List<CompletedEventsReportRecord> createCompletedEventsResults(Status fail, int count) {
		List<CompletedEventsReportRecord> results = Lists.newArrayList();
		for (int i=0; i<count; i++) {
			results.add(new CompletedEventsReportRecord(34L, ChartGranularity.MONTH.toString(), 2011, 1, 1+count));
		}
		return results;
	}

	private List<AssetsIdentifiedReportRecord> createAssetsIdentifiedResults() {
		AssetsIdentifiedReportRecord record = new AssetsIdentifiedReportRecord(45L, ChartGranularity.MONTH.toString(), 2011, 1, 1 );
		AssetsIdentifiedReportRecord record2 = new AssetsIdentifiedReportRecord(4385L, ChartGranularity.MONTH.toString(), 2011, 3, 2 );		
		return Lists.newArrayList(record, record2);
	}
	
	private List<UpcomingScheduledEventsRecord> createUpcomingEventResults() {
		LocalDate jan1 = new LocalDate(2011, 1, 1);
		LocalDate jan5 = new LocalDate(2011, 1, 5);
		UpcomingScheduledEventsRecord event1 = new UpcomingScheduledEventsRecord(jan1, 7851L);
		UpcomingScheduledEventsRecord event2 = new UpcomingScheduledEventsRecord(jan5, 45L);
		return Lists.newArrayList(event1, event2);
	}

	private List<AssetsStatusReportRecord> createAssetStatusResults() {		
		AssetsStatusReportRecord status1 = new AssetsStatusReportRecord(STATUS_FOO, 454L);
		AssetsStatusReportRecord status2 = new AssetsStatusReportRecord(STATUS_BAR, 154L);
		AssetsStatusReportRecord status3 = new AssetsStatusReportRecord(STATUS_HELLO, 971L);
		AssetsStatusReportRecord status4 = new AssetsStatusReportRecord(STATUS_WORLD, 653L);
		return Lists.newArrayList(status1, status2, status3, status4);
	}
	
}
