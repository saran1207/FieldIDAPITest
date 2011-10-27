package com.n4systems.services.reporting;

import java.util.Calendar;
import java.util.Date;

import com.n4systems.util.chart.CalendarChartable;
import com.n4systems.util.chart.Chartable;

public class UpcomingScheduledEventsRecord implements Chartable<Calendar> {
	
	private CalendarChartable chartable;
	
	public UpcomingScheduledEventsRecord(Date date, Long value) {
		this.chartable = new CalendarChartable(getCalendar(date), value);
	}

	public UpcomingScheduledEventsRecord(Calendar time, Long value) {
		this.chartable = new CalendarChartable(time, value);
	}
	
	private Calendar getCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
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
