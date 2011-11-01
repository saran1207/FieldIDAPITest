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
		return series.getLastEntry().getLongX() - granularity.delta();
	}

	@Override
	public Long getPanMin(ChartSeries<Calendar> series) {
		return series.getFirstEntry().getLongX();
	}

	@Override
	public Long getPanMax(ChartSeries<Calendar> series) {
		return series.getLastEntry().getLongX(); 
	}

	@Override
	public void updateOptions(ChartSeries<Calendar> chartSeries, FlotOptions<Calendar> options, int index) {	
		super.updateOptions(chartSeries, options, index);
		setTimeFormatFor(chartSeries, options);		
	}

	protected void setTimeFormatFor(ChartSeries<Calendar> chartSeries, FlotOptions<Calendar> options) {
		if (!"time".equals(options.xaxis.mode)) {
			return;
		}
		options.xaxis.minTickSize = null;
		options.xaxis.monthNames = FlotOptions.MONTH_NAMES;  // unless otherwise overridden.
		String format = "";
		switch (granularity) { 
		case YEAR:
		case ALL:
//			options.xaxis.tickSize = new String[]{"1","year"};  // FIXME DD : BUG! need to generate my own ticks in this case.. flot does't work when you don't have enough years.
			format = "%y";
			break;
		case QUARTER:
			options.xaxis.minTickSize = new String[]{"3","month"};
			options.xaxis.monthNames = FlotOptions.QUARTER_NAMES;
			format = "%b %y";  
			break;
		case MONTH:
			options.xaxis.minTickSize = new String[]{"1","month"};
			format = "%b %y";
			break;
		case WEEK: 
			options.xaxis.minTickSize = new String[]{"14","day"};
			format = "%b %y";
			break;
		}
		options.xaxis.timeformat = format;
	}
	
}
