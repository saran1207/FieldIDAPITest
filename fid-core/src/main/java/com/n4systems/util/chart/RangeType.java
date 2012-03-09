package com.n4systems.util.chart;

import java.io.Serializable;
import java.util.EnumSet;

import com.n4systems.model.api.Listable;
import com.n4systems.model.utils.DateRange;
import com.n4systems.model.utils.DateRangeFormatter;
import com.n4systems.model.utils.DateRangeHandler;

public enum RangeType implements Listable<String>, Serializable {

	SEVEN_DAYS("7 days", new DateRange.DayRangeHandler(7), new DaysRangeFormatter("7 days")),
	THIRTY_DAYS("30 days", new DateRange.DayRangeHandler(30), new DaysRangeFormatter("30 days")),
	SIXTY_DAYS("60 days", new DateRange.DayRangeHandler(60), new DaysRangeFormatter("60 days")),
	NINETY_DAYS("90 days", new DateRange.DayRangeHandler(90), new DaysRangeFormatter("90 days")),
	TODAY("Today", new DateRange.DayHandler(0),  new FloatingDateRangeFormatter("Today", "MMM d")),
	YESTERDAY("Yesterday", new DateRange.DayHandler(-1),  new FloatingDateRangeFormatter("Yesterday", "MMM d")),
	LAST_WEEK("Last Week", new DateRange.WeekHandler(-1),  new FloatingDateRangeFormatter("Last Week", "MMM d")),
	LAST_MONTH("Last Month", new DateRange.MonthHandler(-1), new FloatingDateRangeFormatter("Last Month", "MMM yyyy")),
	LAST_QUARTER("Last Quarter", new DateRange.QuarterHandler(-1), new QuarterDateRangeFormatter("Last Quarter")),
	LAST_YEAR("Last Year", new DateRange.YearHandler(-1), new FloatingDateRangeFormatter("Last Year", "yyyy")),
	THIS_WEEK("This Week", new DateRange.WeekHandler(0), new FloatingDateRangeFormatter("This Week", "MMM d")),
	THIS_MONTH("This Month", new DateRange.MonthHandler(0), new FloatingDateRangeFormatter("This Month", "MMM yyyy")),
	THIS_QUARTER("This Quarter", new DateRange.QuarterHandler(0), new QuarterDateRangeFormatter("This Quarter")),
	THIS_YEAR("This Year", new DateRange.YearHandler(0), new FloatingDateRangeFormatter("This Year", "yyyy")),
	FOREVER("All Time", new DateRange.IntervalHandler(), new StaticDateRanageFormatter("All Time")),
    CUSTOM("Custom Date Range", new DateRange.IntervalHandler(), new StaticDateRanageFormatter("Custom Date Range"));

	
	private static EnumSet<RangeType> wellDefinedRangeTypes = EnumSet.complementOf(EnumSet.of(CUSTOM,FOREVER)); // everything except the ambiguous ones like CUSTOM.
	private static EnumSet<RangeType> floatingRangeTypes = EnumSet.of(YESTERDAY, LAST_WEEK, LAST_MONTH, LAST_QUARTER, LAST_YEAR, TODAY, THIS_WEEK, THIS_MONTH, THIS_QUARTER, THIS_YEAR, FOREVER);
	private static EnumSet<RangeType> daysFromNowRangeTypes = EnumSet.of(SEVEN_DAYS, THIRTY_DAYS, SIXTY_DAYS, NINETY_DAYS);
	private static EnumSet<RangeType> dailyRangeTypes = EnumSet.of(TODAY, YESTERDAY);

    private DateRangeHandler handler;
    private DateRangeFormatter formatter;
    private String displayName;

	RangeType(String displayName, DateRangeHandler handler, DateRangeFormatter formatter) {
        this.displayName = displayName;
        this.handler = handler;
        this.formatter = formatter;
    }

	public static RangeType[] rangeTypes() {
		return floatingRangeTypes.toArray(new RangeType[]{});
	}
		
	public static RangeType[] rangeTypesWithCustom() {
		EnumSet<RangeType> set = EnumSet.copyOf(floatingRangeTypes);
		set.add(RangeType.CUSTOM);
		return set.toArray(new RangeType[]{});
	}

	public boolean isDaily() {
		return dailyRangeTypes.contains(this);
	}	
	
	public boolean isDaysFromNowRangeType()  {
		return daysFromNowRangeTypes.contains(this);
	}

	public boolean isWellDefinedRangeType()  {
		return wellDefinedRangeTypes.contains(this);
	}

	public static RangeType forDays(Integer period) {
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
		return displayName;
	}

    public DateRangeHandler getHandler() {
        return handler;
    }

    public DateRangeFormatter getFormatter() {
        return formatter;
    }
}
