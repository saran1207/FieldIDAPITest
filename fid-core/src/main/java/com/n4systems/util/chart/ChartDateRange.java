package com.n4systems.util.chart;

import java.util.Calendar;
import java.util.Date;

import com.n4systems.util.time.DateUtil;

public enum ChartDateRange {

	LAST_WEEK(Calendar.WEEK_OF_YEAR), LAST_MONTH(Calendar.MONTH), LAST_QUARTER(Calendar.MONTH, 3), LAST_YEAR(Calendar.YEAR),
	THIS_WEEK(Calendar.DAY_OF_WEEK), THIS_MONTH(Calendar.DAY_OF_MONTH), THIS_QUARTER(Calendar.MONTH,3), THIS_YEAR(Calendar.DAY_OF_YEAR), 
	FOREVER;
	
	private int calendarParam;
	private int value;


	ChartDateRange() {
	}

	ChartDateRange(int calendarParam, int delta) { 
		this.calendarParam = calendarParam;
		this.value = delta;
	}
	
	ChartDateRange(int calendarParam) { 
		this(calendarParam, 1);
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
	
	
}
