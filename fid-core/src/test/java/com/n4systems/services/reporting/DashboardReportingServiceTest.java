package com.n4systems.services.reporting;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.FieldIdUnitTest;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.Status;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.DateRange;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.RangeType;


public class DashboardReportingServiceTest extends FieldIdUnitTest {

	private static final Long VALUE_FOR_JAN5 = 45L;
	private static final Long VALUE_FOR_JAN1 = 7851L;
	private static final Long VALUE_FOR_PADDING = 0L;
	
	private static final String STATUS_FOO = "Foo";
	private static final String STATUS_BAR = "bar";
	private static final String STATUS_HELLO = "hello";
	private static final String STATUS_WORLD = "world";
	
	@TestTarget private DashboardReportingService dashboardService; 
	@TestMock private AssetService assetService;
	@TestMock private EventService eventService;

	private LocalDate jan1 = new LocalDate(2011, 1, 1);
	private LocalDate jan2 = new LocalDate(2011, 1, 2);
	private LocalDate jan5 = new LocalDate(2011, 1, 5);
	
	private BaseOrg owner;
	private LocalDate jan1_2011 = new LocalDate().withYear(2011).withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1);

	
	@Override
	@Before 
	public void setUp() {
		super.setUp();
		owner = OrgBuilder.aCustomerOrg().build();
	}

	@Test
	public void test_getUpcomingScheduledEvents() {
		DateTimeUtils.setCurrentMillisFixed(jan1.toDate().getTime());
		
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		Integer period = 7;
		
		List<UpcomingScheduledEventsRecord> events = createUpcomingEventResults();
		expect(eventService.getUpcomingScheduledEvents(7, owner)).andReturn(events);
		expect(eventService.getUpcomingScheduledEvents(30, owner)).andReturn(events);
		expect(eventService.getUpcomingScheduledEvents(60, owner)).andReturn(events);
		expect(eventService.getUpcomingScheduledEvents(90, owner)).andReturn(events);
		replay(eventService);
		replay(assetService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getUpcomingScheduledEvents(period, owner);		

		assertEquals("only one ChartSeries in result set", 1, results.size());		
		assertEquals("expecting 7 days of points", period.intValue(), results.get(0).size());
		assertEquals(VALUE_FOR_JAN1, results.get(0).get(jan1).getY());
		assertEquals(VALUE_FOR_PADDING, results.get(0).get(jan2).getY());
		assertEquals(VALUE_FOR_JAN5, results.get(0).get(jan5).getY());

		period = 30;
		results = dashboardService.getUpcomingScheduledEvents(period, owner);
		assertEquals("expecting 30 days of points", period.intValue(), results.get(0).size());
		assertEquals(VALUE_FOR_JAN1, results.get(0).get(jan1).getY());
		assertEquals(VALUE_FOR_PADDING, results.get(0).get(jan2).getY());
		assertEquals(VALUE_FOR_JAN5, results.get(0).get(jan5).getY());
		
		period = 60;
		results = dashboardService.getUpcomingScheduledEvents(period, owner);
		assertEquals("expecting 60 days of points", period.intValue(), results.get(0).size());
		assertEquals(VALUE_FOR_JAN1, results.get(0).get(jan1).getY());
		assertEquals(VALUE_FOR_PADDING, results.get(0).get(jan2).getY());
		assertEquals(VALUE_FOR_JAN5, results.get(0).get(jan5).getY());
		
		period = 90;
		results = dashboardService.getUpcomingScheduledEvents(period, owner);
		assertEquals("expecting 90 days of points", period.intValue(), results.get(0).size());
		assertEquals(VALUE_FOR_JAN1, results.get(0).get(jan1).getY());
		assertEquals(VALUE_FOR_PADDING, results.get(0).get(jan2).getY());
		assertEquals(VALUE_FOR_JAN5, results.get(0).get(jan5).getY());
		
		verifyTestMocks();
		
	}
	
	@Test 
	public void test_getAssetsStatus() { 		
        DateRange dateRange = new DateRange(RangeType.FOREVER);
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();		
		List<AssetsStatusReportRecord> assetStatuses = createAssetStatusResults();
		expect(assetService.getAssetsStatus(dateRange.getFromDate(), dateRange.getToDate(), owner)).andReturn(assetStatuses);
		replay(assetService);
		replay(eventService);
		
		List<ChartSeries<String>> results = dashboardService.getAssetsStatus(dateRange, owner);
		
		assertEquals(1, results.size());
		assertEquals(assetStatuses.size(), results.get(0).size());		

		verifyTestMocks();		
	}	
	
	@Test 
	public void test_getAssetsStatusWithOther() { 		
        DateRange dateRange = new DateRange(RangeType.FOREVER);
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();		
		List<AssetsStatusReportRecord> assetStatuses = createAssetStatusResults();
		int sizeOfBigData = assetStatuses.size();
		assetStatuses.add(new AssetsStatusReportRecord("otherStuff", 1L));  // these two will be lumped into one "other" group.		
		assetStatuses.add(new AssetsStatusReportRecord("otherStuff2", 1L));		
		assetStatuses.add(new AssetsStatusReportRecord("otherStuff3", 1L));		
		expect(assetService.getAssetsStatus(dateRange.getFromDate(), dateRange.getToDate(), owner)).andReturn(assetStatuses);
		replay(assetService);
		replay(eventService);
		
		List<ChartSeries<String>> results = dashboardService.getAssetsStatus(dateRange, owner);
		
		assertEquals(1, results.size());
		assertEquals(sizeOfBigData+1, results.get(0).size());  // add one for the Other section (which aggregrates the 3 smaller valued records).		

		verifyTestMocks();		
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void test_getAssetsStatus_null_range() { 
		List<ChartSeries<String>> results = dashboardService.getAssetsStatus(null, owner);		
	}	

	@Test
	public void test_getAssetsIdentified() { 
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		ChartGranularity granularity = ChartGranularity.WEEK;
		List<AssetsIdentifiedReportRecord> assets = createAssetsIdentifiedResults(granularity);
        DateRange dateRange = new DateRange(RangeType.THIS_YEAR);
		expect(assetService.getAssetsIdentified(granularity, granularity.roundDown(dateRange.getFrom()).toDate(), granularity.roundUp(dateRange.getTo()).toDate(), owner)).andReturn(assets);
		replay(assetService);
		replay(eventService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getAssetsIdentified(dateRange, granularity, owner);
		
		assertEquals("only one ChartSeries in result set", 1, results.size());		
		assertEquals("expecting 54 points in the ChartSeries", 53, results.get(0).size());
		
		verifyTestMocks();
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
        DateRange dateRange = new DateRange(RangeType.THIS_YEAR);
		
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
		
		expect(eventService.getCompletedEvents(dateRange.calculateFromDate(), dateRange.calculateToDate(), owner, (Status)null, granularity)).andReturn(allEvents);
		expect(eventService.getCompletedEvents(dateRange.calculateFromDate(), dateRange.calculateToDate(), owner, Status.FAIL, granularity)).andReturn(failedEvents);
		expect(eventService.getCompletedEvents(dateRange.calculateFromDate(), dateRange.calculateToDate(), owner, Status.NA, granularity)).andReturn(naEvents);
		expect(eventService.getCompletedEvents(dateRange.calculateFromDate(), dateRange.calculateToDate(), owner, Status.PASS, granularity)).andReturn(passedEvents);
		replay(eventService);
		replay(assetService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getCompletedEvents(dateRange, granularity, owner);
		
		assertEquals("expect ChartSeries for All, Pass, NA, Fail", 4, results.size());
		assertEquals("All", results.get(0).getLabel());
		assertEquals(Status.PASS.getDisplayName(), results.get(1).getLabel());
		assertEquals(Status.FAIL.getDisplayName(), results.get(2).getLabel());
		assertEquals(Status.NA.getDisplayName(), results.get(3).getLabel());
		
		verifyTestMocks();		
	}
		
	@Test 
	@Ignore
	public void test_EventCompleteness() { 
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		ChartGranularity granularity = ChartGranularity.WEEK;
        DateRange dateRange = new DateRange(RangeType.LAST_MONTH);
		BaseOrg org = OrgBuilder.aDivisionOrg().build();
		
		List<EventCompletenessReportRecord> completedEvents = createEventCompletenessResults(granularity, 21L);
		List<EventCompletenessReportRecord> allEvents = Lists.newArrayList();
		allEvents.addAll(completedEvents);
		allEvents.addAll(createEventCompletenessResults(granularity, 888L, 574L, 924L));
		expect(eventService.getEventCompleteness(granularity, granularity.roundDown(dateRange.getFrom()).toDate(), jan1_2011.toDate(), org)).andReturn(allEvents);
		expect(eventService.getEventCompleteness(ScheduleStatus.COMPLETED, granularity, granularity.roundDown(dateRange.getFrom()).toDate(), jan1_2011.toDate(), org)).andReturn(completedEvents);
		replay(eventService);
		replay(assetService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getEventCompletenessEvents(granularity, dateRange, org);
		
		assertEquals(2, results.size());
		assertEquals("All", results.get(0).getLabel());
		assertEquals("Completed", results.get(1).getLabel());
		
		verifyTestMocks();		
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

	private List<AssetsIdentifiedReportRecord> createAssetsIdentifiedResults(ChartGranularity granularity) {
		AssetsIdentifiedReportRecord record = new AssetsIdentifiedReportRecord(45L, granularity.toString(), 2011, 1, 1 );
		AssetsIdentifiedReportRecord record2 = new AssetsIdentifiedReportRecord(4385L, granularity.toString(), 2011, 3, 2 );		
		return Lists.newArrayList(record, record2);
	}
	
	private List<UpcomingScheduledEventsRecord> createUpcomingEventResults() {
		UpcomingScheduledEventsRecord event1 = new UpcomingScheduledEventsRecord(jan1, VALUE_FOR_JAN1);
		UpcomingScheduledEventsRecord event2 = new UpcomingScheduledEventsRecord(jan5, VALUE_FOR_JAN5);
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
