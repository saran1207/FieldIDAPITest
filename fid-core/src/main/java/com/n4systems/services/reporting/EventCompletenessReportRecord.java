package com.n4systems.services.reporting;

import com.n4systems.util.chart.CalendarChartable;


@SuppressWarnings("serial")
public class EventCompletenessReportRecord extends CalendarChartable {	

	public EventCompletenessReportRecord(Long value, String granularity, Integer year, Integer month, Integer day) {
		super(value, granularity, year, month, day);
	}
}
