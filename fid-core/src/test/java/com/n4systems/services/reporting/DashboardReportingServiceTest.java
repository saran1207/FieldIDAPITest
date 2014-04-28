package com.n4systems.services.reporting;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.model.*;
import com.n4systems.model.builders.*;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import com.n4systems.model.dashboard.widget.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.*;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.date.DateService;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.RangeType;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;


public class DashboardReportingServiceTest extends FieldIdServiceTest {

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
    @TestMock private PersistenceService persistenceService;
    @TestMock private AssetStatusService assetStatusService;
    @TestMock private EventTypeGroupService eventTypeGroupService;
    @TestMock private PriorityCodeService priorityCodeService;

	private LocalDate jan1 = new LocalDate(2011, 1, 1);
	private LocalDate jan2 = new LocalDate(2011, 1, 2);
	private LocalDate jan5 = new LocalDate(2011, 1, 5);
	
	private BaseOrg owner;
    private AssetSearchCriteria assetSearchCriteria;
    private EventReportCriteria reportCriteria;


    @Override
    protected Object createSut(Field sutField) throws Exception {
        return new DashboardReportingService() {
            @Override public AssetSearchCriteria getDefaultAssetSearchCritieria() {
                return assetSearchCriteria;
            }
            @Override public EventReportCriteria getDefaultReportCriteria() {
                return reportCriteria;
            }
            @Override EventReportCriteria getDefaultReportCriteria(BaseOrg org) {
                EventReportCriteria reportCriteria = getDefaultReportCriteria();
                reportCriteria.setOwner(org);
                return reportCriteria;
            }
        };
    }

    @Override
	@Before 
	public void setUp() {
		super.setUp();
		owner = OrgBuilder.aCustomerOrg().build();
	}

