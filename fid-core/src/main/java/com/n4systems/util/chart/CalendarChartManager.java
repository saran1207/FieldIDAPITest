package com.n4systems.util.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.LocalDate;

@SuppressWarnings("serial")
public class CalendarChartManager extends SimpleChartManager<LocalDate> {

	private transient ChartGranularity granularity;
	private transient ChartDateRange range;
	
	public CalendarChartManager(ChartGranularity granularity, ChartDateRange range) {
		this.granularity = granularity;
		this.range = range;
	}
	
	@Override
	public Long getMinX(ChartSeries<LocalDate> series) {
		if (series.isEmpty()) { 
			return range.getFrom().toDate().getTime();
		}
		Chartable<LocalDate> lastEntry = series.getLastEntry();
		long minX = lastEntry.getX().minus(granularity.preferredRange()).toDate().getTime();
		long firstX = series.getFirstEntry().getX().toDate().getTime();
		return (minX < firstX) ? firstX : minX;
	}

	@Override
	public Long getPanMin(ChartSeries<LocalDate> series) {
		if (series.isEmpty()) { 
			return range.getFrom().toDate().getTime();
		}
		return series.getFirstEntry().getLongX();
	}

	@Override
	public Long getPanMax(ChartSeries<LocalDate> series) {
		if (series.isEmpty()) { 
			return range.getTo().toDate().getTime();
		}		
		return series.getLastEntry().getLongX();
	}

	@Override
	public void normalize(ChartSeries<LocalDate> series) {
		if (series.isEmpty()) {
			series.add(pad(range.getFrom()));
			return;
		}
		List<CalendarChartable> padding = new ArrayList<CalendarChartable>();		
		LocalDate expected = range.getFrom();
		LocalDate actual = null;		
		for (Iterator<Entry<LocalDate, Chartable<LocalDate>>> i = series.getEntrySet().iterator(); i.hasNext();) {
			Entry<LocalDate, Chartable<LocalDate>> entry = i.next();
			actual = entry.getValue().getX();
			while (granularity.compare(expected,actual)<0) {
				padding.add(pad(expected));
				expected=granularity.next(expected);
			}
			expected=granularity.next(actual);			
		}
		if (!ChartDateRange.FOREVER.equals(range)) { 
			while (granularity.compare(expected, range.getTo())<0) {
				padding.add(pad(expected));
				expected=granularity.next(expected);
			}
		}
		series.add(padding);
	}
	
	protected CalendarChartable pad(LocalDate c) {		
		LocalDate date = new LocalDate(c);
		return new CalendarChartable(date,0);
	}

	@Override
	public void updateOptions(ChartSeries<LocalDate> chartSeries, FlotOptions<LocalDate> options, int index) {	
		super.updateOptions(chartSeries, options, index);
		updateTimeFormat(chartSeries, options);
		options.tooltipFormat = getTooltipFormat(granularity);				
	}

	protected String getTooltipFormat(ChartGranularity granularity) {
		switch (granularity) { 
		case DAY:
			return FlotOptions.TOOLTIP_WITH_DAY;
		case YEAR:
			return FlotOptions.TOOLTIP_YEAR;
		case MONTH:
		case QUARTER:
			return FlotOptions.TOOLTIP_WITHOUT_DAY;
		case WEEK:
			return FlotOptions.TOOLTIP_WEEK;
		default: 
			return FlotOptions.TOOLTIP_WITH_DAY;				
		}		
	}

	protected void updateTimeFormat(ChartSeries<LocalDate> chartSeries, FlotOptions<LocalDate> options) {
		if (!"time".equals(options.xaxis.mode)) {
			return;
		}
		options.xaxis.minTickSize = null;
		options.xaxis.monthNames = FlotOptions.MONTH_NAMES;
		switch (granularity) { 
		case YEAR:
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
