package com.n4systems.util.chart;

import java.util.Calendar;

@SuppressWarnings("serial")
public class CalendarChartManager extends SimpleChartManager<Calendar> {

	private transient ChartDataGranularity granularity;
	
	public CalendarChartManager(ChartDataGranularity granularity) {
		this.granularity = granularity;
	}
	
	@Override
	public Long getMinX(ChartData<Calendar> data) {
		return data.getLastEntry().getLongX() - granularity.delta();
	}

	@Override
	public Long getPanMin(ChartData<Calendar> data) {
		return data.getFirstEntry().getLongX();
	}

	@Override
	public Long getPanMax(ChartData<Calendar> data) {
		return data.getLastEntry().getLongX(); 
	}

}
