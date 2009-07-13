package com.n4systems.model.common;

import com.n4systems.model.api.Listable;

import java.util.Calendar;
import java.util.Date;

public enum SimpleFrequency implements Listable<String> {
	DAILY				(	"label.daily",		"label.daily", 				null				),
	WEEKLY_SUNDAY		(	"label.weekly",		"label.sun.long", 			Calendar.SUNDAY		),
	WEEKLY_MONDAY		(	"label.weekly",		"label.mon.long", 			Calendar.MONDAY		),
	WEEKLY_TUESDAY		(	"label.weekly", 	"label.tues.long", 			Calendar.TUESDAY	),
	WEEKLY_WEDNESDAY	(	"label.weekly", 	"label.wed.long", 			Calendar.WEDNESDAY	),
	WEEKLY_THURSDAY		(	"label.weekly",		"label.thurs.long", 		Calendar.THURSDAY	),
	WEEKLY_FRIDAY		(	"label.weekly",		"label.fri.long", 			Calendar.FRIDAY		),
	WEEKLY_SATURDAY		(	"label.weekly",		"label.sat.long", 			Calendar.SATURDAY	),
	MONTHLY_FIRST		(	"label.monthly",	"label.day_of_month_first",	1					),
	MONTHLY_15TH		(	"label.monthly",	"label.day_of_month_15", 	15					),
	MONTHLY_LAST		(	"label.monthly",	"label.day_of_month_last", 	null				);
	
	private String groupLabel;
	private String label;
	private Integer dayValue;
	
	SimpleFrequency(String groupLabel, String label, Integer dayValue) {
		this.groupLabel = groupLabel;
		this.label = label;
		this.dayValue = dayValue;
	}

	public String getDisplayName() {
	    return label;
    }

	public String getId() {
	    return name();
    }
	
	public boolean isGrouped() {
		return (groupLabel != null);
	}

	public String getGroupLabel() {
    	return groupLabel;
    }

	public String getLabel() {
    	return label;
    }

	public Integer getDayValue() {
    	return dayValue;
    }
	
	public boolean isSameDay(Date targetDay) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(targetDay);
		
		Integer targetDayValue = null;
		switch(this) {
			case DAILY:
				// we will let targetDayValue remain null
				break;
			case WEEKLY_SUNDAY:
			case WEEKLY_MONDAY:
			case WEEKLY_TUESDAY:
			case WEEKLY_WEDNESDAY:
			case WEEKLY_THURSDAY:
			case WEEKLY_FRIDAY:
			case WEEKLY_SATURDAY:
				targetDayValue = cal.get(Calendar.DAY_OF_WEEK);
				break;
			case MONTHLY_FIRST:
			case MONTHLY_15TH:
				targetDayValue = cal.get(Calendar.DAY_OF_MONTH);
				break;
			case MONTHLY_LAST:
				// last day is a special case, we need to figure out the last day
				targetDayValue = cal.get(Calendar.DAY_OF_MONTH);
				dayValue = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				break;
		}
		
		return (targetDayValue == dayValue);
	}
}
