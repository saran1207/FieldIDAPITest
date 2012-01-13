package com.n4systems.util.chart;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.util.EnumUtils;
import com.n4systems.util.time.DateUtil;

public enum ChartGranularity {
	
	YEAR(new Period().withYears(1)), 
	QUARTER(new Period().withMonths(3)), 
	MONTH(new Period().withMonths(1)), 
	WEEK(new Period().withWeeks(1)),   // note : weeks start on monday.  (i.e. withDayOfWeek(1) will set dates to monday)
	DAY(new Period().withDays(1));

	private Period period;

	private ChartGranularity(Period period) {
		this.period = period;
	}
	
	public Period getPeriod() { 
		return period;
	}
	
	public int compare(LocalDate a, LocalDate b) {		
		int comparison = 0;
		if (a==null) { 
			return b==null ? 0 : -1; 
		}
		if (b==null) {
			return 1;
		}
		for (ChartGranularity granularity:values()) {  // CAVEAT : enum must be in order of most to least significant. 
			if (granularity.compareTo(this)<=0) {
				comparison = compare(a,b,granularity);
				if (comparison!=0) {
					return comparison;
				}
			}
		}
		return 0;		
	}
	
	private int compare(LocalDate a, LocalDate b, ChartGranularity granularity) {
		switch (granularity) { 
		case YEAR:
			return a.getYear() - b.getYear();
		case QUARTER: 
			return DateUtil.getQuarter(a) - DateUtil.getQuarter(b);
		case MONTH:
			return a.getMonthOfYear() - b.getMonthOfYear();
		case WEEK:
			// edge case : it is possible that jan 1st could return week 52. (i.e. it is at the end of the week that began at the end
			// of the previous year.  .: we can't just compare weeks, we must bring in year as well).			
			return (a.getWeekyear()*100 + a.getWeekOfWeekyear()) - (b.getWeekyear()*100 + b.getWeekOfWeekyear());
		case DAY: 
			return a.getDayOfYear() - b.getDayOfYear();
		default: 
			throw new InvalidArgumentException("invalid chart granularity used when comparing");
		}
	}

	public Period preferredRange() {
		switch (this) { 
		case DAY:
			return new Period().withDays(30);			
		case WEEK:
			return new Period().withMonths(12);
		case MONTH: 
			return new Period().withYears(3);
		case QUARTER:
			return new Period().withYears(7);
		case YEAR:
			return new Period().withYears(10);
		default: 
			throw new InvalidArgumentException("invalid chart granularity used when calculating delta");			
		}
	}

	/**
	 * note that this just doesn't add a period to given date. 
	 * for example, with granularity YEAR  
	 * 	next(Oct 7,2011) will return Jan1,2012   if i just added 1 year i would get Oct 7, 2012.  wrong!
	 * 	next(Jan 1,2011) will return Jan1,2012
	 * 	next(Dec 31,2011) will return Jan1,2012
	 */
	public LocalDate next(LocalDate date) {
		return roundDown(date).plus(period);
	}

	public LocalDate previous(LocalDate date) {
		return normalize(date).minus(period);
	}

	public LocalDate normalize(LocalDate date) {
		if(date==null) { 
			return null;
		}
		switch (this) { 
		case DAY:
			return date;
		case WEEK:
			// important : the database & java code must be in synch with what they call the "first day of the week". 
			// at this time it is decided that it is monday, not sunday.
			// if you change this code you may need to change any queries use of the WEEK(dateValue, [dateMode]) method.
			return date.withDayOfWeek(DateTimeConstants.MONDAY);
		case MONTH: 
			return date.withDayOfMonth(1);
		case QUARTER:
			int quarterMonth = DateUtil.getQuarterMonth(date); 
			return date.withMonthOfYear(quarterMonth).withDayOfMonth(1);
		case YEAR:
			return date.withDayOfYear(1);
		default: 
			throw new InvalidArgumentException("invalid chart granularity used when comparing");
		}
	}

	/**
	 *  rounding is needed for peculiar cases.  for example if the user asks for assets identified in january viewed weekly there are some
	 *  subtleties to be aware of. say january is...
	 *     -  -  -  -  -  1  2  
	 *     3  4  5  6  7  8  9
	 *     10 11 12 13        etc...
	 *   since the week that includes jan 1, 2, 3 begins in dec those points. (4 is monday...part of the next week) it is 
	 *   considered to be in the last week of the previous year.
	 *   so when generating this query, the "from" date is dec  28th-jan3rd.   (i.e. round down jan 1-->dec 28) 
	 */
	public LocalDate roundDown(LocalDate date) {		
		return normalize(date);
	}

	public LocalDate roundUp(LocalDate date) {
		LocalDate normalized = normalize(date);		
		return (date.isEqual(normalized)) ? date : next(date); 
	}

	public ChartGranularity finer() {
		return EnumUtils.next(this);
	}

	public ChartGranularity coarser() {
		return EnumUtils.previous(this);
	}

}
