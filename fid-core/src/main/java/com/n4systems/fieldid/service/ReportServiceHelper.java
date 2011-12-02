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
				// note : use proper mode for dates to remain consistent with JODA.  weeks start on monday.  
				//  see http://dev.mysql.com/doc/refman/5.5/en/date-and-time-functions.html#function_week
				result.add(new GroupByClause("WEEK("+param+",1)", true));
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

	public List<String> getSelectConstructorArgsForGranularity(String param, ChartGranularity granularity) {
		return Lists.newArrayList(	"'"+granularity.toString()+"'", 
									"YEAR("+param+")", 
									"MONTH("+param+")",
									"DAYOFMONTH("+param+")" );
	}
	
}
