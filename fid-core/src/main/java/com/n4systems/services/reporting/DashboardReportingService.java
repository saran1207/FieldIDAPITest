package com.n4systems.services.reporting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.Status;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.chart.BarChartManager;
import com.n4systems.util.chart.CalendarChartManager;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;


public class DashboardReportingService extends FieldIdPersistenceService {

	@Autowired private AssetService assetService;
	@Autowired private EventService eventService;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartSeries<Calendar>> getAssetsIdentified(ChartDateRange dateRange, ChartGranularity granularity, BaseOrg owner) {
		Preconditions.checkArgument(dateRange!=null);
		List<AssetsIdentifiedReportRecord> results = assetService.getAssetsIdentified(granularity, dateRange.getFromDate(), dateRange.getToDate(), owner);
		ChartSeries<Calendar> chartSeries = new ChartSeries<Calendar>(results).withChartManager(new CalendarChartManager(granularity, dateRange));		
        return Lists.newArrayList(chartSeries);
    }

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartSeries<Calendar>> getUpcomingScheduledEvents(Integer period, BaseOrg owner) {
		Preconditions.checkArgument(period!=null);		
		List<UpcomingScheduledEventsRecord> results = eventService.getUpcomingScheduledEvents(period, owner);

		Date today = new PlainDate();
		Date endDate = DateUtils.addDays(today, period);
		
		List<UpcomingScheduledEventsRecord> fullResults = new ArrayList<UpcomingScheduledEventsRecord>();		
		Iterator<UpcomingScheduledEventsRecord> iterator = results.iterator();
		UpcomingScheduledEventsRecord record = iterator.hasNext() ? iterator.next() : null;
		
		for(Date i = today; i.before(DateUtils.addDays(endDate, 1)); i = DateUtils.addDays(i, 1) ) {		
			if(record != null && record.getX().getTimeInMillis() == i.getTime()) {
				fullResults.add(record);
				if(iterator.hasNext())
					record = iterator.next();
			} else {
				fullResults.add(new UpcomingScheduledEventsRecord(i, 0L));
			}
		}
		return Lists.newArrayList(new ChartSeries<Calendar>(fullResults));
	}
	
	public List<ChartSeries<String>> getAssetsStatus(ChartDateRange dateRange, BaseOrg org) {
		Preconditions.checkArgument(dateRange!=null);		
		List<AssetsStatusReportRecord> results = assetService.getAssetsStatus(dateRange.getFromDate(), dateRange.getToDate(), org);		
        ChartSeries<String> chartSeries = new ChartSeries<String>(results).withChartManager(new BarChartManager(true));
        return new ChartData<String>(chartSeries);
	}		
	
	public List<ChartSeries<Calendar>> getCompletedEvents(ChartDateRange dateRange, ChartGranularity granularity, BaseOrg org) {
		Preconditions.checkArgument(dateRange!=null);				
		List<ChartSeries<Calendar>> results = new ArrayList<ChartSeries<Calendar>>();
		
		List<CompletedEventsReportRecord> completedEvents = eventService.getCompletedEvents(dateRange.getFromDate(), dateRange.getToDate(), org, null, granularity);		
		results.add(new ChartSeries<Calendar>("All", completedEvents).withChartManager(new CalendarChartManager(granularity, dateRange)));

		for (Status status:Status.values()) { 
			completedEvents = eventService.getCompletedEvents(dateRange.getFromDate(), dateRange.getToDate(), org, status, granularity);		
			results.add(new ChartSeries<Calendar>(status.getDisplayName(), completedEvents).withChartManager(new CalendarChartManager(granularity, dateRange)));
		}
				
		return results;
	}
	
	public EventKpiRecord getEventKpi(BaseOrg owner, ChartDateRange dateRange) {
		Preconditions.checkArgument(dateRange!=null);				
		return eventService.getEventKpi(dateRange.getFromDate(), dateRange.getToDate(), owner);
	}

	public List<ChartSeries<Calendar>> getEventCompletenessEvents(ChartGranularity granularity, ChartDateRange dateRange, BaseOrg org) {
		List<EventCompletenessReportRecord> allScheduledEvents = eventService.getEventCompleteness(granularity, dateRange.getFromDate(), dateRange.getToDate(), org);
		List<EventCompletenessReportRecord> completedScheduledEvents = eventService.getEventCompleteness(ScheduleStatus.COMPLETED, granularity, dateRange.getFromDate(), dateRange.getToDate(), org);
		
		List<ChartSeries<Calendar>> results = new ArrayList<ChartSeries<Calendar>>();
		ChartSeries<Calendar> allChartSeries = new ChartSeries<Calendar>("All", allScheduledEvents).withChartManager(new CalendarChartManager(granularity, dateRange));
		results.add(allChartSeries);
		ChartSeries<Calendar> completedChartSeries = new ChartSeries<Calendar>("Completed", completedScheduledEvents).withChartManager(new CalendarChartManager(granularity, dateRange));
		results.add(completedChartSeries);
		
		return results;		
	}		
	
	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
	
}
