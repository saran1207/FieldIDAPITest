package com.n4systems.model.common;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class SimpleFrequencyTest {
	private Calendar cal;
	
	@Before
	public void setup() {
		cal = Calendar.getInstance();
		
		// specifically picking a leap month in leap year
		cal.set(2008, Calendar.FEBRUARY, 1);
	}
	
	@Test
	public void test_daily_same_day_always_returns_true() {
		SimpleFrequency freq = SimpleFrequency.DAILY;

		// for each day of the year, make sure it we get a true back
		for (int day = 1; day < cal.getActualMaximum(Calendar.DAY_OF_YEAR); day++) {
			cal.set(Calendar.DAY_OF_YEAR, day);
			
			assertTrue(freq.isSameDay(cal.getTime()));
		}
	}

	@Test
	public void test_weekly_same_day() {
		SimpleFrequency[] freqDays = { SimpleFrequency.WEEKLY_SUNDAY, SimpleFrequency.WEEKLY_MONDAY, SimpleFrequency.WEEKLY_TUESDAY, SimpleFrequency.WEEKLY_WEDNESDAY, SimpleFrequency.WEEKLY_THURSDAY, SimpleFrequency.WEEKLY_FRIDAY, SimpleFrequency.WEEKLY_SATURDAY };
		int[] calDays = { Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY };
		
		int freqDayIndex = 1;
		boolean sameDay;
		String assertMessage;
		
		// this will test that for each weekly frequency day, and each calendar week day, we get a true on the same days and false otherwise
		for (SimpleFrequency freqDay: freqDays) {
			
			for (int calDay: calDays) {
				cal.set(Calendar.DAY_OF_WEEK, calDay);
				
				sameDay = freqDay.isSameDay(cal.getTime());
				assertMessage = "Freq Day: " + freqDay.name() + ", Test Day: " + calDay + ", Same Day: " + sameDay;
				
				// if the freq day index and test day are the same, then we should get a true back (they're both indexed at 1)
				if (freqDayIndex == calDay) {
					assertTrue(assertMessage, sameDay);
				} else {
					assertFalse(assertMessage, sameDay);
				}
			}
			
			freqDayIndex++;
		}
	}

	@Test
	public void test_monthly_same_day() {
		String assertMessage;
		for (int dayInMonth = 1; dayInMonth < 29; dayInMonth++) {
			cal.set(Calendar.DAY_OF_MONTH, dayInMonth);
			
			assertMessage = "Test Day: " + dayInMonth;
			
			if (dayInMonth == 1) {
				assertTrue(assertMessage, SimpleFrequency.MONTHLY_FIRST.isSameDay(cal.getTime()));
				assertFalse(assertMessage, SimpleFrequency.MONTHLY_15TH.isSameDay(cal.getTime()));
				assertFalse(assertMessage, SimpleFrequency.MONTHLY_LAST.isSameDay(cal.getTime()));
			} else if (dayInMonth == 15) {
				assertFalse(assertMessage, SimpleFrequency.MONTHLY_FIRST.isSameDay(cal.getTime()));
				assertTrue(assertMessage, SimpleFrequency.MONTHLY_15TH.isSameDay(cal.getTime()));
				assertFalse(assertMessage, SimpleFrequency.MONTHLY_LAST.isSameDay(cal.getTime()));
			} else if (dayInMonth == 29) {
				assertFalse(assertMessage, SimpleFrequency.MONTHLY_FIRST.isSameDay(cal.getTime()));
				assertFalse(assertMessage, SimpleFrequency.MONTHLY_15TH.isSameDay(cal.getTime()));
				assertTrue(assertMessage, SimpleFrequency.MONTHLY_LAST.isSameDay(cal.getTime()));
			} else {
				assertFalse(assertMessage, SimpleFrequency.MONTHLY_FIRST.isSameDay(cal.getTime()));
				assertFalse(assertMessage, SimpleFrequency.MONTHLY_15TH.isSameDay(cal.getTime()));
				assertFalse(assertMessage, SimpleFrequency.MONTHLY_LAST.isSameDay(cal.getTime()));
			}
		}
		
	}
}
