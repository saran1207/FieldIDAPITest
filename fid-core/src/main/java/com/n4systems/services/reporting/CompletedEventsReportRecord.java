package com.n4systems.services.reporting;

import com.n4systems.model.Status;
import com.n4systems.util.chart.CalendarChartable;

@SuppressWarnings("serial")
public class CompletedEventsReportRecord extends CalendarChartable {

	public CompletedEventsReportRecord(Long value, Status status, Integer year, Integer quarter, Integer month, Integer week, Integer day) {
		super(value, year, quarter, month, week, day);
	}

}
