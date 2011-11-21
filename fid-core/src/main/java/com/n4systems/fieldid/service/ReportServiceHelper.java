package com.n4systems.fieldid.service;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.persistence.GroupByClause;

public class ReportServiceHelper {

	public List<GroupByClause> getGroupByClausesByGranularity(ChartGranularity granularity, String param) {
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

	public List<String> getSelectConstructorArgsByGranularity(String param, ChartGranularity granularity) {
		String day = granularity.ordinal()<=ChartGranularity.DAY.ordinal() ? "DAYOFYEAR("+param+")" : "-1";
		String week = granularity.ordinal()<=ChartGranularity.WEEK.ordinal() ? "WEEK("+param+")" : "-1";
		String month = granularity.ordinal()<=ChartGranularity.MONTH.ordinal() ? "MONTH("+param+")" : "-1";
		String quarter = granularity.ordinal()<=ChartGranularity.QUARTER.ordinal() ? "QUARTER("+param+")" : "-1";
		String year = "YEAR("+param+")";   // year required for all calendars.  coarsest unit.

		return Lists.newArrayList(year, quarter, month, week, day);
	}
	
}
