package com.n4systems.util.chart;

import java.util.Calendar;
import java.util.Date;

import com.n4systems.util.time.DateUtil;

/**
 *  usage note : if any date values are < 0, they will be ignored.
 *  eg.   year:2011, month/quarter/week/day = -1.   		result = Jan 1,2011.  12:00am. 
 *        year:2011, quarter: 2,  month/week/day = -1.   	result = Apr 1,2011.  12:00am.  (start of 2nd quarter)
 *        year:2011, day=22,  quarter/month/week/day = -1. 	result = Jan 22,2011. 12:00am.
 * currently doesn't support hours, minutes, seconds etc...         
 */
@SuppressWarnings("serial")
public class CalendarChartable extends AbstractChartable<Calendar> {
	
	public CalendarChartable(Calendar x, Number y) {
		super(x,y);
	}
	
	public CalendarChartable(Long value, Integer year, Integer quarter, Integer month, Integer week, Integer day) {
		this(DateUtil.getCalendar(year, quarter, month, week, day), value);		
	}
	
    public CalendarChartable(Date date, Long value) {
    	this(DateUtil.getCalendar(date), value);
	}

	@Override
	public Long getLongX() {
		return getX().getTimeInMillis();
    }

    
	public String getJavascriptX() {	    	
    	return ""+getLongX();
    }	
    
	@Override
	public String toString() { 
		return getX().getTime() + " : " + getY();
	}
	    
}
