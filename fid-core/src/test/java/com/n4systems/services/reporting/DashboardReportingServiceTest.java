package com.n4systems.services.reporting;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.Status;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.time.DateUtil;


public class DashboardReportingServiceTest {

	private static final String STATUS_FOO = "Foo";
	private static final String STATUS_BAR = "bar";
	private static final String STATUS_HELLO = "hello";
	private static final String STATUS_WORLD = "world";
	
	private DashboardReportingService fixture; 
	private AssetService assetService;
	private EventService eventService;

	
	@Before 
	public void setUp() { 
		fixture = new DashboardReportingService();
		assetService = createMock(AssetService.class);
		eventService = createMock(EventService.class);
		fixture.setAssetService(assetService);
		fixture.setEventService(eventService);
	}
	
	@Test
	public void test_getUpcomingScheduledEvents() { 
		Integer period = 30;
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		List<UpcomingScheduledEventsRecord> events = createUpcomingEventResults();
		expect(eventService.getUpcomingScheduledEvents(period, owner)).andReturn(events);
		replay(eventService);
		
		List<ChartSeries<Calendar>> results = fixture.getUpcomingScheduledEvents(period, owner);
		
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
		
		List<ChartSeries<String>> results = fixture.getAssetsStatus(dateRange, owner);
		
		assertEquals(1, results.size());
		assertEquals(assetStatuses.size()+1, results.get(0).size());  // note that it currently adds "Other" section to results so expect one more.		
	}	
	
	@Test
	public void test_getAssetsIdentified() { 
		BaseOrg owner = OrgBuilder.aCustomerOrg().build();
		List<AssetsIdentifiedReportRecord> assets = createAssetsIdentifiedResults();
		ChartGranularity granularity = ChartGranularity.DAY;
		ChartDateRange dateRange = ChartDateRange.THIS_YEAR;
		expect(assetService.getAssetsIdentified(granularity, dateRange.getFromDate(), dateRange.getToDate(), owner)).andReturn(assets);
		replay(assetService);
		
		List<ChartSeries<Calendar>> results = fixture.getAssetsIdentified(dateRange, granularity, owner);
		
		assertEquals("only one ChartSeries in result set", 1, results.size());		
		assertEquals("expecting a year (365) of points in the ChartSeries", 365, results.get(0).size());		
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
		
		List<ChartSeries<Calendar>> results = fixture.getCompletedEvents(dateRange, granularity, owner);
		
		assertEquals("expect ChartSeries for All, Pass, NA, Fail", 4, results.size());
	}
	
	private List<CompletedEventsReportRecord> createCompletedEventsResults(Status fail, int count) {
		List<CompletedEventsReportRecord> results = Lists.newArrayList();
		for (int i=0; i<count; i++) {
			results.add(new CompletedEventsReportRecord(34L, fail, 2011, 1, 1, 1, 1+count));
		}
		return results;
	}

	private List<AssetsIdentifiedReportRecord> createAssetsIdentifiedResults() {
		AssetsIdentifiedReportRecord record = new AssetsIdentifiedReportRecord(45L, 2011, 1, 1, 2, 1);
		AssetsIdentifiedReportRecord record2 = new AssetsIdentifiedReportRecord(8547L, 2011, 1, 1, 2, 1);		
		return Lists.newArrayList(record, record2);
	}
	
	private List<UpcomingScheduledEventsRecord> createUpcomingEventResults() {
		Date jan1 = DateUtil.getDay(2011, 1);
		Date jan5 = DateUtil.getDay(2011, 5); 
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
