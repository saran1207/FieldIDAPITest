package com.n4systems.util.chart;

import java.util.Calendar;

@SuppressWarnings("serial")
public class CalendarChartManager extends SimpleChartManager<Calendar> {

	private transient ChartGranularity granularity;
	
	public CalendarChartManager(ChartGranularity granularity) {
		this.granularity = granularity;
	}
	
	@Override
	public Long getMinX(ChartSeries<Calendar> data) {
		return data.getLastEntry().getLongX() - granularity.delta();
	}

	@Override
	public Long getPanMin(ChartSeries<Calendar> data) {
		return data.getFirstEntry().getLongX();
	}

	@Override
	public Long getPanMax(ChartSeries<Calendar> data) {
		return data.getLastEntry().getLongX(); 
	}

}
