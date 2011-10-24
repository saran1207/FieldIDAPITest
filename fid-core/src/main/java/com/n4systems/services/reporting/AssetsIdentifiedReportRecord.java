package com.n4systems.services.reporting;

import java.util.Calendar;

import com.n4systems.util.chart.CalendarChartable;
import com.n4systems.util.chart.Chartable;


@SuppressWarnings("serial")
public class AssetsIdentifiedReportRecord implements Chartable<Calendar> {	

	private CalendarChartable chartable;
	private int year;
	private int quarter;
	
	public AssetsIdentifiedReportRecord(Integer year, Integer quarter, Long value) {
		this.year = year;		// store these just for debugging info...should be able to deduce from X. 
		this.quarter = quarter;
		chartable = new CalendarChartable(getCalendar(year, quarter), value);
	}
		
	public AssetsIdentifiedReportRecord(Calendar time, Long value) {
		chartable = new CalendarChartable(time, value);
	}

	private Calendar getCalendar(Integer year, Integer quarter) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, (quarter*3));
		return calendar;
	}	

	public String getJavascriptX() {
		return chartable.getJavascriptX();
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
	

}
