package com.n4systems.util.time;

import java.util.Calendar;

public class DateUtils {

	public static Calendar getCalendar(Integer year, Integer quarter, Integer month, Integer week, Integer day) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		setValue(calendar, Calendar.YEAR, year);
		setValue(calendar, Calendar.MONTH, (quarter-1)*3);		
		setValue(calendar, Calendar.WEEK_OF_YEAR, week);
		setValue(calendar, Calendar.DAY_OF_YEAR, day);
		// currently doesn't handle hours/minutes/seconds etc...
		return calendar;
	}	

	private static void setValue(Calendar calendar, int calendarParam, Integer value) {
		if (value>=0) {
			calendar.set(calendarParam, value.intValue());
		}
	}
	
}
