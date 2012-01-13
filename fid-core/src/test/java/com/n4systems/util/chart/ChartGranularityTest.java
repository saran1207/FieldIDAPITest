package com.n4systems.util.chart;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Years;
import org.junit.Test;


public class ChartGranularityTest {
	
	private final LocalDate jan1 = new LocalDate(2011, 1,1);
	private final LocalDate jan2 = new LocalDate(2011, 1,2);
	private final LocalDate jan3 = new LocalDate(2011, 1,3);
	private final LocalDate jan12 = new LocalDate(2011, 1,12);
	private final LocalDate feb1 = new LocalDate(2011, 2,1);
	private final LocalDate mar10 = new LocalDate(2011, 3,10);
	private final LocalDate aug25 = new LocalDate(2011, 8,25);
	private final LocalDate oct1 = new LocalDate(2011, 10,1);
	private final LocalDate oct1_2012 = new LocalDate(2012, 10,1);
	private final LocalDate dec31 = new LocalDate(2011, 12,31);
	
	@Test 
	public void test_preferredRange() { 
		LocalDate now = LocalDate.now();
		
		LocalDate date = now.plus(ChartGranularity.DAY.preferredRange());
		assertEquals(30, Days.daysBetween(now, date).getDays());

		date = now.plus(ChartGranularity.WEEK.preferredRange());
		assertEquals(12, Months.monthsBetween(now, date).getMonths());	
		
		date = now.plus(ChartGranularity.MONTH.preferredRange());
		assertEquals(36, Months.monthsBetween(now, date).getMonths());
		assertTrue(Days.daysBetween(now, date).getDays()>=3*365);
	
		date = now.plus(ChartGranularity.QUARTER.preferredRange());
		assertEquals(7, Years.yearsBetween(now, date).getYears());
	
		date = now.plus(ChartGranularity.YEAR.preferredRange());
		assertEquals(10, Years.yearsBetween(now, date).getYears());		
	}

	@Test
	public void test_compare() {				
		ChartGranularity yearGranularity = ChartGranularity.YEAR;
		assertEquals(0, yearGranularity.compare(jan1, feb1));
		assertTrue(yearGranularity.compare(jan1, oct1_2012)<0);
		assertTrue(yearGranularity.compare(oct1_2012, jan1)>0);
		
		ChartGranularity quarterGranularity = ChartGranularity.QUARTER;
		assertEquals(0, quarterGranularity.compare(jan1, feb1));
		assertEquals(0, quarterGranularity.compare(jan1, mar10));
		assertTrue(quarterGranularity.compare(jan1, oct1)<0);
		assertTrue(quarterGranularity.compare(jan1, oct1_2012)<0);
		assertTrue(quarterGranularity.compare(oct1,jan1)>0);
		assertTrue(quarterGranularity.compare(oct1_2012,jan1)>0);
		
		ChartGranularity monthGranularity = ChartGranularity.MONTH;
		assertTrue(monthGranularity.compare(jan1, feb1)<0);
		assertTrue(monthGranularity.compare(jan1, jan12)==0);
		assertTrue(monthGranularity.compare(jan1, oct1)<0);
		assertTrue(monthGranularity.compare(jan1, oct1_2012)<0);
		assertTrue(monthGranularity.compare(oct1,jan1)>0);
		assertTrue(monthGranularity.compare(oct1_2012,jan1)>0);
		
		ChartGranularity weekGranularity = ChartGranularity.WEEK;
		assertTrue(weekGranularity.compare(jan1, feb1)<0);
		assertTrue(weekGranularity.compare(jan1, jan2)==0);
		assertTrue(weekGranularity.compare(jan1, oct1)<0);
		assertTrue(weekGranularity.compare(jan1, oct1_2012)<0);
		assertTrue(weekGranularity.compare(oct1,jan1)>0);

		ChartGranularity dayGranularity = ChartGranularity.DAY;
		assertTrue(dayGranularity.compare(jan1, feb1)<0);
		assertTrue(dayGranularity.compare(jan1, jan3)<0);
		assertTrue(dayGranularity.compare(jan1, oct1)<0);
		assertTrue(dayGranularity.compare(jan1, oct1_2012)<0);
		assertTrue(dayGranularity.compare(oct1,jan1)>0);
	
	}

