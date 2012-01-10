package com.n4systems.util.chart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.api.Listable;
import com.n4systems.util.time.DateUtil;

public enum ChartDateRange implements Listable<String> {

	SEVEN_DAYS("7 days"), 
	THIRTY_DAYS("30 days"), 
	SIXTY_DAYS("60 days"), 
	NINETY_DAYS("90 days"), 
	LAST_WEEK("Last Week" ), 
	LAST_MONTH("Last Month"), 
	LAST_QUARTER("Last Quarter"), 
	LAST_YEAR("Last Year"),
	THIS_WEEK("This Week"), 
	THIS_MONTH("This Month"), 
	THIS_QUARTER("This Quarter"), 
	THIS_YEAR("This Year"), 
	FOREVER("All Time"), 
	CUSTOM("Custom Date Range");
	

	private static EnumSet<ChartDateRange> chartRanges = EnumSet.of(LAST_WEEK, LAST_MONTH, LAST_QUARTER, LAST_YEAR, THIS_WEEK, THIS_MONTH, THIS_QUARTER, THIS_YEAR, FOREVER);
	private static EnumSet<ChartDateRange> daysFromNowRanges = EnumSet.of(SEVEN_DAYS, THIRTY_DAYS, SIXTY_DAYS, NINETY_DAYS);
	
	private String displayName;

	private static Map<ChartDateRange, DateFormat> dateFormatters = new HashMap<ChartDateRange, DateFormat>();

	private static DateFormat quarterFromFormat;
	private static DateFormat defaultFormat;
	
	static { 
		dateFormatters.put(LAST_WEEK, new SimpleDateFormat("MMM d"));
		dateFormatters.put(LAST_MONTH, new SimpleDateFormat("MMM yyyy"));
		dateFormatters.put(LAST_QUARTER, new SimpleDateFormat("MMM yyyy"));
		dateFormatters.put(LAST_YEAR, new SimpleDateFormat("yyyy"));
		dateFormatters.put(THIS_WEEK, new SimpleDateFormat("MMM d"));
		dateFormatters.put(THIS_MONTH, new SimpleDateFormat("MMM yyyy"));
		dateFormatters.put(THIS_QUARTER, new SimpleDateFormat("MMM yyyy"));
		dateFormatters.put(THIS_YEAR, new SimpleDateFormat("yyyy"));
		dateFormatters.put(FOREVER, null);	// FOREVER just returns a text string. 
		quarterFromFormat = new SimpleDateFormat("MMM");  // NOTE : quarter display = Jan-Mar 2011.  i.e. "from" is different format than "to"
		defaultFormat = new SimpleDateFormat("MMM d yyyy"); 
	}

	ChartDateRange(String displayName) {
		this.displayName = displayName;		
	}

	public String getFromDateDisplayString() {
		DateFormat formatter = getFormatter();
		if (this.equals(FOREVER)) { 
			return "All Time";
		}
		if (this.equals(LAST_QUARTER) || this.equals(THIS_QUARTER)) {
			formatter = quarterFromFormat;  
		}
		return formatter.format(getFrom().toDate());
	}

	private DateFormat getFormatter() {
		DateFormat format = dateFormatters.get(this);
		return format != null ? format : defaultFormat;
	}
	
	public String getToDateDisplayString() {
		if (this.equals(FOREVER)) { 
			return "";
		} else { 
			return getFormatter().format(getInclusiveTo().toDate());
		}
	}
	
	// note that getTo() is exclusive.  e.g. for a year 2011
	// Jan 1, 2011 is getFrom() and getTo() is Jan 1, 2012  *not*  dec 31,2011    [11:59:59.9999...]
	// this method is used for display reasons.  (e.g.  "hello oct 1,2011-oct 31,2011") 
	private LocalDate getInclusiveTo() {
		return getTo().minusDays(1);
	}
	
	public Date getInclusiveToDate() { 
		return getInclusiveTo().toDate();
	}

	public LocalDate getTo() { 
		// exclusive date :  should use <  *not*  <= when comparing against returned value!!!
		LocalDate today = LocalDate.now();
		
		switch (this) {
			case FOREVER: 	
			case CUSTOM:
				return DateUtil.getLatestFieldIdDate();
			case LAST_YEAR:
				return today.withDayOfYear(1); 
			case THIS_YEAR:
				return today.plusYears(1).withDayOfYear(1);
			case LAST_QUARTER:
				return today.minusMonths((today.getMonthOfYear()-1)%3).withDayOfMonth(1);
			case THIS_QUARTER:
				return today.plusMonths(3-(today.getMonthOfYear()-1)%3).withDayOfMonth(1);
			case LAST_MONTH:
				return today.withDayOfMonth(1);
			case THIS_MONTH:
				return today.plusMonths(1).withDayOfMonth(1);
			case LAST_WEEK:
				return today.withDayOfWeek(1);
			case THIS_WEEK:
				return today.plusWeeks(1).withDayOfWeek(1);
			case SEVEN_DAYS:
				return today.plusDays(7);
			case THIRTY_DAYS:
				return today.plusDays(30);
			case SIXTY_DAYS:
				return today.plusDays(60);
			case NINETY_DAYS:
				return today.plusDays(90);
			default: 
				throw new InvalidArgumentException("ChartDateRange " + this + " not supported."); 
		}
	}

	public LocalDate getFrom() {
		LocalDate today = new LocalDate();
		switch (this) {
			case CUSTOM:
			case FOREVER: 			
				return DateUtil.getEarliestFieldIdDate();
			case LAST_YEAR:
				return today.minusYears(1).withDayOfYear(1);				
			case THIS_YEAR:
				return today.withDayOfYear(1);				
			case LAST_QUARTER:
				return today.minusMonths(3+(today.getMonthOfYear()-1)%3).withDayOfMonth(1);
			case THIS_QUARTER:
				return today.minusMonths((today.getMonthOfYear()-1)%3).withDayOfMonth(1);
			case LAST_MONTH:
				return today.minusMonths(1).withDayOfMonth(1);
			case THIS_MONTH:
				return today.withDayOfMonth(1);
			case LAST_WEEK:
				return today.minusWeeks(1).withDayOfWeek(1);
			case THIS_WEEK:
				return today.withDayOfWeek(1);
			case SEVEN_DAYS:
			case THIRTY_DAYS:
			case SIXTY_DAYS:
			case NINETY_DAYS:
				return today;
			default: 
				throw new InvalidArgumentException("ChartDateRange " + this + " not supported."); 
		}
	}
	
	public static ChartDateRange[] chartDateRanges() { 
		return chartRanges.toArray(new ChartDateRange[]{});
	}
		
	public static ChartDateRange[] chartDateRangesWithCustom() {
		EnumSet<ChartDateRange> set = EnumSet.copyOf(chartRanges);
		set.add(ChartDateRange.CUSTOM);
		return set.toArray(new ChartDateRange[]{});		
	}
		
	public boolean isDaysFromNowRange()  {
		return daysFromNowRanges.contains(this);
	}
		
	@Override
	public String getDisplayName() { 
		return displayName;
	}

	public Date getFromDate() {
		return getFrom().toDate();
	}

	public Date getToDate() {
		return getTo().toDate();
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
	
}
