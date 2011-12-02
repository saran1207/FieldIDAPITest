package com.n4systems.services.reporting;

import java.util.Date;

import org.joda.time.LocalDate;

import com.n4systems.util.chart.CalendarChartable;

@SuppressWarnings("serial")
public class UpcomingScheduledEventsRecord extends CalendarChartable {
	
	public UpcomingScheduledEventsRecord(Date date, Long value) {
		super(date, value);
	}

	public UpcomingScheduledEventsRecord(LocalDate date, Long value) {
		super(date, value);
	}

	
}
