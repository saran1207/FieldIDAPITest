package com.n4systems.util.chart;

import com.n4systems.model.api.Listable;
import com.n4systems.model.utils.DateRange;
import com.n4systems.model.utils.DateRangeFormatter;
import com.n4systems.model.utils.DateRangeHandler;

import java.io.Serializable;
import java.util.EnumSet;

public enum RangeType implements Listable<String>, Serializable {

    // -- non floating date ranges --
    // day range
	SEVEN_DAYS("7 days", new DateRange.DayRangeHandler(7), new DaysRangeFormatter("7 days")),
	THIRTY_DAYS("30 days", new DateRange.DayRangeHandler(30), new DaysRangeFormatter("30 days")),
	SIXTY_DAYS("60 days", new DateRange.DayRangeHandler(60), new DaysRangeFormatter("60 days")),
	NINETY_DAYS("90 days", new DateRange.DayRangeHandler(90), new DaysRangeFormatter("90 days")),
    // -- floating date ranges --
    //historic
    YESTERDAY("Yesterday", new DateRange.DayHandler(-1),  new FloatingDateRangeFormatter("Yesterday", "MMM d")),
    LAST_WEEK("Last Week", new DateRange.WeekHandler(-1),  new FloatingDateRangeFormatter("Last Week", "MMM d")),
    LAST_MONTH("Last Month", new DateRange.MonthHandler(-1), new FloatingDateRangeFormatter("Last Month", "MMM yyyy")),
    LAST_QUARTER("Last Quarter", new DateRange.QuarterHandler(-1), new QuarterDateRangeFormatter("Last Quarter")),
    LAST_YEAR("Last Year", new DateRange.YearHandler(-1), new FloatingDateRangeFormatter("Last Year", "yyyy")),
    // current
    TODAY("Today", new DateRange.DayHandler(0),  new FloatingDateRangeFormatter("Today", "MMM d")),
    THIS_WEEK("This Week", new DateRange.WeekHandler(0), new FloatingDateRangeFormatter("This Week", "MMM d")),
    THIS_MONTH("This Month", new DateRange.MonthHandler(0), new FloatingDateRangeFormatter("This Month", "MMM yyyy")),
    THIS_QUARTER("This Quarter", new DateRange.QuarterHandler(0), new QuarterDateRangeFormatter("This Quarter")),
    THIS_YEAR("This Year", new DateRange.YearHandler(0), new FloatingDateRangeFormatter("This Year", "yyyy")),
     //future
    TOMORROW("Tomorrow", new DateRange.DayHandler(1), new FloatingDateRangeFormatter("Tomorrow", "MMM d")),
    NEXT_WEEK("Next Week", new DateRange.WeekHandler(1), new FloatingDateRangeFormatter("Next Week", "MMM d")),
    NEXT_MONTH("Next Month", new DateRange.MonthHandler(1), new FloatingDateRangeFormatter("Next Month", "MMM yyyy")),
    NEXT_QUARTER("Next Quarter", new DateRange.QuarterHandler(1), new QuarterDateRangeFormatter("Next Quarter")),
    NEXT_YEAR("Next Year", new DateRange.YearHandler(1), new FloatingDateRangeFormatter("Next Year", "yyyy")),

    FOREVER("All Time", new DateRange.IntervalHandler(), new StaticDateRanageFormatter("All Time")),
    CUSTOM("Custom Date Range", new DateRange.IntervalHandler(), new StaticDateRanageFormatter("Custom Date Range"));

    private static EnumSet<RangeType> predefinedTypes = EnumSet.complementOf(EnumSet.of(CUSTOM,FOREVER)); // strictly defined. no user data required.

    private static EnumSet<RangeType> wellDefinedTypes = EnumSet.complementOf(EnumSet.of(FOREVER)); // have specific start/end date.

    private static EnumSet<RangeType> customTypes = EnumSet.of(CUSTOM); 

    private static EnumSet<RangeType> historicFloatingTypes = EnumSet.of(  YESTERDAY, LAST_WEEK, LAST_MONTH, LAST_QUARTER, LAST_YEAR );
    private static EnumSet<RangeType> currentFloatingTypes = EnumSet.of(  TODAY, THIS_WEEK, THIS_MONTH, THIS_QUARTER, THIS_YEAR );
    private static EnumSet<RangeType> futureFloatingTypes = EnumSet.of(  TOMORROW, NEXT_WEEK, NEXT_MONTH, NEXT_QUARTER, NEXT_YEAR );

	private static EnumSet<RangeType> daysFromNowRangeTypes = EnumSet.of(SEVEN_DAYS, THIRTY_DAYS, SIXTY_DAYS, NINETY_DAYS);
	private static EnumSet<RangeType> dailyRangeTypes = EnumSet.of(TODAY, YESTERDAY, TOMORROW);
    

    private DateRangeHandler handler;
    private DateRangeFormatter formatter;
    private String displayName;

    RangeType(String displayName, DateRangeHandler handler, DateRangeFormatter formatter) {
        this.displayName = displayName;
        this.handler = handler;
        this.formatter = formatter;
    }

    public static EnumSet<RangeType> allFloatingButFutureTypes() {
        EnumSet<RangeType> result = EnumSet.copyOf(allFloatingTypes());
        result.removeAll(futureFloatingTypes);  // don't want NEXT_YEAR, NEXT_WEEK, etc...
        return result;
    }

    public static EnumSet<RangeType> allFloatingTypes() {
        return EnumSet.complementOf(daysFromNowRangeTypes);
    }

	public boolean isDaily() {
		return dailyRangeTypes.contains(this);
	}	
	
	public boolean isDaysFromNowRangeType()  {
		return daysFromNowRangeTypes.contains(this);
	}

	public boolean isPredefinedType()  {
		return predefinedTypes.contains(this);
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
