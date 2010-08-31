package com.n4systems.model.common;

import com.n4systems.model.api.Listable;
import com.n4systems.util.DateHelper;

import java.util.Date;

public enum RelativeTime implements Listable<String> {
	TODAY		("label.today",			DateHelper.DAY,		0),
	TOMORROW	("label.tomorrow",		DateHelper.DAY,		1),
	DAY_1		("label.1_day",			DateHelper.DAY,		1),
	DAY_2		("label.2_day",			DateHelper.DAY,		2),
	DAY_7		("label.7_day",			DateHelper.DAY,		7),
	THIS_WEEK 	("label.this_week",		DateHelper.WEEK,	0),
	NEXT_WEEK 	("label.next_week",		DateHelper.WEEK,	1),
	WEEK_1		("label.1_week",		DateHelper.WEEK,	1),
	WEEK_2		("label.2_week",		DateHelper.WEEK,	2),
	THIS_MONTH	("label.this_month",	DateHelper.MONTH, 	0),
	NEXT_MONTH	("label.next_month",	DateHelper.MONTH,	1),
	MONTH_1		("label.1_month",		DateHelper.MONTH,	1),
	MONTH_2		("label.2_month",		DateHelper.MONTH,	2),
	MONTH_3		("label.3_month",		DateHelper.MONTH,	3),
	THIS_YEAR	("label.this_year",		DateHelper.YEAR, 	0),
	NEXT_YEAR	("label.next_year",		DateHelper.YEAR,	1);
	
	
	private String label;
	private int dateField;
	private int increment;
	
	RelativeTime(String label, int dateField, int increment) {
		this.label = label;
		this.dateField = dateField;
		this.increment = increment;
	}
	
	public String getDisplayName() {
	    return label;
    }

	public String getId() {
	    return name();
    }
	
	public String getLabel() {
		return label;
	}
	
	/**
	 * Computes the relative date for the given date, based on this time period and the.  All dates are truncated to day start (00:00:00).
	 * Examples:
	 * <pre>
	 * If the current date/time is May 25th 2009, 1:15pm:
	 *	date = SimpleTimePeriod.TODAY.prepareDate(new Date());		// returns Monday May 25th 2009, 00:00:00
	 *	date = SimpleTimePeriod.TOMORROW.prepareDate(new Date()); 	// returns Tuesday May 26th 2009, 00:00:00
	 *	date = SimpleTimePeriod.DAY_7.prepareDate(new Date());  	// returns Monday June 1st 2009, 00:00:00
	 *	date = SimpleTimePeriod.THIS_WEEK.prepareDate(new Date());  	// returns Sunday May 24th 2009, 00:00:00
	 *	date = SimpleTimePeriod.NEXT_MONTH.prepareDate(new Date());  	// returns Monday June 1st 2009, 00:00:00
	 * </pre>
	 * @param fromDate	Date to start relative calculation from
	 * @return			The relative calculated time
	 */
	public Date getRelative(Date fromDate) {
		// first remove less priority fields
		Date truncated = DateHelper.truncate(fromDate, dateField);
		
		// then add the increment
		Date incremented = DateHelper.increment(truncated, dateField, increment);
		
		return incremented;
	}
	
}
