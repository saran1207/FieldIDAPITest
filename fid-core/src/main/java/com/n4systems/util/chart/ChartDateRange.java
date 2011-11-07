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
		
	public Calendar getFromCalendar() {
		Calendar calendar = Calendar.getInstance();
		switch (this) {
		case FOREVER: 			
			return DateUtil.getEarliestFieldIdCalendar();
		case LAST_MONTH:
		case LAST_QUARTER:
		case LAST_YEAR:
		case LAST_WEEK:
			calendar.add(calendarParam, -value);
			return calendar;
		case THIS_WEEK:
		case THIS_MONTH:
		case THIS_YEAR:
			calendar.set(calendarParam, value);
			return calendar;
		case THIS_QUARTER:
			calendar.set(Calendar.MONTH,1 + (calendar.get(Calendar.MONTH)/12 * 3));
			return calendar;
		default : 
			// just return todays' calendar
			return calendar;			
		}
	}	
	
}
