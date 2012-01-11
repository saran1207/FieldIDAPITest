package com.n4systems.util.chart;

import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;

import org.joda.time.LocalDate;

import com.n4systems.model.api.Listable;
import com.n4systems.model.utils.DateRange;
import com.n4systems.model.utils.DateRange.MonthHandler;
import com.n4systems.model.utils.DateRange.QuarterHandler;
import com.n4systems.model.utils.DateRange.WeekHandler;
import com.n4systems.model.utils.DateRange.YearHandler;
import com.n4systems.model.utils.DateRangeFormatter;
import com.n4systems.model.utils.DateRangeHandler;
import com.n4systems.util.time.DateUtil;

public enum ChartDateRange implements Listable<String>, Serializable {

	SEVEN_DAYS(7, new DaysRangeFormatter("7 days", 7)), 
	THIRTY_DAYS(30, new DaysRangeFormatter("30 days", 30)), 
	SIXTY_DAYS(60, new DaysRangeFormatter("60 days", 60)), 
	NINETY_DAYS(90, new DaysRangeFormatter("90 days", 90)), 
	LAST_WEEK(new WeekHandler(-1),  new FloatingDateRangeFormatter("Last Week", "MMM d")), 
	LAST_MONTH(new MonthHandler(-1), new FloatingDateRangeFormatter("Last Month", "MMM yyyy")), 
	LAST_QUARTER(new QuarterHandler(-1), new QuarterDateRangeFormatter("Last Quarter")), 
	LAST_YEAR(new YearHandler(-1), new FloatingDateRangeFormatter("Last Year", "yyyy")),
	THIS_WEEK(new WeekHandler(0), new FloatingDateRangeFormatter("This Week", "MMM d")), 
	THIS_MONTH(new MonthHandler(0), new FloatingDateRangeFormatter("This Month", "MMM yyyy")), 
	THIS_QUARTER(new QuarterHandler(0), new QuarterDateRangeFormatter("This Quarter")), 
	THIS_YEAR(new YearHandler(0), new FloatingDateRangeFormatter("This Year", "yyyy")), 
	FOREVER(new StaticDateRanageFormatter("All Time")),
    CUSTOM(new StaticDateRanageFormatter("Custom Date Range"));
	
	
	private static EnumSet<ChartDateRange> chartRanges = EnumSet.of(LAST_WEEK, LAST_MONTH, LAST_QUARTER, LAST_YEAR, THIS_WEEK, THIS_MONTH, THIS_QUARTER, THIS_YEAR, FOREVER);
	private static EnumSet<ChartDateRange> daysFromNowRanges = EnumSet.of(SEVEN_DAYS, THIRTY_DAYS, SIXTY_DAYS, NINETY_DAYS);
	
	private DateRange dateRange;
	private String id;

	ChartDateRange(int days, DateRangeFormatter formatter) {
		this.dateRange = new DateRange(days, formatter);
	}

	ChartDateRange(DateRangeHandler handler, DateRangeFormatter formatter) {
		this.dateRange = new DateRange(handler, formatter);
	}

	ChartDateRange(DateRangeFormatter formatter) {
		this.dateRange = new DateRange(formatter);
	}

	public static ChartDateRange[] chartDateRanges() { 
		return chartRanges.toArray(new ChartDateRange[]{});
	}
		
	public static ChartDateRange[] chartDateRangesWithCustom() {
		EnumSet<ChartDateRange> set = EnumSet.copyOf(chartRanges);
		set.add(ChartDateRange.CUSTOM);
		return set.toArray(new ChartDateRange[]{});		
	}
		
	public static LocalDate earliestDate() {
		return DateUtil.getEarliestFieldIdDate();
	}
	
	public boolean isDaysFromNowRange()  {
		return daysFromNowRanges.contains(this);
	}
		
	public static ChartDateRange forDays(Integer period) {
		switch (period) { 
			case 7:
				return SEVEN_DAYS;
			case 30: 
				return THIRTY_DAYS;
			case 60: 
				return SIXTY_DAYS;
			case 90:
				return NINETY_DAYS;
			default:
				throw new IllegalStateException("can't find chartDateRange for " + period + " days.");
		}
	}

	@Override
	public String getId() {
		return name();
	}

	@Override
	public String getDisplayName() {
		return dateRange.getDisplayName();
	}

	public LocalDate getTo() {
		return dateRange.getTo();
	}

	public LocalDate getFrom() {
		return dateRange.getFrom();
	}
	
	public LocalDate getEarliest() { 
		return dateRange.getFrom()==null ? DateUtil.getEarliestFieldIdDate() : dateRange.getFrom();
	}

	public LocalDate getLatest() {
		return dateRange.getTo()==null ? DateUtil.getLatestFieldIdDate() : dateRange.getTo();
	}

	
	public Date getFromDate() {
		return dateRange.getFromDate();
	}

	public Date getInclusiveToDate() {
		return dateRange.getInclusiveToDate();
	}

	public String getFromDateDisplayString() {
		return dateRange.getFromDateDisplayString();
	}

	public String getToDateDisplayString() {
		return dateRange.getToDateDisplayString();
	}

	public Date getToDate() {
		return dateRange.getToDate();
	}
	
	public DateRange asDateRange() { 
		return dateRange;// make sure this is immutable.
	}

}
