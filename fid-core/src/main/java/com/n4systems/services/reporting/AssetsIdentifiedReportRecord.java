package com.n4systems.services.reporting;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

import com.n4systems.util.chart.Chartable;


@SuppressWarnings("serial")
public class AssetsIdentifiedReportRecord implements Chartable<Calendar> {	

	private int year;
	private int quarter;
	private Calendar x;
	private long value;
	
	public AssetsIdentifiedReportRecord(Integer year, Integer quarter, Long value) {
		this.x = getCalendar(year, quarter);
		this.value = value;
		this.year = year;
		this.quarter = quarter;
	}
		
	public AssetsIdentifiedReportRecord(Calendar time, Long value) {
		this.x = time;
		this.value = value;
	}

	private Calendar getCalendar(Integer year, Integer quarter) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, (quarter*3));
		return calendar;
	}	
	
	@Override
	public Calendar getX() {
		return x;
    }
			
	public Integer getYear() {
		return year;
	}
	
	public void setYear(Integer year) {
		this.year = year;
	}
	
	public Long getValue() {
		return value;
	}
	
	public void setValue(Long v) {
		this.value = v;
	}

	@Override
	public Long getY() {
		return value; 
	}

	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	public Integer getQuarter() {
		return quarter;
	}
	
	// TODO DD : put this in chart helper class. 
    public long getJavascriptX() {	    	
    	// arggh...javascript sucks at times.  we are forcing all data to toronto time.   (this may not be good
    	//   for users in other time zones but i'm not sure how to handle this).
    	
    	// FIXME DD : dashboard.js should detect locale and set timezone accordingly.
        GregorianCalendar calendar = new GregorianCalendar(new SimpleTimeZone(-3600000*4,"GMT-4"));
        calendar.clear();
		calendar.set(Calendar.YEAR, x.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, x.get(Calendar.MONTH));        
        return calendar.getTimeInMillis();
    }	

	@Override
	public String toJavascriptString() {
		return "[" + getJavascriptX() + "," + getY() + "]";
	}
	
	@Override
	public String toString() { 
		return getX().getTime() + " : " + getY();
	}
	

}
