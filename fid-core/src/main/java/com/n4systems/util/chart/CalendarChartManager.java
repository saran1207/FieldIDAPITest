package com.n4systems.util.chart;

import java.util.Calendar;

@SuppressWarnings("serial")
public class CalendarChartManager extends SimpleChartManager<Calendar> {

	private transient ChartGranularity granularity;
	
	public CalendarChartManager(ChartGranularity granularity) {
		this.granularity = granularity;
	}
	
	@Override
	public Long getMinX(ChartSeries<Calendar> series) {		
		Chartable<Calendar> lastEntry = series.getLastEntry();
		return lastEntry == null ? null : lastEntry.getLongX() - granularity.delta();
	}

	@Override
	public Long getPanMin(ChartSeries<Calendar> series) {
		Chartable<Calendar> firstEntry = series.getFirstEntry();
		return firstEntry == null ? null : firstEntry.getLongX();
	}

	@Override
	public Long getPanMax(ChartSeries<Calendar> series) {		
		Chartable<Calendar> lastEntry = series.getLastEntry();
		return lastEntry == null ? null : lastEntry.getLongX(); 
	}

	@Override
	public void updateOptions(ChartSeries<Calendar> chartSeries, FlotOptions<Calendar> options, int index) {	
		super.updateOptions(chartSeries, options, index);
		updateTimeFormat(chartSeries, options);		
	}

	protected void updateTimeFormat(ChartSeries<Calendar> chartSeries, FlotOptions<Calendar> options) {
		if (!"time".equals(options.xaxis.mode)) {
			return;
		}
		options.xaxis.minTickSize = null;
		options.xaxis.monthNames = FlotOptions.MONTH_NAMES;  // unless otherwise overridden.
		switch (granularity) { 
		case YEAR:
//			options.xaxis.tickSize = new String[]{"1","year"};  // FIXME DD : BUG! need to generate my own ticks in this case.. flot does't work when you don't have enough years.
			options.xaxis.timeformat = "%y";
			break;
		case QUARTER:
			options.xaxis.minTickSize = new String[]{"3","month"};
			options.xaxis.monthNames = FlotOptions.QUARTER_NAMES;
			options.xaxis.timeformat = "%b %y";  
			break;
		case MONTH:
			options.xaxis.minTickSize = new String[]{"1","month"};
			options.xaxis.timeformat = "%b %y";
			break;
		case WEEK: 
			options.xaxis.minTickSize = new String[]{"14","day"};
			options.xaxis.timeformat = "%b %y";
			break;
		}		
	}
	
}
