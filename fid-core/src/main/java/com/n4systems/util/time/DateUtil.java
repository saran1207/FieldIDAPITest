package com.n4systems.util.time;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static long SECONDINMILLIS = 1000;
	public static long MINUTEINMILLIS = SECONDINMILLIS * 60;
	public static long HOURINMILLIS = MINUTEINMILLIS * 60;
	public static long DAYINMILLIS = HOURINMILLIS * 24;
	public static long YEARINMILLIS = DAYINMILLIS * 365;
	

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
	
	// TODO DD : put in util pkg.
	public static Date getEarliestAssetDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR,2007);
		return calendar.getTime();
	}

	public static Calendar getCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public static Calendar getDelta(Date a, Date b) {		
		long delta = Math.abs(a.getTime()-b.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, (int) (delta/YEARINMILLIS));
		calendar.set(Calendar.DAY_OF_YEAR, (int) (delta/DAYINMILLIS)%365);
		return calendar;
	}
	

	
	
}
