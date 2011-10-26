package com.n4systems.util.chart;

import java.util.Calendar;
import java.util.GregorianCalendar;

@SuppressWarnings("serial")
public class CalendarChartable extends SimpleChartable<Calendar> {
	
	public CalendarChartable(Calendar x, Number y) {
		super(x,y);
	}
	
    @Override
	public Long getLongX() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.clear();
		calendar.set(Calendar.YEAR, getX().get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, getX().get(Calendar.MONTH));   
		calendar.set(Calendar.WEEK_OF_YEAR, getX().get(Calendar.WEEK_OF_YEAR));
		calendar.set(Calendar.DAY_OF_YEAR, getX().get(Calendar.DAY_OF_YEAR));
        return calendar.getTimeInMillis();
    }

    @Override
	public String getJavascriptX() {	    	
    	return ""+getLongX();
    }	
    
	@Override
	public String toString() { 
		return getX().getTime() + " : " + getY();
	}
	    
}
