package com.n4systems.util.chart;

import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import com.n4systems.model.api.Listable;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.time.DateUtil;

public enum FloatingDateRange implements Listable<String>, Serializable {

	SEVEN_DAYS(DateRange.SEVEN_DAYS),  
	THIRTY_DAYS(DateRange.THIRTY_DAYS), 
	SIXTY_DAYS(DateRange.SIXTY_DAYS), 
	NINETY_DAYS(DateRange.NINETY_DAYS),  
	LAST_WEEK(DateRange.LAST_WEEK),  
	LAST_MONTH(DateRange.LAST_MONTH),  
	LAST_QUARTER(DateRange.LAST_QUARTER),  
	LAST_YEAR(DateRange.LAST_YEAR), 
	THIS_WEEK(DateRange.THIS_WEEK),  
	THIS_MONTH(DateRange.THIS_MONTH),  
	THIS_QUARTER(DateRange.THIS_QUARTER),  
	THIS_YEAR(DateRange.THIS_YEAR), 
	FOREVER(DateRange.FOREVER), 
    CUSTOM(DateRange.CUSTOM); 
	
		
	
	private static EnumSet<FloatingDateRange> floatingRanges = EnumSet.of(LAST_WEEK, LAST_MONTH, LAST_QUARTER, LAST_YEAR, THIS_WEEK, THIS_MONTH, THIS_QUARTER, THIS_YEAR, FOREVER);
	private static EnumSet<FloatingDateRange> daysFromNowRanges = EnumSet.of(SEVEN_DAYS, THIRTY_DAYS, SIXTY_DAYS, NINETY_DAYS);
	
	private DateRange dateRange;

	FloatingDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
	}

	public static FloatingDateRange[] chartDateRanges() {
		return floatingRanges.toArray(new FloatingDateRange[]{});
	}
		
	public static FloatingDateRange[] chartDateRangesWithCustom() {
		EnumSet<FloatingDateRange> set = EnumSet.copyOf(floatingRanges);
		set.add(FloatingDateRange.CUSTOM);
		return set.toArray(new FloatingDateRange[]{});
	}
		
	public static LocalDate earliestDate() {
		return DateUtil.getEarliestFieldIdDate();
	}
	
	public boolean isDaysFromNowRange()  {
		return daysFromNowRanges.contains(this);
	}
		
	public static FloatingDateRange forDays(Integer period) {
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
		return dateRange.calculateFromDate();
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
		return dateRange.calculateToDate();
	}
	
	public DateRange asDateRange() { 
		return dateRange;// make sure this is immutable.
	}

	public static FloatingDateRange fromDateRange(DateRange dateRange) {
		if (dateRange==null) { 
			return null;
		}
		for (FloatingDateRange floatingDateRange :values()) {
			if (floatingDateRange.asDateRange().equals(dateRange)) {
				return floatingDateRange;
			}
		}
		return null;
	}

	public Period getPeriod() {
		return dateRange.getPeriod();
	}

	public Duration getDuration() {
		//sigh. i wish i could just call toStandardDuration() but i can't 'cause it's too long.
		DateTime x = new DateTime();
		DateTime y = x.plus(getPeriod());
		return new Duration(x,y);
		
	}

}
