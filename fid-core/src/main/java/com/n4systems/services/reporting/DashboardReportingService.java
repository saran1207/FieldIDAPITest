package com.n4systems.services.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.Status;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.CompletedEventsWidgetConfiguration;
import com.n4systems.model.dashboard.widget.EventCompletenessWidgetConfiguration;
import com.n4systems.model.dashboard.widget.UpcomingEventsWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.BarChartManager;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.DateChartManager;


public class DashboardReportingService extends FieldIdPersistenceService {

	@Autowired private AssetService assetService;
	@Autowired private EventService eventService;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartSeries<LocalDate>> getAssetsIdentified(ChartDateRange dateRange, ChartGranularity granularity, BaseOrg owner) {
		Preconditions.checkArgument(dateRange!=null);
		List<AssetsIdentifiedReportRecord> results = assetService.getAssetsIdentified(granularity, getFrom(granularity, dateRange), getTo(granularity, dateRange), owner);
		ChartSeries<LocalDate> chartSeries = new ChartSeries<LocalDate>(results).withChartManager(new DateChartManager(granularity, dateRange));		
        return Lists.newArrayList(chartSeries);
    }

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartSeries<LocalDate>> getUpcomingScheduledEvents(Integer period, BaseOrg owner) {
		Preconditions.checkArgument(period!=null);		
		List<UpcomingScheduledEventsRecord> results = eventService.getUpcomingScheduledEvents(period, owner);

		ChartDateRange dateRange = ChartDateRange.forDays(period);
		
		return Lists.newArrayList(new ChartSeries<LocalDate>(results).withChartManager(new DateChartManager(ChartGranularity.DAY, dateRange)));
	}
	
	public List<ChartSeries<String>> getAssetsStatus(ChartDateRange dateRange, BaseOrg org) {
		Preconditions.checkArgument(dateRange!=null);	
		
		List<AssetsStatusReportRecord> results = assetService.getAssetsStatus(dateRange.getFromDate(), dateRange.getToDate(), org);		
        ChartSeries<String> chartSeries = new ChartSeries<String>(results).withChartManager(new BarChartManager(true));
        return new ChartData<String>(chartSeries);
	}		
	
	public List<ChartSeries<LocalDate>> getCompletedEvents(ChartDateRange dateRange, ChartGranularity granularity, BaseOrg org) {
		Preconditions.checkArgument(dateRange!=null);				
		List<ChartSeries<LocalDate>> results = new ArrayList<ChartSeries<LocalDate>>();

		Date from = getFrom(granularity, dateRange);
		Date to = getTo(granularity, dateRange);
		List<CompletedEventsReportRecord> completedEvents = eventService.getCompletedEvents(from, to, org, null, granularity);		
		results.add(new ChartSeries<LocalDate>("All", completedEvents).withChartManager(new DateChartManager(granularity, dateRange)));

		for (Status status:Status.values()) { 
			completedEvents = eventService.getCompletedEvents(from, to, org, status, granularity);		
			results.add(new ChartSeries<LocalDate>(status.getDisplayName(), completedEvents).withChartManager(new DateChartManager(granularity, dateRange)));
		}
				
		return results;
	}
	
	public EventKpiRecord getEventKpi(BaseOrg owner, ChartDateRange dateRange) {
		Preconditions.checkArgument(dateRange!=null);				
		return eventService.getEventKpi(dateRange.getFromDate(), dateRange.getToDate(), owner);
	}

