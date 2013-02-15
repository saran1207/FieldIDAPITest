package com.n4systems.services.reporting;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.model.Event;
import com.n4systems.model.EventResult;
import com.n4systems.model.EventType;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.*;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.date.DateService;
import com.n4systems.util.EnumUtils;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.EventCompletenessChartSeries;
import com.n4systems.util.chart.RangeType;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DashboardReportingService extends FieldIdPersistenceService {

    private @Autowired AssetService assetService;
	private @Autowired EventService eventService;
	private @Autowired AssetStatusService assetStatusService;
    private @Autowired DateService dateService;
    private @Autowired PriorityCodeService priorityCodeService;
    private @Autowired EventTypeGroupService eventTypeGroupService;

    @Transactional(readOnly = true)
    public ChartSeries<LocalDate> getAssetsIdentified(DateRange dateRange, ChartGranularity granularity, BaseOrg owner) {
		Preconditions.checkArgument(dateRange !=null);
		List<AssetsIdentifiedReportRecord> results = assetService.getAssetsIdentified(granularity, getFrom(granularity, dateRange), getTo(granularity, dateRange), owner);
        return new ChartSeries<LocalDate>(results);
    }

    @Transactional(readOnly = true)
    public ChartSeries<LocalDate> getUpcomingScheduledEvents(Integer period, BaseOrg owner) {
		Preconditions.checkArgument(period!=null);
		List<UpcomingScheduledEventsRecord> results = eventService.getUpcomingScheduledEvents(period, owner);
		return new ChartSeries<LocalDate>(results);
	}

    @Transactional(readOnly = true)
    public List<ChartSeries<String>> getActions(DateRange dateRange, BaseOrg owner, User assignee, EventType actionType) {
        Preconditions.checkArgument(dateRange!=null);
        List<ChartSeries<String>> results = Lists.newArrayList();
        results.add(eventService.getOverdueActions(dateService.calculateFromDate(dateRange), dateService.calculateToDate(dateRange), owner, assignee, actionType));
        results.add(eventService.getUpcomingActions(dateService.calculateFromDate(dateRange), dateService.calculateToDate(dateRange), owner, assignee, actionType));
        return results;
    }

	public ChartSeries<String> getAssetsStatus(DateRange dateRange, BaseOrg org) {
		Preconditions.checkArgument(dateRange !=null);
        List<AssetsStatusReportRecord> results = assetService.getAssetsStatus(dateService.calculateFromDate(dateRange), dateService.calculateToDate(dateRange), org);
        return new ChartSeries<String>(results);
	}

	public List<ChartSeries<LocalDate>> getCompletedEvents(DateRange dateRange, ChartGranularity granularity, BaseOrg org) {
		Preconditions.checkArgument(dateRange !=null);
		List<ChartSeries<LocalDate>> results = new ArrayList<ChartSeries<LocalDate>>();

		Date from = getFrom(granularity, dateRange);
		Date to = getTo(granularity, dateRange);
		List<CompletedEventsReportRecord> completedEvents = eventService.getCompletedEvents(from, to, org, null, granularity);
        results.add(new ChartSeries<LocalDate>(EventResult.ALL, EventResult.ALL.getLabel(), completedEvents));

		for (EventResult eventResult : EventResult.getValidEventResults()) {
			completedEvents = eventService.getCompletedEvents(from, to, org, eventResult, granularity);
			results.add(new ChartSeries<LocalDate>(eventResult, eventResult.getDisplayName(), completedEvents));
		}

		return results;
	}

	public EventKpiRecord getEventKpi(BaseOrg owner, DateRange dateRange) {
		Preconditions.checkArgument(dateRange !=null);
        LocalDate from = dateRange.getFrom();
        LocalDate to = dateRange.getTo();
        return eventService.getEventKpi(from==null?null:from.toDate(), to==null?null:to.toDate(), owner);
    }

	public List<ChartSeries<LocalDate>> getEventCompletenessEvents(ChartGranularity granularity, DateRange dateRange, BaseOrg org) {
		Preconditions.checkArgument(dateRange !=null);

		List<EventCompletenessReportRecord> events = eventService.getEventCompleteness(granularity, getFrom(granularity, dateRange), getTo(granularity, dateRange), org);

		List<ChartSeries<LocalDate>> results = new ArrayList<ChartSeries<LocalDate>>();
        results.add(new EventCompletenessChartSeries(Event.WorkflowState.CLOSED, events));
        results.add(new EventCompletenessChartSeries(Event.WorkflowState.COMPLETED, events));
        results.add(new EventCompletenessChartSeries(Event.WorkflowState.OPEN, events));

        return results;
	}

    private Date getTo(ChartGranularity granularity, DateRange dateRange) {
		LocalDate to = dateRange.getTo();

		return (to==null) ? null : granularity.roundUp(to).toDate();
	}

	private Date getFrom(ChartGranularity granularity, DateRange dateRange) {
		LocalDate from = dateRange.getFrom();
		return (from==null) ? null : granularity.roundDown(from).toDate();
	}


	// ------------------------------------------------------------------------------------------------
	//
	// NOTE : methods used by widgets to convert configuration into criteria.
	//
	
	public EventReportCriteria convertWidgetDefinitionToReportCriteria(Long widgetDefinitionId, Long x, String y, String series) {
		WidgetDefinition<?> widgetDefinition = getWidgetDefinition(widgetDefinitionId);
		switch (widgetDefinition.getWidgetType()) {
			case EVENT_COMPLETENESS:
				return getCriteriaDefaults(((EventCompletenessWidgetConfiguration) widgetDefinition.getConfig()), series, new LocalDate(x));
			case COMPLETED_EVENTS:
				return getCriteriaDefaults(((CompletedEventsWidgetConfiguration) widgetDefinition.getConfig()), series, new LocalDate(x));
			case UPCOMING_SCHEDULED_EVENTS: 
				return getCriteriaDefaults(((UpcomingEventsWidgetConfiguration) widgetDefinition.getConfig()), series, new LocalDate(x));
            case EVENT_KPI:
                return getCriteriaDefaults((EventKPIWidgetConfiguration) widgetDefinition.getConfig(), series, x.intValue()/*assumed to be org index*/);
            case WORK:
                return getCriteriaDefaults((WorkWidgetConfiguration)widgetDefinition.getConfig(), series, x.intValue());
            case ACTIONS:
                return getCriteriaDefaults((ActionsWidgetConfiguration)widgetDefinition.getConfig(), series, y, x.intValue());
			default:
				throw new IllegalArgumentException("Can't convert widget of type " + widgetDefinition.getWidgetType() + " into report criteria");
		}
	}

    private EventReportCriteria getCriteriaDefaults() {
    	EventReportCriteria criteria = getDefaultReportCriteria();
    	criteria.setWorkflowState(WorkflowState.OPEN);
		return criteria;
	}

	private WidgetDefinition<?> getWidgetDefinition(Long widgetDefinitionId) {
		return persistenceService.findNonSecure(WidgetDefinition.class, widgetDefinitionId);
	}

	private EventReportCriteria getCriteriaDefaults(UpcomingEventsWidgetConfiguration config, String series, LocalDate localDate) {
        EventReportCriteria criteria = getDefaultReportCriteria(config.getOrg());
        // this widget displays day by day.  .: when you click on a pt the date range is always a single day.
        criteria.setDueDateRange(new DateRange(localDate, localDate));
        criteria.setWorkflowState(WorkflowState.OPEN);
        return criteria;
	}

    private EventReportCriteria getCriteriaDefaults(ActionsWidgetConfiguration config, String series, String priorityName, int i) {
        EventService.ActionBar bar = EventService.ActionBar.valueOf(series);
        Preconditions.checkArgument(bar != null, "must pass in one of " + EventService.ActionBar.values());
        EventReportCriteria criteria = getDefaultReportCriteria(config.getOrg());
        if (config.getUser()!=null) {
            criteria.setAssignee(config.getUser());
        }

        criteria.setDueDateRange(config.getDateRange());
        criteria.setPriority(priorityCodeService.getPriorityCodeByName(priorityName));
        criteria.setWorkflowState(WorkflowState.OPEN);
        criteria.setEventType(config.getActionType());
        criteria.setEventTypeGroup(eventTypeGroupService.getDefaultActionGroup());
        return criteria;
    }

    private EventReportCriteria getCriteriaDefaults(EventKPIWidgetConfiguration config, String series, int orgIndex) {
        Preconditions.checkArgument(orgIndex >= 0 && orgIndex <=config.getOrgs().size());
        EventReportCriteria criteria = getDefaultReportCriteria(config.getOrgs().get(orgIndex));
        DateRange dateRange = config.getDateRange();
        criteria.setIncludeDueDateRange(IncludeDueDateRange.SELECT_DUE_DATE_RANGE);
        if (dateRange!=null && RangeType.FOREVER.equals(dateRange.getRangeType())) {
            criteria.setIncludeDueDateRange(IncludeDueDateRange.HAS_A_DUE_DATE);
        }
        criteria.setDueDateRange(dateRange);
        criteria.setDateRange(new DateRange(RangeType.FOREVER));

        if (KpiType.INCOMPLETE.getLabel().equals(series)) {
            criteria.setWorkflowState(WorkflowState.OPEN);
        } else if (KpiType.FAILED.getLabel().equals(series)) {
            criteria.setEventResult(EventResult.FAIL);
        } else if (KpiType.NA.getLabel().equals(series)) {
            criteria.setEventResult(EventResult.NA);
        } else if (KpiType.PASSED.getLabel().equals(series)) {
            criteria.setEventResult(EventResult.PASS);
        } else if (KpiType.CLOSED.getLabel().equals(series)) {
            criteria.setWorkflowState(WorkflowState.CLOSED);
        }
        return criteria;
    }

    private EventReportCriteria getCriteriaDefaults(CompletedEventsWidgetConfiguration config, String series, LocalDate localDate) {
        EventReportCriteria criteria = getDefaultReportCriteria(config.getOrg());
        criteria.setEventResult(EnumUtils.valueOf(EventResult.class, series));
        setDateRange(criteria, config.getGranularity(), localDate);
        return criteria;
    }

	private EventReportCriteria getCriteriaDefaults(EventCompletenessWidgetConfiguration config, String series, LocalDate localDate) {
		EventReportCriteria criteria = getDefaultReportCriteria(config.getOrg());
        Event.WorkflowState state = EnumUtils.valueOf(Event.WorkflowState.class, series);
        switch (state) {
            case OPEN:
                criteria.setIncludeDueDateRange(IncludeDueDateRange.SELECT_DUE_DATE_RANGE);
                criteria.setDueDateRange(new DateRange(localDate, localDate.plus(config.getGranularity().getPeriod()).minusDays(1)));
                criteria.setWorkflowState(WorkflowState.OPEN);
                break;
            case COMPLETED:
                criteria.setIncludeDueDateRange(IncludeDueDateRange.SELECT_DUE_DATE_RANGE);
                criteria.setDueDateRange(new DateRange(localDate, localDate.plus(config.getGranularity().getPeriod()).minusDays(1)));
                criteria.setWorkflowState(WorkflowState.COMPLETE);
                break;
            case CLOSED:
                criteria.setIncludeDueDateRange(IncludeDueDateRange.SELECT_DUE_DATE_RANGE);
                criteria.setDueDateRange(new DateRange(localDate, localDate.plus(config.getGranularity().getPeriod()).minusDays(1)));
                criteria.setWorkflowState(WorkflowState.CLOSED);
                break;
        }
		return criteria;
	}

    private EventReportCriteria getCriteriaDefaults(WorkWidgetConfiguration config, String series, int i) {
        EventReportCriteria criteria = getDefaultReportCriteria(config.getOrg());
        criteria.setAssetType(config.getAssetType());
        criteria.setEventType(config.getEventType());
        criteria.setAssignee(config.getUser());
        criteria.setWorkflowState(WorkflowState.OPEN);
        return criteria;
    }

    /*package protected for testing purposes */
    EventReportCriteria getDefaultReportCriteria(BaseOrg org) {
		EventReportCriteria criteria = getDefaultReportCriteria();
        criteria.setOwner(org);
        return criteria;
	}

    private void setDateRange(EventReportCriteria criteria, ChartGranularity granularity, LocalDate localDate) {
        if (granularity!=null) {
            LocalDate from = localDate;
            LocalDate to = from.plus(granularity.getPeriod().minusDays(1));  // subtract one day because it is inclusive.
            criteria.setDateRange(new DateRange(from, to));
        }
    }



    /**
     * asset widget related config --> criteria methods.
     */

	public AssetSearchCriteria convertWidgetDefinitionToAssetCriteria(Long widgetDefinitionId, Long x, String y, String series) {
		WidgetDefinition<?> widgetDefinition = getWidgetDefinition(widgetDefinitionId);		
		switch (widgetDefinition.getWidgetType()) { 
			case ASSETS_IDENTIFIED:
				return getCriteriaDefaults(((AssetsIdentifiedWidgetConfiguration) widgetDefinition.getConfig()), new LocalDate(x));
			case ASSETS_STATUS:
				return getCriteriaDefaults(((AssetsStatusWidgetConfiguration) widgetDefinition.getConfig()), y);
			default: 
				throw new IllegalArgumentException("Can't convert widget of type " + widgetDefinition.getWidgetType() + " into report criteria");
		}
	}

	private AssetSearchCriteria getCriteriaDefaults(
            AssetsIdentifiedWidgetConfiguration config,
            LocalDate localDate) {
		AssetSearchCriteria criteria = getDefaultAssetSearchCritieria();
		if (config.getGranularity()!=null) { 
			LocalDate from = localDate;
			LocalDate to = from.plus(config.getGranularity().getPeriod().minusDays(1));
			criteria.setDateRange(new DateRange(from, to));
		}
        criteria.setOwner(config.getOrg());
		return criteria;
	}

	private AssetSearchCriteria getCriteriaDefaults(
            AssetsStatusWidgetConfiguration config, String assetStatus) {
		AssetSearchCriteria criteria = getDefaultAssetSearchCritieria();
		criteria.setAssetStatus(assetStatusService.getStatusByName(assetStatus));
		criteria.setDateRange(new DateRange(config.getRangeType()));
        criteria.setOwner(config.getOrg());
		return criteria;
	}

	public AssetSearchCriteria getDefaultAssetSearchCritieria() {
		AssetSearchCriteria criteria = new AssetSearchCriteria();
		ReportConfiguration reportConfiguration = new AssetColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());		
		criteria.setColumnGroups(reportConfiguration.getColumnGroups());
        criteria.setSortColumn(reportConfiguration.getSortColumn());
        criteria.setSortDirection(reportConfiguration.getSortDirection());
        criteria.setReportAlreadyRun(true);
		return criteria;
	}

    public EventReportCriteria getDefaultReportCriteria() {
        EventReportCriteria criteria = new EventReportCriteria();
        ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());
        criteria.setColumnGroups(reportConfiguration.getColumnGroups());
        criteria.setSortColumn(reportConfiguration.getSortColumn());
        criteria.setSortDirection(reportConfiguration.getSortDirection());
        criteria.setReportAlreadyRun(true);
        return criteria;
    }
}
