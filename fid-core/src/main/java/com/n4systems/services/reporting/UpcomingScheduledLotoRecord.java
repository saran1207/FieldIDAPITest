package com.n4systems.services.reporting;

import com.n4systems.util.chart.DateChartable;
import org.joda.time.LocalDate;

import java.util.Date;

@SuppressWarnings("serial")
public class UpcomingScheduledLotoRecord extends DateChartable {

	public UpcomingScheduledLotoRecord(Date date, Long value) {
		super(date, value);
	}

	public UpcomingScheduledLotoRecord(LocalDate date, Long value) {
		super(date, value);
	}

	
}
