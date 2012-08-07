package com.n4systems.services.reporting;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.FieldIdServicesUnitTest;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Status;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.date.DateService;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.RangeType;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;


public class DashboardReportingServiceTest extends FieldIdServicesUnitTest {

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
    @TestMock private DateService dateService;

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
        setCurrentMillisFixed(jan1.toDate().getTime());

        BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		Integer period = 7;
		
		List<UpcomingScheduledEventsRecord> events = createUpcomingEventResults();
		expect(eventService.getUpcomingScheduledEvents(7, owner)).andReturn(events);
		expect(eventService.getUpcomingScheduledEvents(30, owner)).andReturn(events);
		expect(eventService.getUpcomingScheduledEvents(60, owner)).andReturn(events);
		expect(eventService.getUpcomingScheduledEvents(90, owner)).andReturn(events);
		replay(eventService);
		replay(assetService);

        ChartSeries<LocalDate> results = dashboardService.getUpcomingScheduledEvents(period, owner);

		assertEquals(events.size(), results.size());
		assertEquals(VALUE_FOR_JAN1, results.get(jan1).getY());
		assertEquals(VALUE_FOR_JAN5, results.get(jan5).getY());

        period = 30;
		results = dashboardService.getUpcomingScheduledEvents(period, owner);
		assertEquals(events.size(), results.size());
		assertEquals(VALUE_FOR_JAN1, results.get(jan1).getY());
		assertEquals(VALUE_FOR_JAN5, results.get(jan5).getY());

        period = 60;
		results = dashboardService.getUpcomingScheduledEvents(period, owner);
		assertEquals(events.size(), results.size());
		assertEquals(VALUE_FOR_JAN1, results.get(jan1).getY());
		assertEquals(VALUE_FOR_JAN5, results.get(jan5).getY());

        period = 90;
		results = dashboardService.getUpcomingScheduledEvents(period, owner);
		assertEquals(events.size(), results.size());
		assertEquals(VALUE_FOR_JAN1, results.get(jan1).getY());
		assertEquals(VALUE_FOR_JAN5, results.get(jan5).getY());
		
