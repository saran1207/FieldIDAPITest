package com.n4systems.util.chart;

import org.joda.time.LocalDate;

@SuppressWarnings("serial")
public class DateChartManager extends SimpleChartManager<LocalDate> {

	private transient ChartGranularity granularity;
	private transient ChartDateRange dateRange;
	
	public DateChartManager(ChartGranularity granularity, ChartDateRange range) {
		this.granularity = granularity;
		this.dateRange = range;
	}
	
	@Override
	public Long getMinX(ChartSeries<LocalDate> series) {
		if (series.isEmpty() || dateRange.isDaysFromNowRange()) { 
			return null;
		}
		Chartable<LocalDate> lastEntry = series.getLastEntry();
		long minX = lastEntry.getX().minus(granularity.preferredRange()).toDate().getTime();
		long firstX = series.getFirstEntry().getX().toDate().getTime();
		return Math.max(minX,firstX);
	}

	@Override
	public Long getPanMin(ChartSeries<LocalDate> series) {
		if (series.isEmpty()) { 
			return dateRange.getFrom().toDate().getTime();
		}
		return series.getFirstEntry().getLongX();
	}

	@Override
	public Long getPanMax(ChartSeries<LocalDate> series) {
		if (series.isEmpty()) { 
			return null;
		}		
		return series.getLastEntry().getLongX();
	}

	@Override
	public void normalize(ChartSeries<LocalDate> series) {
		LocalDate endDate = ChartDateRange.FOREVER.equals(dateRange) ? series.getLastX() : granularity.roundUp(dateRange.getTo());
		LocalDate date = granularity.roundDown(dateRange.getFrom());
		
		while (endDate!=null && date.isBefore(endDate)) { 
			Chartable<LocalDate> value = series.get(date);
			if (value==null) {  // add some padding where no values exist. 
				series.add(new DateChartable(date,0L));
			}
			date = granularity.next(date);
		}		
	}
	
	protected DateChartable pad(LocalDate c) {		
		LocalDate date = new LocalDate(c);
		return new DateChartable(date,0);
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
