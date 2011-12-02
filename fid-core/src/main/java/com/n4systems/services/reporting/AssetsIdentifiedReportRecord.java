package com.n4systems.services.reporting;

import com.n4systems.util.chart.CalendarChartable;


@SuppressWarnings("serial")
public class AssetsIdentifiedReportRecord extends CalendarChartable {	

	public AssetsIdentifiedReportRecord(Long value, String granularity, Integer year, Integer month, Integer day) {
		super(value, granularity, year, month, day);
	}
}
