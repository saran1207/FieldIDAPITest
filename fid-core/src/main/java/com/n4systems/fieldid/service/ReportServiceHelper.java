package com.n4systems.fieldid.service;

import com.google.common.collect.Lists;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.persistence.GroupByClause;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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

	public List<String> getSelectConstructorArgsForGranularity(String param, ChartGranularity granularity, TimeZone timeZone) {
        // UGGH : hack.   this is a small, focused approach to fixing yet another time zone bug.
        // this should be reverted when a complete, system wide approach to handling time zones is implemented.
        // see WEB-2836
        String p = param;
        if (timeZone!=null) {
            // CAVEAT : this is quite likely DB specific and semi-brittle.
            String tz1 = "+00:00";
            int offset = timeZone.getRawOffset();
            String tz2  = String.format("%s%02d:%02d", offset >= 0 ? "+" : "-", Math.abs(offset) / 3600000, (Math.abs(offset) / 60000) % 60);
            p = String.format("CONVERT_TZ(%s,'%s','%s')", p, tz1, tz2);
        }
		return Lists.newArrayList(	"'"+granularity.toString()+"'",
									"YEAR("+p+")",
									"MONTH("+p+")",
									"DAYOFMONTH("+p+")");
	}

}
