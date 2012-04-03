package com.n4systems.model.common;

import com.n4systems.model.api.Listable;
import com.n4systems.util.DateHelper;

import java.util.Date;

public enum RelativeTime implements Listable<String> {

	TODAY		("label.today",			DateHelper.DAY,		0, false),
	TOMORROW	("label.tomorrow",		DateHelper.DAY,		1, false),
    THIS_WEEK 	("label.this_week",		DateHelper.WEEK,	0, false),
    NEXT_WEEK 	("label.next_week",		DateHelper.WEEK,	1, false),
    THIS_MONTH	("label.this_month",	DateHelper.MONTH, 	0, false),
    NEXT_MONTH	("label.next_month",	DateHelper.MONTH,	1, false),

	DAY_1		("label.1_day",			DateHelper.DAY,		1, true),
	DAY_2		("label.2_day",			DateHelper.DAY,		2, true),
	DAY_7		("label.7_day",			DateHelper.DAY,		7, true),
	WEEK_1		("label.1_week",		DateHelper.WEEK,	1, true),
	WEEK_2		("label.2_week",		DateHelper.WEEK,	2, true),
	MONTH_1		("label.1_month",		DateHelper.MONTH,	1, true),
	MONTH_2		("label.2_month",		DateHelper.MONTH,	2, true),
	MONTH_3		("label.3_month",		DateHelper.MONTH,	3, true),
	MONTH_6		("label.6_month",		DateHelper.MONTH,	6, true),
	MONTH_9		("label.9_month",		DateHelper.MONTH,	9, true),
	MONTH_12	("label.12_month",		DateHelper.MONTH,	12, true),
	MONTH_18	("label.18_month",		DateHelper.MONTH,	18, true),
	MONTH_24	("label.24_month",		DateHelper.MONTH,	24, true);
	
	
	private String label;
	private int dateField;
	private int increment;
    private boolean truncateJustDay;
	
	RelativeTime(String label, int dateField, int increment, boolean truncateJustDay) {
		this.label = label;
		this.dateField = dateField;
		this.increment = increment;
        this.truncateJustDay = truncateJustDay;
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
        int fieldToTruncate = dateField;
        if (truncateJustDay) {
            fieldToTruncate = DateHelper.DAY;
        }

        Date truncated = DateHelper.truncate(fromDate, fieldToTruncate);

		// then add the increment
		Date incremented = DateHelper.increment(truncated, dateField, increment);
		
		return incremented;
	}
	
}
