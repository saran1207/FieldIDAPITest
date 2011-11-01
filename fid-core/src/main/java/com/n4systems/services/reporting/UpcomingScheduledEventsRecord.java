package com.n4systems.services.reporting;

import java.util.Calendar;
import java.util.Date;

import com.n4systems.util.chart.CalendarChartable;

@SuppressWarnings("serial")
public class UpcomingScheduledEventsRecord extends CalendarChartable {
	
	public UpcomingScheduledEventsRecord(Date date, Long value) {
		super(date, value);
	}

	public UpcomingScheduledEventsRecord(Calendar time, Long value) {
		super(time, value);
	}
	
}
