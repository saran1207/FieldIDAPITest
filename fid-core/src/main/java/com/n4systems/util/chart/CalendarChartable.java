package com.n4systems.util.chart;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.n4systems.util.time.DateUtil;

@SuppressWarnings("serial")
public class CalendarChartable extends AbstractChartable<Calendar> {
	
	public CalendarChartable(Calendar x, Number y) {
		super(x,y);
	}
	
	public CalendarChartable(Long value, Integer year, Integer quarter, Integer month, Integer week, Integer day) {
		super(DateUtil.getCalendar(year, quarter, month, week, day), value);		
	}
	
    @Override
	public Long getLongX() {
    	// will getX().getTimeInMillis be better???  damn browser time zone fuckups.
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.clear();
		calendar.set(Calendar.YEAR, getX().get(Calendar.YEAR));
		calendar.set(Calendar.DAY_OF_YEAR, getX().get(Calendar.DAY_OF_YEAR));
        return calendar.getTimeInMillis();
    }

    
	public String getJavascriptX() {	    	
    	return ""+getLongX();
    }	
    
	@Override
	public String toString() { 
		return getX().getTime() + " : " + getY();
	}
	    
}