	@Test
	public void test_next() {
		ChartGranularity yearGranularity = ChartGranularity.YEAR;
		assertEquals(2012, yearGranularity.next(jan1).getYear());
		assertEquals(2013, yearGranularity.next(oct1_2012).getYear());
		
		ChartGranularity quarterGranularity = ChartGranularity.QUARTER;
		assertEquals(DateTimeConstants.APRIL, quarterGranularity.next(jan1).getMonthOfYear());
		assertEquals(DateTimeConstants.JANUARY, quarterGranularity.next(oct1_2012).getMonthOfYear());
		assertEquals(DateTimeConstants.APRIL, quarterGranularity.next(feb1).getMonthOfYear());
		assertEquals(DateTimeConstants.APRIL, quarterGranularity.next(mar10).getMonthOfYear());
		assertEquals(DateTimeConstants.OCTOBER, quarterGranularity.next(aug25).getMonthOfYear());
		
		ChartGranularity monthGranularity = ChartGranularity.MONTH;
		assertEquals(DateTimeConstants.FEBRUARY, monthGranularity.next(jan1).getMonthOfYear());
		assertEquals(DateTimeConstants.NOVEMBER, monthGranularity.next(oct1_2012).getMonthOfYear());
		assertEquals(DateTimeConstants.MARCH, monthGranularity.next(feb1).getMonthOfYear());
		assertEquals(DateTimeConstants.APRIL, monthGranularity.next(mar10).getMonthOfYear());
		assertEquals(DateTimeConstants.SEPTEMBER, monthGranularity.next(aug25).getMonthOfYear());
		
		ChartGranularity weekGranularity = ChartGranularity.WEEK;
		assertEquals(1, weekGranularity.next(jan1).getWeekOfWeekyear());
		assertEquals(6, weekGranularity.next(feb1).getWeekOfWeekyear());
		assertEquals(3, weekGranularity.next(jan12).getWeekOfWeekyear());

		ChartGranularity dayGranularity = ChartGranularity.DAY;
		assertEquals(2, dayGranularity.next(jan1).getDayOfYear());
		assertEquals(33, dayGranularity.next(feb1).getDayOfYear());
		assertEquals(13, dayGranularity.next(jan12).getDayOfYear());
		
	}
	
	
	@Test
	public void test_previous() {
		ChartGranularity yearGranularity = ChartGranularity.YEAR;
		assertEquals(2010, yearGranularity.previous(jan1).getYear());
		assertEquals(2011, yearGranularity.previous(oct1_2012).getYear());
		
		ChartGranularity quarterGranularity = ChartGranularity.QUARTER;
		assertEquals(DateTimeConstants.OCTOBER, quarterGranularity.previous(jan1).getMonthOfYear());
		assertEquals(DateTimeConstants.JULY, quarterGranularity.previous(oct1_2012).getMonthOfYear());
		assertEquals(DateTimeConstants.OCTOBER, quarterGranularity.previous(feb1).getMonthOfYear());
		assertEquals(DateTimeConstants.OCTOBER, quarterGranularity.previous(mar10).getMonthOfYear());
		assertEquals(DateTimeConstants.APRIL, quarterGranularity.previous(aug25).getMonthOfYear());
		
		ChartGranularity monthGranularity = ChartGranularity.MONTH;
		assertEquals(DateTimeConstants.DECEMBER, monthGranularity.previous(jan1).getMonthOfYear());
		assertEquals(DateTimeConstants.SEPTEMBER, monthGranularity.previous(oct1_2012).getMonthOfYear());
		assertEquals(DateTimeConstants.JANUARY, monthGranularity.previous(feb1).getMonthOfYear());
		assertEquals(DateTimeConstants.FEBRUARY, monthGranularity.previous(mar10).getMonthOfYear());
		assertEquals(DateTimeConstants.JULY, monthGranularity.previous(aug25).getMonthOfYear());

		
		ChartGranularity weekGranularity = ChartGranularity.WEEK;
		// CAVEAT :  see joda documentation for definition of week.  the important part is that ISO specifies 
		// the possibility for days in january to be part of previous years week.  (i.e. first few days of january are not guaranteed to have a 
		//   weekOfYear value of 1.  
		
//		System.out.println(jan1.getYear());
//		System.out.println(jan1.getMonthOfYear());
//		System.out.println(jan1.getDayOfYear());
//		System.out.println(jan1.getWeekOfWeekyear());      prints out [2011, 1, 1, 52]    NOT   [2011, 1, 1, 1] as you might expect.
		
		assertEquals(51, weekGranularity.previous(jan1).getWeekOfWeekyear());
		assertEquals(4, weekGranularity.previous(feb1).getWeekOfWeekyear());
		assertEquals(1, weekGranularity.previous(jan12).getWeekOfWeekyear());

		ChartGranularity dayGranularity = ChartGranularity.DAY;
		assertEquals(365, dayGranularity.previous(jan1).getDayOfYear());
		assertEquals(31, dayGranularity.previous(feb1).getDayOfYear());
		assertEquals(11, dayGranularity.previous(jan12).getDayOfYear());		
	}
	
