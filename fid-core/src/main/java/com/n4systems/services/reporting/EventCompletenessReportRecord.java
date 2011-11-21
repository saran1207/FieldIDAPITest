package com.n4systems.services.reporting;

import com.n4systems.util.chart.CalendarChartable;


@SuppressWarnings("serial")
public class EventCompletenessReportRecord extends CalendarChartable {	

	public EventCompletenessReportRecord(Long value, Integer year, Integer quarter, Integer month, Integer week, Integer day) {
		super(value, year, quarter, month, week, day);
	}
}
