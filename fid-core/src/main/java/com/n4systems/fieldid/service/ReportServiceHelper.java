package com.n4systems.fieldid.service;

import com.google.common.collect.Lists;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.persistence.GroupByClause;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ReportServiceHelper {

    public List<GroupByClause> getGroupByClausesByGranularity(ChartGranularity granularity, String param) {
        return getGroupByClausesByGranularity(granularity, param, null, null);
    }

    public List<GroupByClause> getGroupByClausesByGranularity(ChartGranularity granularity, String param, TimeZone timeZone, Date sampleDate) {
        ArrayList<GroupByClause> result = Lists.newArrayList();
		
		if (granularity==null) {
			return result; 
		}
        String date = getTimeZoneAdjusted(param, timeZone, sampleDate);

		result.add(	new GroupByClause("YEAR("+date+")", true) );

		switch (granularity) { 
			case DAY:			
				result.add(new GroupByClause("DAYOFYEAR("+date+")", true));
				break;
			case WEEK:
				// note : use proper mode for dates to remain consistent with JODA.  weeks start on monday.  
				//  see http://dev.mysql.com/doc/refman/5.5/en/date-and-time-functions.html#function_week
				result.add(new GroupByClause("WEEK("+date+",1)", true));
				break;
			case MONTH:
				result.add(new GroupByClause("MONTH("+date+")", true));
				break;
			case QUARTER:
				result.add(new GroupByClause("QUARTER("+date+")", true));
				break;
			case YEAR:
			default:
				// do nothing...default of group by year is good.
		}
		return result;
	}

    public List<String> getSelectConstructorArgsForGranularity(String param, ChartGranularity granularity) {
        return getSelectConstructorArgsForGranularityTimezoneAdjusted(param, granularity, null, null);
    }

    public List<String> getSelectConstructorArgsForGranularityTimezoneAdjusted(String param, ChartGranularity granularity, TimeZone timeZone, Date sampleDate) {
        // UGGH : hack.   this is a small, focused approach to fixing yet another time zone bug.
        // this should be reverted when a complete, system wide approach to handling time zones is implemented.
        // see WEB-2836

        // this is getting uglier...we have to make an educated guess at Daylight Savings Time via sampleDate.
        // the ultimate solution would be to have each individual queried value have its time zone converted but i don't know how
        // to do that in a query.
        String p = getTimeZoneAdjusted(param, timeZone, sampleDate);
        return getSelectConstructorArgsForGranularity(granularity, p);
    }

    public List<String> getSelectConstructorArgsForGranularity(ChartGranularity granularity, String paramName) {
        return Lists.newArrayList("'" + granularity.toString() + "'",
                "YEAR(" + paramName + ")",
                "MONTH(" + paramName + ")",
                "DAYOFMONTH(" + paramName + ")");
    }

    private String getTimeZoneAdjusted(String p, TimeZone timeZone, Date sampleDate) {
        // note that we can't simply use timeZone.getRawOffset 'cause that doesn't take into effect
        // the daylight savings time. note that this isn't perfect though...what we really want is to take
        // into account the time zone of each entity being queried.
        if (timeZone!=null && sampleDate!=null) {
            // CAVEAT : this is quite likely DB specific and semi-brittle.  using EXTRACT might be more performant (but still MYSQL specific)
            String tz1 = "+00:00";
            int offset = timeZone.getOffset(sampleDate.getTime());
            String tz2  = String.format("%s%02d:%02d", offset >= 0 ? "+" : "-", Math.abs(offset) / 3600000, (Math.abs(offset) / 60000) % 60);
            p = String.format("CONVERT_TZ(%s,'%s','%s')", p, tz1, tz2);
        }
        return p;
    }

}
