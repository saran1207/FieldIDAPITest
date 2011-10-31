package com.n4systems.services.reporting;

import java.util.Calendar;

import com.n4systems.util.chart.CalendarChartable;
import com.n4systems.util.chart.Chartable;


@SuppressWarnings("serial")
public class AssetsIdentifiedReportRecord implements Chartable<Calendar> {	

	private CalendarChartable chartable;
	private int year;
	private int quarter;
	private Integer week;
	private Integer day;   // day of year = 1..365(366)
	private Integer month;


	public AssetsIdentifiedReportRecord(Long value, Integer year, Integer quarter, Integer month, Integer week, Integer day) {
		this.year = year;		// store these just for debugging info...should be able to deduce from X. 
		this.quarter = quarter;
		this.month = month;
		this.week = week;
		this.day = day;
		chartable = new CalendarChartable(value, year, quarter, month, week, day);
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
