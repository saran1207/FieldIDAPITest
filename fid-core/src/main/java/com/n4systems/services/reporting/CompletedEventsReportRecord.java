package com.n4systems.services.reporting;

import java.util.Calendar;

import com.n4systems.model.Status;
import com.n4systems.util.chart.CalendarChartable;
import com.n4systems.util.chart.Chartable;


@SuppressWarnings("serial")
public class CompletedEventsReportRecord implements Chartable<Calendar> {	

	private CalendarChartable chartable;
	private int year;
	private int quarter;
	private Integer week;
	private Integer day;   // day of year = 1..365(366)
	private Status status;

	public CompletedEventsReportRecord(Long value, Status status, Integer year, Integer quarter, Integer month, Integer week, Integer day) {
		this.status = status;
		this.year = year;		// store these just for debugging info...should be able to deduce from X. 
		this.quarter = quarter;
		this.week = week;
		this.day = day;
		chartable = new CalendarChartable(getCalendar(year, quarter, week, day), value);
	}		

	// TODO DD : refactor this out to DateUtils.getCal(y,q,w,d)
	private Calendar getCalendar(Integer year, Integer quarter, Integer week, Integer day) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, (quarter*3));		
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_YEAR, day);		
		return calendar;
	}	

	@Override
	public Calendar getX() {
		return chartable.getX();
	}

	@Override
	public Number getY() {
		return chartable.getY();
	}
	
	@Override
	public String toString() { 
		return chartable.toString();
	}

	@Override
	public String toJavascriptString() {
		return chartable.toJavascriptString();
	}
	
	@Override 
	public Long getLongX() {
		return chartable.getLongX();
	}

}
