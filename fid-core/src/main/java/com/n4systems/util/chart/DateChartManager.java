package com.n4systems.util.chart;

import com.n4systems.model.utils.DateRange;
import com.n4systems.util.math.MathUtil;
import com.n4systems.util.time.DateUtil;
import org.joda.time.LocalDate;

public class DateChartManager extends SimpleChartManager<LocalDate> {
	
	private transient ChartGranularity granularity;
	private transient DateRange dateRange;
	
	public DateChartManager(ChartGranularity granularity, DateRange dateRange) {
		this.granularity = granularity;
		this.dateRange = dateRange;
	}

    @Override
    public Long getMin(ChartSeries<LocalDate> series) {
        if (series.isEmpty() || dateRange.getRangeType().isDaysFromNowRangeType()) {
            return null;
        }
        Chartable<LocalDate> lastEntry = series.getLastEntry();
        long minX = lastEntry.getX().minus(granularity.preferredRange()).toDate().getTime();
        long firstX = series.getFirstEntry().getX().toDate().getTime();
        // don't scroll if you have < X points.
        return (series.size()<PAN_THRESHOLD) ? firstX : Math.max(minX,firstX);
    }

    @Override
	public Long getPanMin(ChartSeries<LocalDate> series) {
		if (series.isEmpty()) { 
			LocalDate from = dateRange.getFrom();
			return from != null ? from.toDate().getTime() :
				DateUtil.getEarliestFieldIdDate().toDate().getTime();
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
	public ChartSeries<LocalDate> normalize(ChartSeries<LocalDate> series, LocalDate min, LocalDate max) {

        LocalDate earliest = dateRange.getFrom();
        earliest = earliest==null ? DateUtil.getEarliestFieldIdDate() : earliest;

        LocalDate date = MathUtil.nullSafeMin(granularity.roundDown(min), granularity.roundDown(earliest));
        LocalDate endDate = MathUtil.nullSafeMax(max, series.getLastX());
        if (!RangeType.FOREVER.equals(dateRange.getRangeType())) {
            // skip FOREVER case because it's end date isn't practical.
            LocalDate calculatedMax = granularity.roundUp(dateRange.getTo());
            // if we are given a date, we consider it inclusive. if it's less than what date range specifies we use that exclusive date.
            endDate = (max != null && max.compareTo(calculatedMax) > 0) ? max.plusDays(1) : calculatedMax;
        }

		while (endDate!=null && date.isBefore(endDate)) {
			Chartable<LocalDate> value = series.get(date);
			if (value==null) {  // add some padding where no values exist. 
				series.add(new DateChartable(date,0L));
			}
			date = granularity.next(date);
		}
		return series;
	}

	@Override
	public void updateOptions(ChartSeries<LocalDate> chartSeries, FlotOptions<LocalDate> options, int index) {	
		super.updateOptions(chartSeries, options, index);
		updateTimeFormat(chartSeries, options);
		if (dateRange.getRangeType().isDaily()) { 
			updateForDaily(chartSeries, options);
		}
		options.tooltipFormat = getTooltipFormat(granularity);		
	}

    @Override
    public void sortSeries(ChartData<LocalDate> chartSeries) {
        ;
    }

    private void updateForDaily(ChartSeries<LocalDate> chartSeries, FlotOptions<LocalDate> options) {
		// note that for daily graphs we do the ole switcherooo.   turn a line graph into a single bar graph. 
		// this means resetting
		options.series = null;
		options.bars.barWidth = 0.5;
		options.bars.horizontal = false;
		options.bars.clickable = true;
		options.bars.show = true;
		options.bars.lineWidth = 0;
		options.yaxis.tickLength = 0;
		options.grid.show = true;
		options.grid.hoverable = true;
		options.points.show = false;		
		
		options.lines.show = false;

		options.xaxis.mode = "time";
		options.xaxis.timeformat = "%y/%m";
		
		options.pan.interactive = false;						
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
		case DAY:
			options.xaxis.minTickSize = new String[]{"3","day"};
			options.xaxis.timeformat = "%b %d";
			break;
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
	
	public ChartGranularity getGranularity() { 
		return granularity;
	}
	
	public DateRange getDateRange() {
		return dateRange;
	}
	
}
