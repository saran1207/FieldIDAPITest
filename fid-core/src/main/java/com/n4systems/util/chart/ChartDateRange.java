package com.n4systems.util.chart;

import java.util.Calendar;
import java.util.Date;

import com.n4systems.util.time.DateUtil;

public enum ChartDateRange {

	LAST_WEEK("Last Week" ), 
	LAST_MONTH("Last Month"), 
	LAST_QUARTER("Last Quarter"), 
	LAST_YEAR("Last Year"),
	THIS_WEEK("This Week"), 
	THIS_MONTH("This Month"), 
	THIS_QUARTER("This Quarter"), 
	THIS_YEAR("This Year"), 
	FOREVER("All Time");
	
	private String displayName;


	ChartDateRange(String displayName) {
		this.displayName = displayName;		
	}
	
	public Date getFromDate() {
		return getFromCalendar().getTime();
	}
		
	public Calendar getToCalendar() { 
		// exclusive date :  should use <,  NOT <= when comparing against returned value!!!   
		Calendar calendar = DateUtil.getTimelessIntance();
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
			calendar.set(Calendar.DAY_OF_WEEK, 1);
			calendar.add(Calendar.WEEK_OF_YEAR, 1);			
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
		Calendar calendar = DateUtil.getTimelessIntance();
		switch (this) {
		case FOREVER: 			
			return DateUtil.getEarliestFieldIdCalendar();
		case LAST_YEAR:
			calendar.add(Calendar.YEAR, -1);
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			break;
		case LAST_QUARTER:
			//Q3/2011= Q2/2011
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)/3 * 3);  // round to nearest quarter...
			calendar.add(Calendar.MONTH, -3);										// then go back one.
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case LAST_MONTH:
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case LAST_WEEK:
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
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
	
	public Date getToDate() { 
		return getToCalendar().getTime();
	}
	
	public String getDisplayName() { 
		return displayName;
	}
	
}
