package com.n4systems.services.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
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
		LocalDate from  = granularity.roundDown(dateRange.getFrom());
		LocalDate to = granularity.roundUp(dateRange.getTo());
		List<AssetsIdentifiedReportRecord> results = assetService.getAssetsIdentified(granularity, from.toDate(), to.toDate(), owner);
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

		Date from  = granularity.roundDown(dateRange.getFrom()).toDate();
		Date to = granularity.roundUp(dateRange.getTo()).toDate();

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

		Date from  = granularity.roundDown(dateRange.getFrom()).toDate();
		Date to = granularity.roundUp(dateRange.getTo()).toDate();
		
		List<EventCompletenessReportRecord> allScheduledEvents = eventService.getEventCompleteness(granularity, from, to,  org);
		List<EventCompletenessReportRecord> completedScheduledEvents = eventService.getEventCompleteness(ScheduleStatus.COMPLETED, granularity, dateRange.getFromDate(), dateRange.getToDate(), org);
		
		List<ChartSeries<LocalDate>> results = new ArrayList<ChartSeries<LocalDate>>();
		ChartSeries<LocalDate> allChartSeries = new ChartSeries<LocalDate>("All", allScheduledEvents).withChartManager(new DateChartManager(granularity, dateRange));
		results.add(allChartSeries);
		ChartSeries<LocalDate> completedChartSeries = new ChartSeries<LocalDate>("Completed", completedScheduledEvents).withChartManager(new DateChartManager(granularity, dateRange));
		results.add(completedChartSeries);
		
		return results;		
	}		
	
}
