package com.n4systems.services.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.util.chart.RangeType;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.Status;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.AssetsIdentifiedWidgetConfiguration;
import com.n4systems.model.dashboard.widget.AssetsStatusWidgetConfiguration;
import com.n4systems.model.dashboard.widget.CompletedEventsWidgetConfiguration;
import com.n4systems.model.dashboard.widget.EventCompletenessWidgetConfiguration;
import com.n4systems.model.dashboard.widget.UpcomingEventsWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.EnumUtils;
import com.n4systems.util.chart.BarChartManager;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.DateChartManager;


public class DashboardReportingService extends FieldIdPersistenceService {

	@Autowired private AssetService assetService;
	@Autowired private EventService eventService;
	@Autowired private AssetStatusService assetStatusService;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartSeries<LocalDate>> getAssetsIdentified(DateRange dateRange, ChartGranularity granularity, BaseOrg owner) {
		Preconditions.checkArgument(dateRange !=null);
		List<AssetsIdentifiedReportRecord> results = assetService.getAssetsIdentified(granularity, getFrom(granularity, dateRange), getTo(granularity, dateRange), owner);
		ChartSeries<LocalDate> chartSeries = new ChartSeries<LocalDate>(results).withChartManager(new DateChartManager(granularity, dateRange));
        return Lists.newArrayList(chartSeries);
    }

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartSeries<LocalDate>> getUpcomingScheduledEvents(Integer period, BaseOrg owner) {
		Preconditions.checkArgument(period!=null);		
		List<UpcomingScheduledEventsRecord> results = eventService.getUpcomingScheduledEvents(period, owner);

		DateRange dateRange = new DateRange(RangeType.forDays(period));
		
		return Lists.newArrayList(new ChartSeries<LocalDate>(results).withChartManager(new DateChartManager(ChartGranularity.DAY, dateRange)));
	}
	
	public List<ChartSeries<String>> getAssetsStatus(DateRange dateRange, BaseOrg org) {
		Preconditions.checkArgument(dateRange !=null);
		
		List<AssetsStatusReportRecord> results = assetService.getAssetsStatus(dateRange.getFromDate(), dateRange.getToDate(), org);
        ChartSeries<String> chartSeries = new ChartSeries<String>(results).withChartManager(new BarChartManager(true));
        return new ChartData<String>(chartSeries);
	}		
	
	public List<ChartSeries<LocalDate>> getCompletedEvents(DateRange dateRange, ChartGranularity granularity, BaseOrg org) {
		Preconditions.checkArgument(dateRange !=null);
		List<ChartSeries<LocalDate>> results = new ArrayList<ChartSeries<LocalDate>>();

		Date from = getFrom(granularity, dateRange);
		Date to = getTo(granularity, dateRange);
		List<CompletedEventsReportRecord> completedEvents = eventService.getCompletedEvents(from, to, org, null, granularity);		
		results.add(new ChartSeries<LocalDate>("All", completedEvents).withChartManager(new DateChartManager(granularity, dateRange)));

		for (Status status:Status.values()) { 
			completedEvents = eventService.getCompletedEvents(from, to, org, status, granularity);		
			results.add(new ChartSeries<LocalDate>(status.getDisplayName(), status.name(), completedEvents).withChartManager(new DateChartManager(granularity, dateRange)));
		}
				
		return results;
	}
	
	public EventKpiRecord getEventKpi(BaseOrg owner, DateRange dateRange) {
		Preconditions.checkArgument(dateRange !=null);
		return eventService.getEventKpi(dateRange.getFromDate(), dateRange.getToDate(), owner);
	}

