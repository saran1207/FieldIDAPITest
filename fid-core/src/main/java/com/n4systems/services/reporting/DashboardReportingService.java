package com.n4systems.services.reporting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.Status;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.chart.CalendarChartManager;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.StringChartManager;
import com.n4systems.util.persistence.GroupByClause;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.WhereParameterGroup;

// TODO DD : CACHEABLE!!!  this is used for getting old, unchangeable data.  use EMcache?

public class DashboardReportingService extends FieldIdPersistenceService {
	
	// TODO DD : unit tests...
	
	@Autowired
	private PersistenceService persistenceService;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartSeries<Calendar>> getAssetsIdentified(ChartDateRange dateRange, ChartGranularity granularity, BaseOrg org) {
		QueryBuilder<AssetsIdentifiedReportRecord> builder = new QueryBuilder<AssetsIdentifiedReportRecord>(Asset.class, securityContext.getUserSecurityFilter());
		
		NewObjectSelect select = new NewObjectSelect(AssetsIdentifiedReportRecord.class);
		List<String> args = Lists.newArrayList("COUNT(*)");
		args.addAll(getSelectConstructorArgsByGranularity("identified", granularity));
		select.setConstructorArgs(args);
		
		builder.setSelectArgument(select);
		builder.addGroupByClauses(getGroupByClausesByGranularity(granularity,"identified"));
		ChartDateRange range = dateRange==null ? ChartDateRange.FOREVER : dateRange;
		builder.addWhere(Comparator.GE, "identified", "identified", range.getFromDate());
		if (org!=null) { 
			builder.addSimpleWhere("owner.id", org.getId());
		}
		builder.addOrder("identified");
		
		List<AssetsIdentifiedReportRecord> results = persistenceService.findAll(builder);
				
        return Lists.newArrayList(new ChartSeries<Calendar>(results).withChartManager(new CalendarChartManager(granularity)));
    }



	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartSeries<Calendar>> getUpcomingScheduledEvents(Integer period, BaseOrg owner) {
		
		QueryBuilder<UpcomingScheduledEventsRecord> builder = new QueryBuilder<UpcomingScheduledEventsRecord>(EventSchedule.class, securityContext.getUserSecurityFilter());
		
		builder.setSelectArgument(new NewObjectSelect(UpcomingScheduledEventsRecord.class, "nextDate", "COUNT(*)"));
		Date today = new PlainDate();
		Date endDate = DateUtils.addDays(today, period);
		
		WhereParameterGroup filtergroup = new WhereParameterGroup("filtergroup");
		
		filtergroup.addClause(WhereClauseFactory.create(Comparator.GE, "fromDate", "nextDate", today, null, ChainOp.AND));
		filtergroup.addClause(WhereClauseFactory.create(Comparator.LE, "toDate", "nextDate", endDate, null, ChainOp.AND));
		
		builder.addWhere(filtergroup);
		builder.addSimpleWhere("status", ScheduleStatus.SCHEDULED);

		if(owner != null) {
			builder.addSimpleWhere("owner", owner);
		}

		builder.addGroupBy("nextDate");
		List<UpcomingScheduledEventsRecord> results = persistenceService.findAll(builder);
		
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
	
	@SuppressWarnings("unchecked")
	public List<ChartSeries<String>> getAssetsStatus(ChartDateRange chartDateRange, BaseOrg org) {
		QueryBuilder<AssetsStatusReportRecord> builder = new QueryBuilder<AssetsStatusReportRecord>(Asset.class, securityContext.getUserSecurityFilter());
		
//		SELECT u.status, u.v, @rownum:=@rownum+1 AS rownum
//		FROM (
//		   select ass.name as status, count(*) as v from assets a, assetstatus ass 
//		where a.tenant_id = 15511493 and a.state = 'ACTIVE' and a.assetstatus_id=ass.id
//		group by ass.name
//		) u,
//		(SELECT @rownum:=0) r		
		
		builder.setSelectArgument(new NewObjectSelect(AssetsStatusReportRecord.class, "assetStatus.name", "COUNT(*)"));		
		builder.addWhere(Comparator.GE, "identified", "identified", chartDateRange.getFromDate());
		builder.addGroupBy("assetStatus.name");
		if (org!=null) { 
			builder.addSimpleWhere("owner.id", org.getId());
		}		
		
		List<AssetsStatusReportRecord> results = persistenceService.findAll(builder);
				
        return Lists.newArrayList(new ChartSeries<String>(results).withChartManager(new StringChartManager(true)));
	}	
	
	public List<ChartSeries<Calendar>> getCompletedEvents(ChartDateRange chartDateRange, ChartGranularity granularity, BaseOrg org) {
		QueryBuilder<CompletedEventsReportRecord> builder = new QueryBuilder<CompletedEventsReportRecord>(Event.class, securityContext.getUserSecurityFilter());
		
		NewObjectSelect select = new NewObjectSelect(CompletedEventsReportRecord.class);
		List<String> args = Lists.newArrayList("COUNT(*)", "status");
		args.addAll(getSelectConstructorArgsByGranularity("date", granularity));
		select.setConstructorArgs(args);
		builder.setSelectArgument(select);
		
		builder.addWhere(Comparator.GE, "date", "date", chartDateRange.getFromDate());
		builder.addGroupByClauses(getGroupByClausesByGranularity(granularity,"date"));		
		if (org!=null) { 
			builder.addSimpleWhere("owner.id", org.getId());
		}
		builder.addOrder("date");
		
		ArrayList<ChartSeries<Calendar>> results = new ArrayList<ChartSeries<Calendar>>();
		// first add all events...
		results.add(new ChartSeries<Calendar>("All", persistenceService.findAll(builder)).withChartManager(new CalendarChartManager(granularity)));	
		
		for (Status status:Status.values()) {
			// ...then group them by status.
			builder.addSimpleWhere("status", status);
			List<CompletedEventsReportRecord> events = persistenceService.findAll(builder);
			results.add(new ChartSeries<Calendar>(status.getDisplayName(), events).withChartManager(new CalendarChartManager(granularity)));	
		}
		return results;
	}
	
	public EventKpiRecord getEventKpi(BaseOrg owner) {
		EventKpiRecord eventKpiRecord = new EventKpiRecord();	
		eventKpiRecord.setCustomer(owner);
		
		QueryBuilder<EventScheduleStatusCount> builder1 = new QueryBuilder<EventScheduleStatusCount>(EventSchedule.class, securityContext.getUserSecurityFilter());
		builder1.setSelectArgument(new NewObjectSelect(EventScheduleStatusCount.class, "status", "COUNT(*)"));
		builder1.addSimpleWhere("owner.id", owner.getId());
		builder1.addGroupBy("status");
		List<EventScheduleStatusCount> statusCounts = persistenceService.findAll(builder1);
		
		for (EventScheduleStatusCount statusCount: statusCounts ) {
			if(statusCount.status.equals(ScheduleStatus.COMPLETED))
				eventKpiRecord.setCompleted(statusCount.count);
			if(statusCount.status.equals(ScheduleStatus.IN_PROGRESS))
				eventKpiRecord.setInProgress(statusCount.count);
			if(statusCount.status.equals(ScheduleStatus.SCHEDULED))
				eventKpiRecord.setScheduled(statusCount.count);			
		}

		QueryBuilder<EventSchedule> builder2 = new QueryBuilder<EventSchedule>(EventSchedule.class, securityContext.getUserSecurityFilter());
		builder2.addSimpleWhere("owner.id", owner.getId());
		builder2.addSimpleWhere("status", ScheduleStatus.COMPLETED);
		builder2.addSimpleWhere("event.status", Status.FAIL);
		
		Long failedCount = persistenceService.count(builder2);
		
		eventKpiRecord.setFailed(failedCount);
		
		return eventKpiRecord;
	}
		
	private List<GroupByClause> getGroupByClausesByGranularity(ChartGranularity granularity, String param) {
		ArrayList<GroupByClause> result = Lists.newArrayList();
		
		if (granularity==null) {
			return result; 
		}
		
		result.add(	new GroupByClause("YEAR("+param+")", true) );

		switch (granularity) { 
		case DAY:			
			result.add(new GroupByClause("DAY("+param+")", true));
			break;
		case WEEK:
			result.add(new GroupByClause("WEEK("+param+")", true));
			break;
		case MONTH:
			result.add(new GroupByClause("MONTH("+param+")", true));			
			break;
		case QUARTER:
			result.add(new GroupByClause("QUARTER("+param+")", true));
			break;
		case YEAR:
		default:
			// do nothing...default of group by year is good.
		}
		return result;
	}

	private List<String> getSelectConstructorArgsByGranularity(String param, ChartGranularity granularity) {
		// e.g. will return "YEAR(date)", "QUARTER(date)", "WEEK(date)", "DAYOFYEAR(date)" if you want all this data. 
		//  so, for quarterly data will return "YEAR(date)", "QUARTER(date)",  "-1",  "-1"   because all other fields are irrelevant.
		
		String day = granularity.ordinal()<=ChartGranularity.DAY.ordinal() ? "DAYOFYEAR("+param+")" : "-1";
		String week = granularity.ordinal()<=ChartGranularity.WEEK.ordinal() ? "WEEK("+param+")" : "-1";
		String month = granularity.ordinal()<=ChartGranularity.MONTH.ordinal() ? "MONTH("+param+")" : "-1";
		String quarter = granularity.ordinal()<=ChartGranularity.QUARTER.ordinal() ? "QUARTER("+param+")" : "-1";
		String year = "YEAR("+param+")";   // year required for all calendars.  coarsest unit.

		return Lists.newArrayList(year, quarter, month, week, day);
	}
		

}
