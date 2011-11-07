package com.n4systems.util.chart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class CalendarChartManager extends SimpleChartManager<Calendar> {

	private transient ChartGranularity granularity;
	private transient ChartDateRange range;
	
	public CalendarChartManager(ChartGranularity granularity, ChartDateRange range) {
		this.granularity = granularity;
		this.range = range;
	}
	
	@Override
	public Long getMinX(ChartSeries<Calendar> series) {
		Chartable<Calendar> lastEntry = series.getLastEntry();
		// if (-delta>firstEntry....)
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
	public void normalize(ChartSeries<Calendar> series) {
		List<CalendarChartable> padding = new ArrayList<CalendarChartable>();
		Calendar expected = range.getFromCalendar();
		for (Iterator<Entry<Calendar, Chartable<Calendar>>> i = series.getEntrySet().iterator(); i.hasNext();) {
			Entry<Calendar, Chartable<Calendar>> entry = i.next();
			Calendar actual = entry.getValue().getX();
			while (granularity.compare(expected,actual)<0) {
				padding.add(pad(expected));
				expected=granularity.next(expected);
			}
			expected=granularity.next(actual);			
		}
		series.add(padding);
	}
	
	protected CalendarChartable pad(Calendar c) {		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(c.getTimeInMillis());
		return new CalendarChartable(calendar,0);
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
