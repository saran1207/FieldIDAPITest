package com.n4systems.util.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public static long SECONDINMILLIS = 1000;
	public static long MINUTEINMILLIS = SECONDINMILLIS * 60;
	public static long HOURINMILLIS = MINUTEINMILLIS * 60;
	public static long DAYINMILLIS = HOURINMILLIS * 24;
	public static long WEEKINMILLIS = DAYINMILLIS * 7;
	public static long YEARINMILLIS = DAYINMILLIS * 365;
	

	public static Calendar getCalendar(Integer year, Integer quarter, Integer month, Integer week, Integer day) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(0);
		setValue(calendar, Calendar.YEAR, year);
		setValue(calendar, Calendar.MONTH, (quarter-1)*3);		
		setValue(calendar, Calendar.MONTH, month>=0 ? month-1 : -1);		
		setValue(calendar, Calendar.WEEK_OF_YEAR, week>=0 ? week+1 : -1);
		setValue(calendar, Calendar.DAY_OF_YEAR, day);
		return calendar;
	}	

	private static void setValue(Calendar calendar, int calendarParam, Integer value) {
		if (value>=0) {
			calendar.set(calendarParam, value.intValue());
		}
	}
	
	public static Date getEarliestFieldIdDate() {
		return getEarliestFieldIdCalendar().getTime();
	}

	public static Calendar getEarliestFieldIdCalendar() {
		Calendar calendar = new GregorianCalendar();
		calendar.clear();
		calendar.set(Calendar.YEAR,2005);
		return calendar;
	}

	public static Calendar getCalendar(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}

	public static Calendar getDelta(Date a, Date b) {		
		long delta = Math.abs(a.getTime()-b.getTime());
		Calendar calendar = new GregorianCalendar();
		calendar.clear();
		calendar.set(Calendar.YEAR, (int) (delta/YEARINMILLIS));
		calendar.set(Calendar.DAY_OF_YEAR, (int) (delta/DAYINMILLIS)%365);
		return calendar;
	}

	public static Calendar getLatestCalendar() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(0);  // in order to make this not fail in tests (i.e. so it doesnt' create dates just a MS or two apart, we force it to return exact same value every time.
		calendar.set(Calendar.YEAR, calendar.getMaximum(Calendar.YEAR));
		return calendar;
	}	

	public static Calendar getMidnightIntance() {		
		Calendar c = new GregorianCalendar();
		c.set(Calendar.HOUR_OF_DAY, 0);  
		c.set(Calendar.MINUTE, 0);  
		c.set(Calendar.SECOND, 0);  
		c.set(Calendar.MILLISECOND, 0);  		
		return c;
	}

	public static long intervalInDays(long delta) {
		return delta/DAYINMILLIS;
	}
	
	public static long intervalInYears(long delta) {
		return delta/YEARINMILLIS;
	}
	
	public static long intervalInWeeks(long delta) {
		return delta/WEEKINMILLIS;
	}

	public static Date getDay(int year, int day) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(0);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_YEAR, day);
		return c.getTime();
	}
	
	public static Long nullSafeMax(Long a, Long b) {
		return a==null ? b : 
			b==null ? a : 
			a.compareTo(b) < 0 ? b : a;  
	}
	
	public static Long nullSafeMin(Long a, Long b) { 
		return a==null ? b : 
			b==null ? a :  
			a.compareTo(b) < 0 ? a : b;  
	}	

	public static Calendar getTimelessIntance() {		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);  
		c.set(Calendar.MINUTE, 0);  
		c.set(Calendar.SECOND, 0);  
		c.set(Calendar.MILLISECOND, 0);  		
		return c;
	}
	
	
	
}
