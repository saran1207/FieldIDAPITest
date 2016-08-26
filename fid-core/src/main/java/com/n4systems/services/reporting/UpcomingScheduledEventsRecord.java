package com.n4systems.services.reporting;

import com.n4systems.util.chart.DateChartable;
import org.joda.time.LocalDate;

import java.util.Date;

@SuppressWarnings("serial")
public class UpcomingScheduledEventsRecord extends DateChartable {
	
	public UpcomingScheduledEventsRecord(Date date, Long value) {
		super(date, value);
	}

	public UpcomingScheduledEventsRecord(LocalDate date, Long value) {
		super(date, value);
	}

	
}
