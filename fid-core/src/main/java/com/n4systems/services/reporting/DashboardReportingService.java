package com.n4systems.services.reporting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.chart.CalendarChartManager;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDataGranularity;
import com.n4systems.util.chart.StringChartManager;
import com.n4systems.util.persistence.GroupByClause;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.WhereParameterGroup;

// CACHEABLE!!!  this is used for getting old data.

public class DashboardReportingService extends FieldIdPersistenceService {
	
	// TODO DD : unit tests...
	
	@Autowired
	private PersistenceService persistenceService;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartData<Calendar>> getAssetsIdentified(ChartDataGranularity granularity, BaseOrg org) {
		QueryBuilder<AssetsIdentifiedReportRecord> builder = new QueryBuilder<AssetsIdentifiedReportRecord>(Asset.class, securityContext.getUserSecurityFilter());
		
		builder.setSelectArgument(new NewObjectSelect(AssetsIdentifiedReportRecord.class, "YEAR(identified)", "QUARTER(identified)", "WEEK(identified)", "DAYOFYEAR(identified)", "COUNT(*)"));
		builder.addGroupByClauses(getGroupByClauses(granularity,"identified"));
		builder.addWhere(Comparator.GE, "identified", "identified", getEarliestAssetDate());
		if (org!=null) { 
			builder.addSimpleWhere("owner.id", org.getId());
		}
		builder.addOrder("identified");
		
		List<AssetsIdentifiedReportRecord> results = persistenceService.findAll(builder);
				
        return Lists.newArrayList(new ChartData<Calendar>().withChartManager(new CalendarChartManager(granularity)).add(results));
    }


	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartData<Calendar>> getUpcomingScheduledEvents(Integer period, BaseOrg owner) {
		
		QueryBuilder<UpcomingScheduledEventsRecord> builder = new QueryBuilder<UpcomingScheduledEventsRecord>(EventSchedule.class, securityContext.getUserSecurityFilter());
		
		builder.setSelectArgument(new NewObjectSelect(UpcomingScheduledEventsRecord.class, "nextDate", "COUNT(*)"));
		Date today = new PlainDate();
		
		WhereParameterGroup filtergroup = new WhereParameterGroup("filtergroup");
		
		filtergroup.addClause(WhereClauseFactory.create(Comparator.GE, "fromDate", "nextDate", today, null, ChainOp.AND));
		filtergroup.addClause(WhereClauseFactory.create(Comparator.LE, "toDate", "nextDate", DateUtils.addDays(today, period), null, ChainOp.AND));
		
		builder.addWhere(filtergroup);
		builder.addSimpleWhere("status", ScheduleStatus.SCHEDULED);

		if(owner != null) {
			builder.addSimpleWhere("owner", owner);
		}

		builder.addGroupBy("nextDate");
		List<UpcomingScheduledEventsRecord> results = persistenceService.findAll(builder);
		
        return Lists.newArrayList(new ChartData<Calendar>().add(results));
	}
	
	@SuppressWarnings("unchecked")
	public List<ChartData<String>> getAssetsStatus(BaseOrg org) {
		QueryBuilder<AssetsStatusReportRecord> builder = new QueryBuilder<AssetsStatusReportRecord>(Asset.class, securityContext.getUserSecurityFilter());
		
//		SELECT u.status, u.v, @rownum:=@rownum+1 AS rownum
//		FROM (
//		   select ass.name as status, count(*) as v from assets a, assetstatus ass 
//		where a.tenant_id = 15511493
//		    and a.state = 'ACTIVE'
//		    and a.assetstatus_id=ass.id
//		group by ass.name
//
//		) u,
//		(SELECT @rownum:=0) r		
		
		builder.setSelectArgument(new NewObjectSelect(AssetsStatusReportRecord.class, "assetStatus.name", "COUNT(*)"));		
		builder.addWhere(Comparator.GE, "identified", "identified", getEarliestAssetDate());
		builder.addGroupBy("assetStatus.name");
		if (org!=null) { 
			builder.addSimpleWhere("owner.id", org.getId());
		}		
		
		List<AssetsStatusReportRecord> results = persistenceService.findAll(builder);
				
        return Lists.newArrayList(new ChartData<String>().withChartManager(new StringChartManager(true)).add(results));
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ChartData<Calendar>> getCompletedEvents(ChartDataGranularity granularity/*TODO DD : use granularity/group by */, BaseOrg org) {
		QueryBuilder<CompletedEventsReportRecord> builder = new QueryBuilder<CompletedEventsReportRecord>(Event.class, securityContext.getUserSecurityFilter());
		
		builder.setSelectArgument(new NewObjectSelect(CompletedEventsReportRecord.class, "status", "COUNT(*)", "YEAR(date)", "QUARTER(date)", "WEEK(date)", "DAYOFYEAR(date)"));		
		builder.addWhere(Comparator.GE, "date", "date", getEarliestAssetDate());
		builder.addGroupByClauses(getGroupByClauses(granularity,"date"));
		if (org!=null) { 
			builder.addSimpleWhere("owner.id", org.getId());
		}		
		builder.addOrder("date");
		
//		ArrayList<ChartData<Calendar>> results = new ArrayList<ChartData<Calendar>>();
//		for (Status status:status.values()) {
			List<CompletedEventsReportRecord> results = persistenceService.findAll(builder);
				
        return Lists.newArrayList(new ChartData<Calendar>().withChartManager(new CalendarChartManager(granularity)).add(results));
	}
		
	private List<GroupByClause> getGroupByClauses(ChartDataGranularity granularity, String param) {
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
		case ALL:
		case YEAR:
		default:
			// do nothing...default of group by year is good.
		}
		return result;
	}

	
	// TODO DD : put in util pkg.
	private Date getEarliestAssetDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR,2007);
		return calendar.getTime();
	}



}
