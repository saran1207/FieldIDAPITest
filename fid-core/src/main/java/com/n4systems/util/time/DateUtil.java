package com.n4systems.util.time;

import java.util.Date;

import org.joda.time.LocalDate;

public class DateUtil {

	public static long SECONDINMILLIS = 1000;
	public static long MINUTEINMILLIS = SECONDINMILLIS * 60;
	public static long HOURINMILLIS = MINUTEINMILLIS * 60;
	public static long DAYINMILLIS = HOURINMILLIS * 24;
	public static long WEEKINMILLIS = DAYINMILLIS * 7;
	public static long YEARINMILLIS = DAYINMILLIS * 365;
	

	public static LocalDate getLocalDate(Integer year, Integer month, Integer day) {
		LocalDate date = new LocalDate(year, month, day);
		return date;
	}	

	public static LocalDate getEarliestFieldIdDate() {
		return new LocalDate().withYear(2005).withDayOfYear(1);
	}

	public static LocalDate getLatestFieldIdDate() {
		return new LocalDate().withYear(3000).withDayOfYear(1);
		// this doesn't work. bug in JODA.  setting to arbitrarily distant date.
		//return new LocalDate(Years.MAX_VALUE.getYears(), 1, 1);
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

	public static int getMonthInQuarter(LocalDate date) {
		// returns 1..3  e.g. getMonthInQuarter(January)=1, Feb=2 etc..     
		return (date.getMonthOfYear()-1)%3 + 1;
	}

	public static int getQuarter(LocalDate date) {
		return (date.getMonthOfYear()-1)/3 + 1;
	}

	public static int getQuarterMonth(LocalDate date) {
		// e.g. for Feb will return Jan because Jan is the month that the quarter starts.
		return (date.getMonthOfYear()-1)/3 * 3 + 1;
	}

	public static Date createLocalDate(LocalDate date) {
		return date==null ? null : date.toDate();
	}
	
}
