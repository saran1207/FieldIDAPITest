package com.n4systems.util.chart;

import java.util.Calendar;
import java.util.Date;

import com.n4systems.util.time.DateUtil;

public enum ChartDateRange {

	LAST_WEEK("Last Week", Calendar.WEEK_OF_YEAR), 
	LAST_MONTH("Last Month", Calendar.MONTH), 
	LAST_QUARTER("Last Quarter", Calendar.MONTH, 3), 
	LAST_YEAR("Last Year", Calendar.YEAR),
	THIS_WEEK("This Week", Calendar.DAY_OF_WEEK), 
	THIS_MONTH("This Month", Calendar.DAY_OF_MONTH), 
	THIS_QUARTER("This Quarter", Calendar.MONTH,3), 
	THIS_YEAR("This Year", Calendar.DAY_OF_YEAR), 
	FOREVER("All Time");
	
	private int calendarParam;
	private int value = 1;
	private String displayName;


	ChartDateRange(String displayName) {
		this.displayName = displayName;		
	}

	ChartDateRange(String displayName, int calendarParam) { 
		this(displayName, calendarParam, 1);
	}		
	
	ChartDateRange(String displayName, int calendarParam, int delta) {
		this(displayName);
		this.calendarParam = calendarParam;
		this.value = delta;
	}
	
	public Date getFromDate() {
		return getFromCalendar().getTime();
	}
		
	public Calendar getToCalendar() { 
		// queries should use <,  NOT <= when using these dates!!!
		Calendar calendar = getTimelessIntance();
		switch (this) {
		case FOREVER: 			
			return DateUtil.getLatestCalendar();
		case LAST_YEAR:
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			break;			
		case LAST_QUARTER:
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)/3 * 3);  // round to nearest quarter...
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case LAST_MONTH:
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;			
		case LAST_WEEK:
			calendar.set(Calendar.DAY_OF_WEEK, 1);
			break;
		case THIS_WEEK:
			calendar.set(Calendar.DAY_OF_WEEK, 8);			
			break;
		case THIS_MONTH:
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case THIS_QUARTER:
			calendar.set(Calendar.MONTH, 3 + (calendar.get(Calendar.MONTH)/3)*3);  // round to nearest quarter...
			calendar.set(Calendar.DAY_OF_MONTH,1);
			break;
		case THIS_YEAR:
			calendar.add(Calendar.YEAR, 1);
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			break;
		}
		return calendar;
	}

	public Calendar getFromCalendar() {
		Calendar calendar = getTimelessIntance();
		switch (this) {
		case FOREVER: 			
			return DateUtil.getEarliestFieldIdCalendar();
		case LAST_YEAR:
			calendar.add(calendarParam, -value);
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			break;
		case LAST_QUARTER:
			//Q3/2011= Q2/2011
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)/3 * 3);  // round to nearest quarter...
			calendar.add(calendarParam, -value);										// then go back one.
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case LAST_MONTH:
			calendar.add(calendarParam, -value);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case LAST_WEEK:
			calendar.add(calendarParam, -value);
			calendar.set(Calendar.DAY_OF_WEEK, 1);
			break;
		case THIS_WEEK:
			calendar.set(Calendar.DAY_OF_WEEK,1);
			break;
		case THIS_MONTH:
			calendar.set(Calendar.DAY_OF_MONTH,1);
			break;
		case THIS_QUARTER:
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)/3 * 3);  
			calendar.set(Calendar.DAY_OF_MONTH,1);
			break;
		case THIS_YEAR:
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			break;
		}
		return calendar;
	}

	// TODO DD : put in dateUtils.
	private Calendar getTimelessIntance() {		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);  
		c.set(Calendar.MINUTE, 0);  
		c.set(Calendar.SECOND, 0);  
		c.set(Calendar.MILLISECOND, 0);  		
		return c;
	}
	
	public Date getToDate() { 
		return getToCalendar().getTime();
	}
	
	public String getDisplayName() { 
		return displayName;
	}
	
}
