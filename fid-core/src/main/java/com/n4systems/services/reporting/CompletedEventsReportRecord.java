package com.n4systems.services.reporting;

import com.n4systems.util.chart.DateChartable;

public class CompletedEventsReportRecord extends DateChartable {

	public CompletedEventsReportRecord(Long value, String granularity, Integer year, Integer month, Integer day) {
		super(value, granularity, year, month, day);
	}

}
