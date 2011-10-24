package com.n4systems.util.chart;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

@SuppressWarnings("serial")
public class CalendarChartable extends SimpleChartable<Calendar> {
	
	public CalendarChartable(Calendar x, Number y) {
		super(x,y);
	}

    @Override
	public String getJavascriptX() {	    	
    	// FIXME DD : dashboard.js should detect locale and set timezone accordingly.
        GregorianCalendar calendar = new GregorianCalendar(new SimpleTimeZone(-3600000*4,"GMT-4"));
        calendar.clear();
		calendar.set(Calendar.YEAR, getX().get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, getX().get(Calendar.MONTH));        
        return new Long(calendar.getTimeInMillis()).toString();
    }	
    
	@Override
	public String toString() { 
		return getX().getTime() + " : " + getY();
	}
	    

	
	
}
