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
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDataGranularity;
import com.n4systems.util.persistence.GroupByClause;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereParameter.Comparator;

// CACHEABLE!!!  this is used for getting old data.

public class ReportingService extends FieldIdPersistenceService {
	
	// TODO DD : unit tests...
	
	@Autowired
	private PersistenceService persistenceService;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartData<Calendar>> getAssetsIdentified(ChartDataGranularity granularity, BaseOrg org) {
		QueryBuilder<AssetsIdentifiedReportRecord> builder = new QueryBuilder<AssetsIdentifiedReportRecord>(Asset.class, securityContext.getUserSecurityFilter());
		
		builder.setSelectArgument(new NewObjectSelect(AssetsIdentifiedReportRecord.class, "YEAR(identified)", "QUARTER(identified)", "WEEK(identified)", "DAYOFYEAR(identified)", "COUNT(*)"));
		builder.addGroupByClauses(getGroupByClauses(granularity));
		builder.addWhere(Comparator.GE, "identified", "identified", getEarliestAssetDate());
		if (org!=null) { 
			builder.addSimpleWhere("owner.id", org.getId());
		}
		builder.addOrder("identified");
		
		List<AssetsIdentifiedReportRecord> results = persistenceService.findAll(builder);
				
        return Lists.newArrayList(new ChartData<Calendar>().withGranularity(granularity).add(results));
    }


	// TODO DD : put in util pkg.
	private Date getEarliestAssetDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR,2007);
		return calendar.getTime();
	}

	private List<GroupByClause> getGroupByClauses(ChartDataGranularity granularity) {
		ArrayList<GroupByClause> result = Lists.newArrayList();
		
		if (granularity==null) {
			return result; 
		}
		
		result.add(	new GroupByClause("YEAR(identified)", true) );

		switch (granularity) { 
		case DAY:			
			result.add(new GroupByClause("DAY(identified)", true));
			break;
		case WEEK:
			result.add(new GroupByClause("WEEK(identified)", true));
			break;
		case MONTH:
			result.add(new GroupByClause("MONTH(identified)", true));			
			break;
		case QUARTER:
			result.add(new GroupByClause("QUARTER(identified)", true));
			break;
		case ALL:
		case YEAR:
		default:
			// do nothing...default of group by year is good.
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartData<Calendar>> getUpcomingScheduledEvents() {
		
		QueryBuilder<UpcomingScheduledEventsRecord> builder = new QueryBuilder<UpcomingScheduledEventsRecord>(EventSchedule.class, securityContext.getUserSecurityFilter());
		
		builder.setSelectArgument(new NewObjectSelect(UpcomingScheduledEventsRecord.class, "nextDate", "COUNT(*)"));
		Date today = new PlainDate();
		
		WhereParameterGroup filtergroup = new WhereParameterGroup("filtergroup");
		
		filtergroup.addClause(WhereClauseFactory.create(Comparator.GE, "fromDate", "nextDate", today, null, ChainOp.AND));
		filtergroup.addClause(WhereClauseFactory.create(Comparator.LE, "toDate", "nextDate", DateUtils.addDays(today, 30), null, ChainOp.AND));
		
		builder.addWhere(filtergroup);
		builder.addWhere(Comparator.EQ, "status", "status", ScheduleStatus.SCHEDULED);

		builder.addGroupBy("nextDate");
		List<UpcomingScheduledEventsRecord> results = persistenceService.findAll(builder);
		
		
        return Lists.newArrayList(new ChartData<Calendar>().add(results));
	}

	

}
