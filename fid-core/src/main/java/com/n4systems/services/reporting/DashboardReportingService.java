package com.n4systems.services.reporting;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.model.Event;
import com.n4systems.model.Status;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.*;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.date.DateService;
import com.n4systems.util.EnumUtils;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
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


    // use DateService.
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
        results.add(new ChartSeries<LocalDate>(Status.ALL, Status.ALL.getLabel(), completedEvents));

		for (Status status:Status.getValidEventStates()) {
			completedEvents = eventService.getCompletedEvents(from, to, org, status, granularity);
			results.add(new ChartSeries<LocalDate>(status, status.getDisplayName(), completedEvents));
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

		List<EventCompletenessReportRecord> allScheduledEvents = eventService.getEventCompleteness(granularity, getFrom(granularity, dateRange), getTo(granularity, dateRange),  org);
		List<EventCompletenessReportRecord> completedScheduledEvents = eventService.getEventCompleteness(Event.EventState.OPEN, granularity, getFrom(granularity, dateRange), getTo(granularity, dateRange), org);

		List<ChartSeries<LocalDate>> results = new ArrayList<ChartSeries<LocalDate>>();
		ChartSeries<LocalDate> completedChartSeries = new ChartSeries<LocalDate>(EventState.COMPLETE, Event.EventState.COMPLETED.getLabel(), completedScheduledEvents);
        results.add(completedChartSeries);
		ChartSeries<LocalDate> allChartSeries = new ChartSeries<LocalDate>(EventState.ALL_STATES, EventState.ALL_STATES.getLabel(), allScheduledEvents);
		results.add(allChartSeries);
        // hack. because ALL isn't the exact same on reporting, we will just disable the click thru on it now.
        //  COMPLETED is still clickable though by default.
        allChartSeries.setClickable(false);

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
	
	public EventReportCriteria convertWidgetDefinitionToReportCriteria(Long widgetDefinitionId, Long x, String series) {
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
            case COMMON_LINKS:
            	return getCriteriaDefaults();
			default: 
				throw new IllegalArgumentException("Can't convert widget of type " + widgetDefinition.getWidgetType() + " into report criteria");
		}
	}

    private EventReportCriteria getCriteriaDefaults() {
    	EventReportCriteria criteria = getDefaultReportCriteria();
    	criteria.setEventState(EventState.OPEN);
		return criteria;
	}

	private WidgetDefinition<?> getWidgetDefinition(Long widgetDefinitionId) {
		return persistenceService.findNonSecure(WidgetDefinition.class, widgetDefinitionId);
	}

	private EventReportCriteria getCriteriaDefaults(UpcomingEventsWidgetConfiguration config, String series, LocalDate localDate) {
		EventReportCriteria criteria = getDefaultReportCriteria(config.getOrg());
        criteria.setDueDateRange(new DateRange(localDate, localDate));// this widget displays day by day.  .: when you click on a pt the date range is always a single day.
        criteria.setEventState(EventState.OPEN);
		return criteria;
	}

    private EventReportCriteria getCriteriaDefaults(EventKPIWidgetConfiguration config, String series, int orgIndex) {
        Preconditions.checkArgument(orgIndex >= 0 && orgIndex <=config.getOrgs().size());
        EventReportCriteria criteria = getDefaultReportCriteria(config.getOrgs().get(orgIndex));
        if (KpiType.INCOMPLETE.getLabel().equals(series)) {
            criteria.setEventState(EventState.OPEN);
            criteria.setDueDateRange(config.getDateRange());
            criteria.setDateRange(new DateRange(RangeType.FOREVER));
        } else if (KpiType.FAILED.getLabel().equals(series)) {
            criteria.setIncludeDueDateRange(IncludeDueDateRange.SELECT_DUE_DATE_RANGE);
            criteria.setDueDateRange(config.getDateRange());
            criteria.setResult(Status.FAIL);
            criteria.setDateRange(new DateRange(RangeType.FOREVER));
        } else if (KpiType.COMPLETED.getLabel().equals(series)) {
            criteria.setIncludeDueDateRange(IncludeDueDateRange.SELECT_DUE_DATE_RANGE);
            criteria.setDueDateRange(config.getDateRange());
            criteria.setEventState(EventState.COMPLETE);
            criteria.setDateRange(new DateRange(RangeType.FOREVER));
        }
        return criteria;
    }

    private EventReportCriteria getCriteriaDefaults(CompletedEventsWidgetConfiguration config, String series, LocalDate localDate) {
        EventReportCriteria criteria = getDefaultReportCriteria(config.getOrg());
        criteria.setResult(EnumUtils.valueOf(Status.class, series));
        setDateRange(criteria, config.getGranularity(), localDate);
        return criteria;
    }

	private EventReportCriteria getCriteriaDefaults(EventCompletenessWidgetConfiguration config, String series, LocalDate localDate) {
		EventReportCriteria criteria = getDefaultReportCriteria(config.getOrg());
        EventState state = EnumUtils.valueOf(EventState.class, series);
        if (EventState.COMPLETE.equals(state)) {
            criteria.setIncludeDueDateRange(IncludeDueDateRange.SELECT_DUE_DATE_RANGE);
            criteria.setDueDateRange(new DateRange(localDate, localDate.plus(config.getGranularity().getPeriod()).minusDays(1)));
            criteria.setEventState(EventState.COMPLETE);
        } else {
            criteria.setEventState(EventState.ALL);
            criteria.setDateRange(new DateRange(localDate, localDate.plus(config.getGranularity().getPeriod()).minusDays(1)));
        }
		return criteria;
	}

    private EventReportCriteria getDefaultReportCriteria(BaseOrg org) {
		EventReportCriteria criteria = new EventReportCriteria();
        ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());
        criteria.setColumnGroups(reportConfiguration.getColumnGroups());
        criteria.setSortColumn(reportConfiguration.getSortColumn());
        criteria.setSortDirection(reportConfiguration.getSortDirection());
        criteria.setReportAlreadyRun(true);
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
            criteria.setOwner(config.getOrg());
		}
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