	@Test
	public void test_normalize() { 
		ChartGranularity yearGranularity = ChartGranularity.YEAR;
		assertEquals(DateTimeConstants.JANUARY, yearGranularity.normalize(feb1).getMonthOfYear());
		assertEquals(2011, yearGranularity.normalize(feb1).getYear());
		assertEquals(1, yearGranularity.normalize(feb1).getDayOfMonth());
		assertEquals(DateTimeConstants.JANUARY, yearGranularity.normalize(jan1).getMonthOfYear());
		assertEquals(2011, yearGranularity.normalize(jan1).getYear());
		assertEquals(1, yearGranularity.normalize(jan1).getDayOfMonth());
		assertEquals(DateTimeConstants.JANUARY, yearGranularity.normalize(dec31).getMonthOfYear());
		assertEquals(2011, yearGranularity.normalize(dec31).getYear());
		assertEquals(1, yearGranularity.normalize(dec31).getDayOfMonth());

		
		ChartGranularity quarterGranularity = ChartGranularity.QUARTER;
		assertEquals(DateTimeConstants.JANUARY, quarterGranularity.normalize(feb1).getMonthOfYear());
		assertEquals(2011, quarterGranularity.normalize(feb1).getYear());
		assertEquals(1, quarterGranularity.normalize(feb1).getDayOfMonth());
		assertEquals(DateTimeConstants.JULY, quarterGranularity.normalize(aug25).getMonthOfYear());
		assertEquals(2011, quarterGranularity.normalize(aug25).getYear());
		assertEquals(1, quarterGranularity.normalize(aug25).getDayOfMonth());
		assertEquals(DateTimeConstants.OCTOBER, quarterGranularity.normalize(dec31).getMonthOfYear());
		assertEquals(2011, quarterGranularity.normalize(dec31).getYear());
		assertEquals(1, quarterGranularity.normalize(dec31).getDayOfMonth());		

		ChartGranularity monthGranularity = ChartGranularity.MONTH;
		assertEquals(DateTimeConstants.FEBRUARY, monthGranularity.normalize(feb1).getMonthOfYear());
		assertEquals(2011, monthGranularity.normalize(feb1).getYear());
		assertEquals(1, monthGranularity.normalize(feb1).getDayOfMonth());
		assertEquals(DateTimeConstants.AUGUST, monthGranularity.normalize(aug25).getMonthOfYear());
		assertEquals(2011, monthGranularity.normalize(aug25).getYear());
		assertEquals(1, monthGranularity.normalize(aug25).getDayOfMonth());
		assertEquals(DateTimeConstants.DECEMBER, monthGranularity.normalize(dec31).getMonthOfYear());
		assertEquals(2011, monthGranularity.normalize(dec31).getYear());
		assertEquals(1, monthGranularity.normalize(dec31).getDayOfMonth());		
		

		// CAVEAT :  see joda documentation for definition of week.  the important part is that ISO specifies 
		// the possibility for days in january to be part of previous years week.  (i.e. first few days of january are not guaranteed to have a 
		//   weekOfYear value of 1.  
		
		ChartGranularity weekGranularity = ChartGranularity.WEEK;
		assertEquals(DateTimeConstants.JANUARY, weekGranularity.normalize(feb1).getMonthOfYear());
		// note that the week starts on monday jan 31st and goes thru sun feb 6th.
		assertEquals(2011, weekGranularity.normalize(feb1).getYear());
		assertEquals(31, weekGranularity.normalize(feb1).getDayOfMonth());
		assertEquals(1, weekGranularity.normalize(feb1).getDayOfWeek());
		assertEquals(DateTimeConstants.AUGUST, weekGranularity.normalize(aug25).getMonthOfYear());
		assertEquals(2011, weekGranularity.normalize(aug25).getYear());
		assertEquals(22, weekGranularity.normalize(aug25).getDayOfMonth());
		assertEquals(DateTimeConstants.DECEMBER, weekGranularity.normalize(dec31).getMonthOfYear());
		assertEquals(2011, weekGranularity.normalize(dec31).getYear());
		assertEquals(26, weekGranularity.normalize(dec31).getDayOfMonth());		
		
		ChartGranularity dayGranularity = ChartGranularity.DAY;
		assertEquals(DateTimeConstants.FEBRUARY, dayGranularity.normalize(feb1).getMonthOfYear());
		assertEquals(2011, dayGranularity.normalize(feb1).getYear());
		assertEquals(1, dayGranularity.normalize(feb1).getDayOfMonth());
		assertEquals(DateTimeConstants.AUGUST, dayGranularity.normalize(aug25).getMonthOfYear());
		assertEquals(2011, dayGranularity.normalize(aug25).getYear());
		assertEquals(25, dayGranularity.normalize(aug25).getDayOfMonth());
		assertEquals(DateTimeConstants.DECEMBER, dayGranularity.normalize(dec31).getMonthOfYear());
		assertEquals(2011, dayGranularity.normalize(dec31).getYear());
		assertEquals(31, dayGranularity.normalize(dec31).getDayOfMonth());		
	}
	