		verify(eventService, assetService);
	}
	
	@Test 
	public void test_getAssetsStatus() { 		
        DateRange dateRange = new DateRange(RangeType.FOREVER);
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();		
		List<AssetsStatusReportRecord> assetStatuses = createAssetStatusResults(STATUS_FOO, STATUS_BAR, STATUS_HELLO, STATUS_WORLD);
        Date from = LocalDate.now().toDate();
        Date to = LocalDate.now().plusDays(7).toDate();

        expect(dateService.calculateFromDate(dateRange)).andReturn(from);
        expect(dateService.calculateToDate(dateRange)).andReturn(to);
        replay(dateService);

        expect(assetService.getAssetsStatus(from, to, owner)).andReturn(assetStatuses);
		replay(assetService);

        ChartSeries<String> results = dashboardService.getAssetsStatus(dateRange, owner);
		
		assertEquals(assetStatuses.size(), results.size());
        assertEquals(assetStatuses.get(0), results.get(STATUS_FOO));
        assertEquals(assetStatuses.get(1), results.get(STATUS_BAR));
        assertEquals(assetStatuses.get(2), results.get(STATUS_HELLO));
        assertEquals(assetStatuses.get(3), results.get(STATUS_WORLD));

		verify(dateService, assetService);
	}	
	
	@Test 
	public void test_getAssetsStatusWithOther() { 		
        DateRange dateRange = new DateRange(RangeType.FOREVER);
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();		
		List<AssetsStatusReportRecord> assetStatuses = createAssetStatusResults();
		int sizeOfBigData = assetStatuses.size();
        Date from = LocalDate.now().toDate();
        Date to = LocalDate.now().plusDays(7).toDate();

        expect(dateService.calculateFromDate(dateRange)).andReturn(from);
        expect(dateService.calculateToDate(dateRange)).andReturn(to);
        replay(dateService);

		expect(assetService.getAssetsStatus(from, to, owner)).andReturn(assetStatuses);
		replay(assetService);

        ChartSeries<String> results = dashboardService.getAssetsStatus(dateRange, owner);
		
		assertEquals(sizeOfBigData, results.size());  // add one for the Other section (which aggregrates the 3 smaller valued records).

		verify(dateService, assetService);
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void test_getAssetsStatus_null_range() {
        ChartSeries<String> results = dashboardService.getAssetsStatus(null, owner);
	}	

	@Test
	public void test_getAssetsIdentified() {
        setCurrentMillisFixed(jan1.toDate().getTime());

        BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		ChartGranularity granularity = ChartGranularity.MONTH;
		List<AssetsIdentifiedReportRecord> assets = createAssetsIdentifiedResults(granularity, jan1_2011, 100L, 200L);
        DateRange dateRange = new DateRange(RangeType.THIS_YEAR, TimeZone.getDefault());
		expect(assetService.getAssetsIdentified(granularity, granularity.roundDown(dateRange.getFrom()).toDate(), granularity.roundUp(dateRange.getTo()).toDate(), owner)).andReturn(assets);
		replay(assetService);

        ChartSeries<LocalDate> results = dashboardService.getAssetsIdentified(dateRange, granularity, owner);
		
		assertEquals(2, results.size());
        assertEquals(100L, results.get(jan1_2011).getY().longValue());
        assertEquals(200L, results.get(jan1_2011.plusMonths(1)).getY().longValue());

		verify(assetService);
	}

	@Test(expected=IllegalArgumentException.class)
	public void test_getAssetsIdentified_null_date() { 
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		ChartGranularity granularity = ChartGranularity.DAY;
        ChartSeries<LocalDate> results = dashboardService.getAssetsIdentified(null, granularity, owner);
	}	

	@Test
	public void test_getCompletedEvents() { 
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		ChartGranularity granularity = ChartGranularity.DAY;
        DateRange dateRange = new DateRange(RangeType.THIS_YEAR, TimeZone.getDefault());
		
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
		
		expect(eventService.getCompletedEvents(dateRange.getFrom().toDate(), dateRange.getTo().toDate(), owner, (Status)null, granularity)).andReturn(allEvents);
		expect(eventService.getCompletedEvents(dateRange.getFrom().toDate(), dateRange.getTo().toDate(), owner, Status.FAIL, granularity)).andReturn(failedEvents);
		expect(eventService.getCompletedEvents(dateRange.getFrom().toDate(), dateRange.getTo().toDate(), owner, Status.NA, granularity)).andReturn(naEvents);
		expect(eventService.getCompletedEvents(dateRange.getFrom().toDate(), dateRange.getTo().toDate(), owner, Status.PASS, granularity)).andReturn(passedEvents);
		replay(eventService);
		replay(assetService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getCompletedEvents(dateRange, granularity, owner);
		
		assertEquals("expect ChartSeries for All, Pass, NA, Fail", 4, results.size());
		assertEquals(Status.ALL.getLabel(), results.get(0).getLabel());
		assertEquals(Status.PASS.getDisplayName(), results.get(1).getLabel());
		assertEquals(Status.FAIL.getDisplayName(), results.get(2).getLabel());
		assertEquals(Status.NA.getDisplayName(), results.get(3).getLabel());

		verify(eventService,assetService);
	}
		
	@Test 
	public void test_EventCompleteness() {
		setCurrentMillisFixed(jan1_2011.toDate().getTime());
		ChartGranularity granularity = ChartGranularity.WEEK;
        DateRange dateRange = new DateRange(RangeType.LAST_MONTH);
		BaseOrg org = OrgBuilder.aDivisionOrg().build();
		
		List<EventCompletenessReportRecord> completedEvents = createEventCompletenessResults(granularity, 21L);
		List<EventCompletenessReportRecord> allEvents = Lists.newArrayList();
		allEvents.addAll(completedEvents);
		allEvents.addAll(createEventCompletenessResults(granularity, 888L, 574L, 924L));
		expect(eventService.getEventCompleteness(granularity, granularity.roundDown(dateRange.getFrom()).toDate(), granularity.roundUp(jan1_2011).toDate(), org)).andReturn(allEvents);
		expect(eventService.getEventCompleteness(Event.EventState.OPEN, granularity, granularity.roundDown(dateRange.getFrom()).toDate(), granularity.roundUp(jan1_2011).toDate(), org)).andReturn(completedEvents);
		replay(eventService);
		replay(assetService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getEventCompletenessEvents(granularity, dateRange, org);
		
		assertEquals(2, results.size());
		assertEquals(Event.EventState.COMPLETED.getLabel(), results.get(0).getLabel());
		assertEquals(EventSchedule.ALL_STATUS.getLabel(), results.get(1).getLabel());

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

	private List<AssetsIdentifiedReportRecord> createAssetsIdentifiedResults(ChartGranularity granularity, LocalDate d, Long... values) {
        ArrayList<AssetsIdentifiedReportRecord> results = Lists.newArrayList();
        LocalDate date = new LocalDate(d);
        for (Long value:values) {         
		    AssetsIdentifiedReportRecord record = new AssetsIdentifiedReportRecord( value, granularity.toString(), date.getYear(), date.getMonthOfYear(), date.getDayOfMonth() );
            results.add(record);
            date = date.plusMonths(1);
        }
        return results;
	}
	
	private List<UpcomingScheduledEventsRecord> createUpcomingEventResults() {
		UpcomingScheduledEventsRecord event1 = new UpcomingScheduledEventsRecord(jan1, VALUE_FOR_JAN1);
		UpcomingScheduledEventsRecord event2 = new UpcomingScheduledEventsRecord(jan5, VALUE_FOR_JAN5);
		return Lists.newArrayList(event1, event2);
	}

	private List<AssetsStatusReportRecord> createAssetStatusResults(String ... statuses) {
        long value = 234L;
        ArrayList<AssetsStatusReportRecord> results = Lists.newArrayList();    
        for (String status:statuses) { 
		    AssetsStatusReportRecord record = new AssetsStatusReportRecord(status, value);
            results.add(record);
            value+=100L;
        }
		return results;
	}
	
}
