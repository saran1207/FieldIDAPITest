package com.n4systems.services.reporting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDataGranularity;
import com.n4systems.util.chart.SimpleChartable;
import com.n4systems.util.persistence.GroupByClause;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;

// CACHEABLE!!!  this is used for getting old data.

public class ReportingService extends FieldIdPersistenceService {
	
	// TODO DD : unit tests...
	
	@Autowired
	private PersistenceService persistenceService;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public List<ChartData<Calendar>> getAssetsIdentified(ChartDataGranularity granularity, BaseOrg org) {
		QueryBuilder<AssetsIdentifiedReportRecord> builder = getBuilderForGranularity(granularity, org);
		
		List<AssetsIdentifiedReportRecord> results = persistenceService.findAll(builder);
				
		ChartData<Calendar> chartData = new ChartData<Calendar>();
		
		for (int i= 0 ; i < 100; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(i);			
			chartData.add(new SimpleChartable<Calendar>(calendar,new Long(i)) {
				@Override protected String getJavascriptX() {
					return ""+getX().getTimeInMillis();
				}
			});
		}
		        
        return Lists.newArrayList(new ChartData<Calendar>().add(results));
    }

	private QueryBuilder<AssetsIdentifiedReportRecord> getBuilderForGranularity(ChartDataGranularity period, BaseOrg org) {
		QueryBuilder<AssetsIdentifiedReportRecord> builder = new QueryBuilder<AssetsIdentifiedReportRecord>(Asset.class, securityContext.getUserSecurityFilter());
		
		builder.setSelectArgument(new NewObjectSelect(AssetsIdentifiedReportRecord.class, "YEAR(identified)", "QUARTER(identified)", "WEEK(identified)", "DAYOFYEAR(identified)", "COUNT(*)"));
//		builder.addWhere(Comparator.GE, "identified", "identified", getFromDate(period));
		builder.addGroupByClauses(getGroupByClauses(period));
		
		return builder;		
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

	private Date getFromDate(ChartDataGranularity period) {
		if (period==null) { 
			return new Date(0);
		}

		Calendar today = Calendar.getInstance();
		int day = today.get(Calendar.DAY_OF_YEAR);
		int year = today.get(Calendar.YEAR);
		
		// try to get around 30 data points per plot. 
		Calendar from = today;
		switch (period) { 
			case DAY:
				from.add(Calendar.MONTH, -1);
				break;
			case WEEK:
				from.add(Calendar.YEAR, -1);
				break;
			case MONTH: 
				from.add(Calendar.YEAR, -2);
				break;
			case QUARTER:
				from.add(Calendar.YEAR, -4);
				break;
			case YEAR:
			case ALL:
				from.clear();
				break;
		}
		return from.getTime();
	}

}