	@Test
	public void test_round() { 
		ChartGranularity weekGranularity = ChartGranularity.WEEK;
		LocalDate jan1Down = weekGranularity.roundDown(jan1);
		LocalDate jan1Up = weekGranularity.roundUp(jan1);
		
		assertEquals(27, jan1Down.getDayOfMonth() );
		assertEquals(DateTimeConstants.DECEMBER, jan1Down.getMonthOfYear() );
		assertEquals(3, jan1Up.getDayOfMonth() );
		assertEquals(DateTimeConstants.JANUARY, jan1Up.getMonthOfYear() );

		// because this is on a monday (i.e. first day of week, it requires no rounding.
		LocalDate jan3Down = weekGranularity.roundDown(jan3);
		LocalDate jan3Up = weekGranularity.roundUp(jan3);
		
		assertEquals(3, jan3Down.getDayOfMonth() );
		assertEquals(DateTimeConstants.JANUARY, jan3Down.getMonthOfYear() );
		assertEquals(3, jan3Up.getDayOfMonth() );
		assertEquals(DateTimeConstants.JANUARY, jan3Up.getMonthOfYear() );

		LocalDate oct1Down = weekGranularity.roundDown(oct1);
		LocalDate oct1Up = weekGranularity.roundUp(oct1);
		
		assertEquals(26, oct1Down.getDayOfMonth() );
		assertEquals(DateTimeConstants.SEPTEMBER, oct1Down.getMonthOfYear() );
		assertEquals(3, oct1Up.getDayOfMonth() );
		assertEquals(DateTimeConstants.OCTOBER, oct1Up.getMonthOfYear() );		
	}

	@Test
	public void test_finerCoarser() { 
		assertEquals(ChartGranularity.QUARTER, ChartGranularity.YEAR.finer());
		assertEquals(ChartGranularity.MONTH, ChartGranularity.QUARTER.finer());
		assertEquals(ChartGranularity.WEEK, ChartGranularity.MONTH.finer());
		assertEquals(ChartGranularity.DAY, ChartGranularity.WEEK.finer());
		assertEquals(null, ChartGranularity.DAY.finer());

		assertEquals(null, ChartGranularity.YEAR.coarser());
		assertEquals(ChartGranularity.YEAR, ChartGranularity.QUARTER.coarser());
		assertEquals(ChartGranularity.QUARTER, ChartGranularity.MONTH.coarser());
		assertEquals(ChartGranularity.MONTH, ChartGranularity.WEEK.coarser());
		assertEquals(ChartGranularity.WEEK, ChartGranularity.DAY.coarser());
	}
	
}