	@Test
	public void test_getUpcomingScheduledEvents() {
        setCurrentMillisFixed(jan1);

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
        setCurrentMillisFixed(jan1);

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
		List<CompletedEventsReportRecord> failedEvents = createCompletedEventsResults(EventResult.FAIL, failedCount);
		List<CompletedEventsReportRecord> passedEvents = createCompletedEventsResults(EventResult.PASS, passCount);
		List<CompletedEventsReportRecord> naEvents = createCompletedEventsResults(EventResult.NA, naCount);
		List<CompletedEventsReportRecord> allEvents = new ArrayList<CompletedEventsReportRecord>();
		allEvents.addAll(failedEvents);
		allEvents.addAll(naEvents);
		allEvents.addAll(passedEvents);
		
		expect(eventService.getCompletedEvents(dateRange.getFrom().toDate(), dateRange.getTo().toDate(), owner, (EventResult)null, granularity)).andReturn(allEvents);
		expect(eventService.getCompletedEvents(dateRange.getFrom().toDate(), dateRange.getTo().toDate(), owner, EventResult.FAIL, granularity)).andReturn(failedEvents);
		expect(eventService.getCompletedEvents(dateRange.getFrom().toDate(), dateRange.getTo().toDate(), owner, EventResult.NA, granularity)).andReturn(naEvents);
		expect(eventService.getCompletedEvents(dateRange.getFrom().toDate(), dateRange.getTo().toDate(), owner, EventResult.PASS, granularity)).andReturn(passedEvents);
		replay(eventService);
		replay(assetService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getCompletedEvents(dateRange, granularity, owner);
		
		assertEquals("expect ChartSeries for All, Pass, NA, Fail", 4, results.size());
		assertEquals(EventResult.ALL.getLabel(), results.get(0).getLabel());
		assertEquals(EventResult.PASS.getDisplayName(), results.get(1).getLabel());
		assertEquals(EventResult.FAIL.getDisplayName(), results.get(2).getLabel());
		assertEquals(EventResult.NA.getDisplayName(), results.get(3).getLabel());

		verify(eventService,assetService);
	}

    @Test
    public void test_kpi_forever() {
        DateRange dateRange = new DateRange(RangeType.FOREVER);
        BaseOrg org = OrgBuilder.aDivisionOrg().build();

        EventKpiRecord kpi = new EventKpiRecord();
        expect(eventService.getEventKpi(null,null,org)).andReturn(kpi);
        replay(eventService);

        EventKpiRecord result = dashboardService.getEventKpi(org, dateRange);

        verifyTestMocks();
    }

    @Test
    public void test_kpi_thisyear() {
        setCurrentMillisFixed(jan1_2011);
        DateRange dateRange = new DateRange(RangeType.THIS_YEAR);
        BaseOrg org = OrgBuilder.aDivisionOrg().build();

        EventKpiRecord kpi = new EventKpiRecord();
        expect(eventService.getEventKpi(jan1_2011.toDate(),jan1_2011.plusYears(1).toDate(),org)).andReturn(kpi);
        replay(eventService);

        EventKpiRecord result = dashboardService.getEventKpi(org, dateRange);

        verifyTestMocks();
    }

    @Test
	public void test_EventCompleteness() {
        setCurrentMillisFixed(jan1_2011);
		ChartGranularity granularity = ChartGranularity.WEEK;
        DateRange dateRange = new DateRange(RangeType.LAST_MONTH);
		BaseOrg org = OrgBuilder.aDivisionOrg().build();
		
		List<EventCompletenessReportRecord> completedEvents = createEventCompletenessResults(granularity, 21L);
		List<EventCompletenessReportRecord> allEvents = Lists.newArrayList();
		allEvents.addAll(completedEvents);
		allEvents.addAll(createEventCompletenessResults(granularity, 888L, 574L, 924L));
		expect(eventService.getEventCompleteness(granularity, granularity.roundDown(dateRange.getFrom()).toDate(), granularity.roundUp(jan1_2011).toDate(), org)).andReturn(allEvents);
		replay(eventService);
		replay(assetService);
		
		List<ChartSeries<LocalDate>> results = dashboardService.getEventCompletenessEvents(granularity, dateRange, org);
		
		assertEquals(3, results.size());
		assertEquals(com.n4systems.model.WorkflowState.CLOSED.getLabel(), results.get(0).getLabel());
        assertEquals(com.n4systems.model.WorkflowState.COMPLETED.getLabel(), results.get(1).getLabel());
        assertEquals(com.n4systems.model.WorkflowState.OPEN.getLabel(), results.get(2).getLabel());

		verifyTestMocks();		
	}
	
	private List<EventCompletenessReportRecord> createEventCompletenessResults(ChartGranularity granularity, Long... values) {
		List<EventCompletenessReportRecord> results = Lists.newArrayList();
		for (Long value:values) { 
			EventCompletenessReportRecord record = new EventCompletenessReportRecord(value, com.n4systems.model.WorkflowState.COMPLETED, granularity.toString(),2011,1,1);
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

    @Test
    public void test_convertWidgetDefinitionToAssetIdentifiedCriteria() {
        Long xAsDateInMs = jan1_2011.toDate().getTime();
        String y = "helloY";
        String series = "series";
        Long id = -1L;
        assetSearchCriteria = new AssetSearchCriteria();

        AssetsIdentifiedWidgetConfiguration config = new AssetsIdentifiedWidgetConfiguration();
        config.setGranularity(ChartGranularity.MONTH);

        WidgetDefinition widgetDefinition = new WidgetDefinition();
        widgetDefinition.setWidgetType(WidgetType.ASSETS_IDENTIFIED);
        widgetDefinition.setConfig(config);

        expect(persistenceService.findNonSecure(WidgetDefinition.class, id)).andReturn(widgetDefinition);
        replay(persistenceService);

        AssetSearchCriteria result = dashboardService.convertWidgetDefinitionToAssetCriteria(id, xAsDateInMs, y, series);

        LocalDate date = new LocalDate(xAsDateInMs);
        assertEquals(jan1_2011, result.getDateRange().getFrom() );
        assertEquals(jan31_2011, result.getDateRange().getTo() );

        verifyTestMocks();
    }

    @Test
    public void test_convertWidgetDefinitionToAssetStatusCriteria() {
        Long xAsDateInMs = jan1_2011.toDate().getTime();
        String y = "helloY";
        String statusString = "SomeStatus";
        Long id = -1L;
        assetSearchCriteria = new AssetSearchCriteria();
        BaseOrg org = OrgBuilder.aPrimaryOrg().build();
        AssetStatus status = AssetStatusBuilder.anAssetStatus().named(statusString).build();

        AssetsStatusWidgetConfiguration config = new AssetsStatusWidgetConfiguration();
        config.setOrg(org);
        config.setRangeType(RangeType.THIS_MONTH);

        WidgetDefinition widgetDefinition = new WidgetDefinition();
        widgetDefinition.setWidgetType(WidgetType.ASSETS_STATUS);
        widgetDefinition.setConfig(config);

        expect(persistenceService.findNonSecure(WidgetDefinition.class, id)).andReturn(widgetDefinition);
        replay(persistenceService);

        expect(assetStatusService.getStatusByName(statusString)).andReturn(status);
        replay(assetStatusService);

        AssetSearchCriteria result = dashboardService.convertWidgetDefinitionToAssetCriteria(id, xAsDateInMs, statusString, "unusedParameter");

        LocalDate date = new LocalDate(xAsDateInMs);
        assertEquals(new DateRange(RangeType.THIS_MONTH), result.getDateRange() );
        assertEquals(org, result.getOwner() );
        assertEquals(status, result.getAssetStatus());

        verifyTestMocks();
    }

    @Test
    public void test_convertWidgetDefinitionToReportCompletedCriteria() {
        Long xAsDateInMs = jan1_2011.toDate().getTime();
        String statusString = "pass";
        Long id = -1L;
        reportCriteria = new EventReportCriteria();

        BaseOrg org = OrgBuilder.aPrimaryOrg().build();
        AssetStatus status = AssetStatusBuilder.anAssetStatus().named(statusString).build();

        CompletedEventsWidgetConfiguration config = new CompletedEventsWidgetConfiguration();
        config.setOrg(org);
        config.setGranularity(ChartGranularity.MONTH);

        WidgetDefinition widgetDefinition = new WidgetDefinition();
        widgetDefinition.setWidgetType(WidgetType.COMPLETED_EVENTS);
        widgetDefinition.setConfig(config);

        expect(persistenceService.findNonSecure(WidgetDefinition.class, id)).andReturn(widgetDefinition);
        replay(persistenceService);

        EventReportCriteria result = (EventReportCriteria) dashboardService.convertWidgetDefinitionToReportCriteria(id, xAsDateInMs, "unusedParameter", statusString );

        assertEquals(EventResult.PASS,result.getEventResult());
        assertEquals(jan1_2011, result.getDateRange().getFrom());
        assertEquals(jan31_2011, result.getDateRange().getTo());

        verifyTestMocks();
    }

    @Test
    public void test_convertWidgetDefinitionToReportScheduledCriteria() {
        Long xAsDateInMs = jan1_2011.toDate().getTime();
        String statusString = "pass";
        Long id = -1L;
        reportCriteria = new EventReportCriteria();

        BaseOrg org = OrgBuilder.aPrimaryOrg().build();

        UpcomingEventsWidgetConfiguration config = new UpcomingEventsWidgetConfiguration();
        config.setOrg(org);
        config.setPeriod(30);
        config.setRangeType(RangeType.THIS_MONTH);

        WidgetDefinition widgetDefinition = new WidgetDefinition();
        widgetDefinition.setWidgetType(WidgetType.UPCOMING_SCHEDULED_EVENTS);
        widgetDefinition.setConfig(config);

        expect(persistenceService.findNonSecure(WidgetDefinition.class, id)).andReturn(widgetDefinition);
        replay(persistenceService);

        EventReportCriteria result = (EventReportCriteria) dashboardService.convertWidgetDefinitionToReportCriteria(id, xAsDateInMs, "unusedParameter", statusString );

        assertEquals(jan1_2011, result.getDueDateRange().getFrom());
        assertEquals(jan1_2011, result.getDueDateRange().getTo());
        assertEquals(WorkflowStateCriteria.OPEN, result.getWorkflowState());

        verifyTestMocks();
    }

    @Test
    public void test_convertWidgetDefinitionToReportKpiCriteria() {
        Long xIndex = 1L;
        Long id = -1L;
        reportCriteria = new EventReportCriteria();

        BaseOrg org1 = OrgBuilder.aPrimaryOrg().build();
        BaseOrg org2 = OrgBuilder.aSecondaryOrg().build();

        EventKPIWidgetConfiguration config = new EventKPIWidgetConfiguration();
        config.setOrgs(Lists.newArrayList(org1, org2));
        config.setRangeType(RangeType.THIS_MONTH);

        WidgetDefinition widgetDefinition = new WidgetDefinition();
        widgetDefinition.setWidgetType(WidgetType.EVENT_KPI);
        widgetDefinition.setConfig(config);

        expect(persistenceService.findNonSecure(WidgetDefinition.class, id)).andReturn(widgetDefinition).anyTimes();
        replay(persistenceService);

        EventReportCriteria result = (EventReportCriteria) dashboardService.convertWidgetDefinitionToReportCriteria(id, xIndex, "unusedParameter", KpiType.NA.getLabel() );

        assertEquals(org2, result.getOwner());
        assertEquals(IncludeDueDateRange.SELECT_DUE_DATE_RANGE, result.getIncludeDueDateRange());
        assertEquals(jan1_2011, result.getDueDateRange().getFrom());
        assertEquals(feb1_2011, result.getDueDateRange().getTo());
        assertEquals(new DateRange(RangeType.FOREVER), result.getDateRange());
        assertEquals(EventResult.NA, result.getEventResult());

        result = (EventReportCriteria) dashboardService.convertWidgetDefinitionToReportCriteria(id, 0L, "unusedParameter", KpiType.CLOSED.getLabel() );
        assertEquals(org1, result.getOwner());
        assertEquals(IncludeDueDateRange.SELECT_DUE_DATE_RANGE, result.getIncludeDueDateRange());
        assertEquals(jan1_2011, result.getDueDateRange().getFrom());
        assertEquals(feb1_2011, result.getDueDateRange().getTo());
        assertEquals(new DateRange(RangeType.FOREVER), result.getDateRange());
        assertEquals(WorkflowStateCriteria.CLOSED, result.getWorkflowState());


        result = (EventReportCriteria) dashboardService.convertWidgetDefinitionToReportCriteria(id, 0L, "unusedParameter", KpiType.FAILED.getLabel() );
        assertEquals(org1, result.getOwner());
        assertEquals(IncludeDueDateRange.SELECT_DUE_DATE_RANGE, result.getIncludeDueDateRange());
        assertEquals(jan1_2011, result.getDueDateRange().getFrom());
        assertEquals(feb1_2011, result.getDueDateRange().getTo());
        assertEquals(new DateRange(RangeType.FOREVER), result.getDateRange());
        assertEquals(EventResult.FAIL, result.getEventResult());


        result = (EventReportCriteria) dashboardService.convertWidgetDefinitionToReportCriteria(id, 0L, "unusedParameter", KpiType.INCOMPLETE.getLabel() );
        assertEquals(org1, result.getOwner());
        assertEquals(IncludeDueDateRange.SELECT_DUE_DATE_RANGE, result.getIncludeDueDateRange());
        assertEquals(jan1_2011, result.getDueDateRange().getFrom());
        assertEquals(feb1_2011, result.getDueDateRange().getTo());
        assertEquals(new DateRange(RangeType.FOREVER), result.getDateRange());
        assertEquals(WorkflowStateCriteria.OPEN, result.getWorkflowState());


        result = (EventReportCriteria) dashboardService.convertWidgetDefinitionToReportCriteria(id, 0L, "unusedParameter", KpiType.PASSED.getLabel() );
        assertEquals(org1, result.getOwner());
        assertEquals(IncludeDueDateRange.SELECT_DUE_DATE_RANGE, result.getIncludeDueDateRange());
        assertEquals(jan1_2011, result.getDueDateRange().getFrom());
        assertEquals(feb1_2011, result.getDueDateRange().getTo());
        assertEquals(new DateRange(RangeType.FOREVER), result.getDateRange());
        assertEquals(EventResult.PASS, result.getEventResult());

        verifyTestMocks();
    }

    @Test
    public void test_convertWidgetDefinitionToReportWorkCriteria() {
        Long xAsDateInMs = jan1_2011.toDate().getTime();
        String statusString = "pass";
        Long id = -1L;
        reportCriteria = new EventReportCriteria();

        BaseOrg org = OrgBuilder.aPrimaryOrg().build();
        AssetType assetType = AssetTypeBuilder.anAssetType().named("assetType").build();
        ThingEventType eventType = EventTypeBuilder.anEventType().named("eventType").build();
        User user = UserBuilder.aFullUser().withId(33L).build();

        WorkWidgetConfiguration config = new WorkWidgetConfiguration();
        config.setOrg(org);
        config.setAssetType(assetType);
        config.setEventType(eventType);
        config.setUser(user);

        WidgetDefinition widgetDefinition = new WidgetDefinition();
        widgetDefinition.setWidgetType(WidgetType.WORK);
        widgetDefinition.setConfig(config);

        expect(persistenceService.findNonSecure(WidgetDefinition.class, id)).andReturn(widgetDefinition);
        replay(persistenceService);

        EventReportCriteria result = (EventReportCriteria) dashboardService.convertWidgetDefinitionToReportCriteria(id, xAsDateInMs, "unusedParameter", statusString);

        assertEquals(assetType, result.getAssetType());
        assertEquals(eventType, result.getEventType());
        assertEquals(WorkflowStateCriteria.OPEN, result.getWorkflowState());
        assertEquals(user, result.getAssignee());

        verifyTestMocks();
    }

    @Test
    public void test_convertWidgetDefinitionToReportActionsCriteria() {
        Long xAsDateInMs = jan1_2011.toDate().getTime();
        String priorityString = "priority";
        Long id = -1L;
        reportCriteria = new EventReportCriteria();

        BaseOrg org = OrgBuilder.aPrimaryOrg().build();
        EventTypeGroup eventTypeGroup = EventTypeGroupBuilder.anEventTypeGroup().withName("eventTypeGroup").build();
        ActionEventType eventType = ActionEventTypeBuilder.anEventType().named("eventType").withGroup(eventTypeGroup).build();
        User user = UserBuilder.aFullUser().withId(33L).build();
        PriorityCode priority = new PriorityCode();
        priority.setName("priority");

        ActionsWidgetConfiguration config = new ActionsWidgetConfiguration();
        config.setOrg(org);
        config.setActionType(eventType);
        config.setUser(user);
        config.setRangeType(RangeType.THIS_MONTH);

        WidgetDefinition widgetDefinition = new WidgetDefinition();
        widgetDefinition.setWidgetType(WidgetType.ACTIONS);
        widgetDefinition.setConfig(config);

        expect(persistenceService.findNonSecure(WidgetDefinition.class, id)).andReturn(widgetDefinition);
        replay(persistenceService);

        expect(priorityCodeService.getPriorityCodeByName(priorityString)).andReturn(priority);
        replay(priorityCodeService);

        EventReportCriteria result = (EventReportCriteria) dashboardService.convertWidgetDefinitionToReportCriteria(id, xAsDateInMs, priorityString, "OVERDUE");

        assertEquals(jan1_2011, result.getDueDateRange().getFrom());
        assertEquals(feb1_2011, result.getDueDateRange().getTo());
        assertEquals(priority, result.getPriority());
        assertEquals(WorkflowStateCriteria.OPEN, result.getWorkflowState());
        assertEquals(user, result.getAssignee());
        assertEquals(EventSearchType.ACTIONS, result.getEventSearchType());

        verifyTestMocks();
    }

    private List<CompletedEventsReportRecord> createCompletedEventsResults(EventResult fail, int count) {
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