	public List<ChartSeries<LocalDate>> getEventCompletenessEvents(ChartGranularity granularity, DateRange dateRange, BaseOrg org) {
		Preconditions.checkArgument(dateRange !=null);

		List<EventCompletenessReportRecord> allScheduledEvents = eventService.getEventCompleteness(granularity, getFrom(granularity, dateRange), getTo(granularity, dateRange),  org);
		List<EventCompletenessReportRecord> completedScheduledEvents = eventService.getEventCompleteness(ScheduleStatus.COMPLETED, granularity, getFrom(granularity, dateRange), getTo(granularity, dateRange), org);
		
		List<ChartSeries<LocalDate>> results = new ArrayList<ChartSeries<LocalDate>>();
		ChartSeries<LocalDate> allChartSeries = new ChartSeries<LocalDate>("All", allScheduledEvents).withChartManager(new DateChartManager(granularity, dateRange));
		results.add(allChartSeries);
		ChartSeries<LocalDate> completedChartSeries = new ChartSeries<LocalDate>("Completed", completedScheduledEvents).withChartManager(new DateChartManager(granularity, dateRange));
		results.add(completedChartSeries);
		
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
	// NOTE : stuff below is temporary and will have to be refactored when saving reports changes. 
	// in particular, setCommonModelDefaults().
	//
	
	public EventReportCriteriaModel convertWidgetDefinitionToReportCriteria(Long widgetDefinitionId, Long x, String series) {
		WidgetDefinition<?> widgetDefinition = getWidgetDefinition(widgetDefinitionId);
		LocalDate localDate = new LocalDate(x);
		switch (widgetDefinition.getWidgetType()) { 
			case EVENT_COMPLETENESS:
				return getModelDefaults(((EventCompletenessWidgetConfiguration)widgetDefinition.getConfig()), series, localDate);
			case COMPLETED_EVENTS:
				return getModelDefaults(((CompletedEventsWidgetConfiguration)widgetDefinition.getConfig()), series, localDate);
			case UPCOMING_SCHEDULED_EVENTS: 
				return getModelDefaults(((UpcomingEventsWidgetConfiguration)widgetDefinition.getConfig()), series, localDate);
			default: 
				throw new IllegalArgumentException("Can't convert widget of type " + widgetDefinition.getWidgetType() + " into report criteria");
		}
	}

	private WidgetDefinition<?> getWidgetDefinition(Long widgetDefinitionId) {
		return persistenceService.findNonSecure(WidgetDefinition.class, widgetDefinitionId);
	}

	private EventReportCriteriaModel getModelDefaults(UpcomingEventsWidgetConfiguration config, String series, LocalDate localDate) {
		EventReportCriteriaModel model = getDefaultReportCriteriaModel(config.getOrg(), config.getRangeType(), new Period().withDays(config.getPeriod()), localDate);
		return model;
	}

	private EventReportCriteriaModel getModelDefaults(CompletedEventsWidgetConfiguration config, String series, LocalDate localDate) {
		EventReportCriteriaModel model = getDefaultReportCriteriaModel(config.getOrg(), config.getDateRangeType(), config.getGranularity().getPeriod(), localDate);
		model.setResult(EnumUtils.valueOf(Status.class, series));		
		return model;
	}

	private EventReportCriteriaModel getModelDefaults(EventCompletenessWidgetConfiguration config, String series, LocalDate localDate) {
		EventReportCriteriaModel model = getDefaultReportCriteriaModel(config.getOrg(), config.getRangeType(), config.getGranularity().getPeriod(), localDate);
		model.setOwner(config.getOrg());
		return model;
	}

	private EventReportCriteriaModel getDefaultReportCriteriaModel(BaseOrg org, RangeType rangeType, Period period, LocalDate localDate) {
		EventReportCriteriaModel model = new EventReportCriteriaModel();
        ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());
        model.setColumnGroups(reportConfiguration.getColumnGroups());
        model.setSortColumn(reportConfiguration.getSortColumn());
        model.setSortDirection(reportConfiguration.getSortDirection());
        model.setReportAlreadyRun(true);
        
        if (period!=null) { 
        	LocalDate from = localDate;
        	LocalDate to = from.plus(period).minusDays(1);  // recall...it's inclusive so need to subtract one.
        	model.setDateRange(new DateRange(from,to));
        }
        return model;
	}

	public AssetSearchCriteriaModel convertWidgetDefinitionToAssetCriteria(Long widgetDefinitionId, Long x, String y, String series) {
		WidgetDefinition<?> widgetDefinition = getWidgetDefinition(widgetDefinitionId);		
		switch (widgetDefinition.getWidgetType()) { 
			case ASSETS_IDENTIFIED:
				return getModelDefaults(((AssetsIdentifiedWidgetConfiguration)widgetDefinition.getConfig()), new LocalDate(x));
			case ASSETS_STATUS:
				return getModelDefaults(((AssetsStatusWidgetConfiguration)widgetDefinition.getConfig()), y);
			default: 
				throw new IllegalArgumentException("Can't convert widget of type " + widgetDefinition.getWidgetType() + " into report criteria");
		}
	}

	private AssetSearchCriteriaModel getModelDefaults(
			AssetsIdentifiedWidgetConfiguration config,
			LocalDate localDate) {		
		AssetSearchCriteriaModel model = getDefaultAssetSearchModel();		
		if (config.getGranularity()!=null) { 
			LocalDate from = localDate;
			LocalDate to = from.plus(config.getGranularity().getPeriod().minusDays(1));
			model.setDateRange(new DateRange(from,to));
		}
		return model;
	}

	private AssetSearchCriteriaModel getModelDefaults(
			AssetsStatusWidgetConfiguration config, String assetStatus) {
		AssetSearchCriteriaModel model = getDefaultAssetSearchModel();
		model.setAssetStatus(assetStatusService.getStatusByName(assetStatus));
		model.setDateRange(new DateRange(config.getRangeType()));
		return model;
	}

	private AssetSearchCriteriaModel getDefaultAssetSearchModel() {
		AssetSearchCriteriaModel model = new AssetSearchCriteriaModel();
		ReportConfiguration reportConfiguration = new AssetColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());		
		model.setColumnGroups(reportConfiguration.getColumnGroups());
        model.setSortColumn(reportConfiguration.getSortColumn());
        model.setSortDirection(reportConfiguration.getSortDirection());
        model.setReportAlreadyRun(true);		
		return model;
	}
	
}