	public List<ChartSeries<LocalDate>> getEventCompletenessEvents(ChartGranularity granularity, ChartDateRange dateRange, BaseOrg org) {
		Preconditions.checkArgument(dateRange!=null);		

		List<EventCompletenessReportRecord> allScheduledEvents = eventService.getEventCompleteness(granularity, getFrom(granularity,dateRange), getTo(granularity,dateRange),  org);
		List<EventCompletenessReportRecord> completedScheduledEvents = eventService.getEventCompleteness(ScheduleStatus.COMPLETED, granularity, getFrom(granularity,dateRange), getTo(granularity,dateRange), org);
		
		List<ChartSeries<LocalDate>> results = new ArrayList<ChartSeries<LocalDate>>();
		ChartSeries<LocalDate> allChartSeries = new ChartSeries<LocalDate>("All", allScheduledEvents).withChartManager(new DateChartManager(granularity, dateRange));
		results.add(allChartSeries);
		ChartSeries<LocalDate> completedChartSeries = new ChartSeries<LocalDate>("Completed", completedScheduledEvents).withChartManager(new DateChartManager(granularity, dateRange));
		results.add(completedChartSeries);
		
		return results;		
	}

	private Date getTo(ChartGranularity granularity, ChartDateRange dateRange) {		
		LocalDate to = dateRange.getTo();		
		return (to==null) ? null : granularity.roundUp(to).toDate();
	}

	private Date getFrom(ChartGranularity granularity, ChartDateRange dateRange) {
		LocalDate from = dateRange.getFrom();		
		return (from==null) ? null : granularity.roundDown(from).toDate();
	}		


	// ------------------------------------------------------------------------------------------------
	// 
	// NOTE : stuff below is temporary and will have to be refactored when saving reports changes. 
	// in particular, setCommonModelDefaults().
	//
	
	public EventReportCriteriaModel convertWidgetDefinitionToCriteria(Long widgetDefinitionId, Long time) {
		WidgetDefinition<?> widgetDefinition = persistenceService.findNonSecure(WidgetDefinition.class, widgetDefinitionId);
		EventReportCriteriaModel model = new EventReportCriteriaModel();
		switch (widgetDefinition.getWidgetType()) { 
			case EVENT_COMPLETENESS:
				setModelDefaults(model, ((EventCompletenessWidgetConfiguration)widgetDefinition.getConfig()), time);
				break;
			case COMPLETED_EVENTS:
				setModelDefaults(model, ((CompletedEventsWidgetConfiguration)widgetDefinition.getConfig()), time);
				break;
			case UPCOMING_SCHEDULED_EVENTS: 
				setModelDefaults(model, ((UpcomingEventsWidgetConfiguration)widgetDefinition.getConfig()), time);
				break;
			default: 
				throw new IllegalArgumentException("Can't convert widget of type " + widgetDefinition.getWidgetType() + " into report criteria");
		}
		// from, to. type of data.   i.e. assets identified, events failed, etc...
		return model;
	}

	private void setModelDefaults(EventReportCriteriaModel model, UpcomingEventsWidgetConfiguration config, Long time) {		
		setCommonModelDefaults(model, config.getDateRange(), new Period().withDays(config.getPeriod()), time);
	}

	private void setModelDefaults(EventReportCriteriaModel model, CompletedEventsWidgetConfiguration config, Long time) {		
		setCommonModelDefaults(model, config.getDateRange(), config.getGranularity().getPeriod(), time);
	}

	private void setModelDefaults(EventReportCriteriaModel model, EventCompletenessWidgetConfiguration config, Long time) {
		setCommonModelDefaults(model, config.getDateRange(), config.getGranularity().getPeriod(), time);
	}

	private void setCommonModelDefaults(EventReportCriteriaModel model, ChartDateRange chartDateRange, Period period, Long time) {
        if (!model.isReportAlreadyRun()) {
            ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());
            model.setColumnGroups(reportConfiguration.getColumnGroups());
            model.setSortColumn(reportConfiguration.getSortColumn());
            model.setSortDirection(reportConfiguration.getSortDirection());
            model.setReportAlreadyRun(true);
        }
        if (period!=null) { 
        	LocalDate from = new LocalDate(time);
        	LocalDate to = from.plus(period.minusDays(1));  // subtract one since this will yield non-inclusive date.   i.e. we want Jan1-Jan7 NOT Jan1-Jan8 when searching for a week.
        	model.setDateRange(new DateRange(from,to));
        }		
	}    	
	
}
