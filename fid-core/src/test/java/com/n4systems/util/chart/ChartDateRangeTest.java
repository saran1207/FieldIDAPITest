package com.n4systems.util.chart;

import static org.junit.Assert.*;

import org.hamcrest.collection.IsIn;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Test;


public class ChartDateRangeTest {

	private final LocalDate jan1_2011 = new LocalDate(2011, 1, 1);
	
	@Test
	public void testFromToDelta() {
		assertFromToDelta(ChartDateRange.THIS_WEEK, 7);
		assertFromToDelta(ChartDateRange.THIS_MONTH, 28, 29, 30, 31);
		assertFromToDelta(ChartDateRange.THIS_QUARTER, 88, 89, 90, 91, 92, 93);
		assertFromToDelta(ChartDateRange.THIS_YEAR, 365);
	}

	@Test
	public void test_FromDate() {
		// for testing purposes i will set the current time.
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
		//year
		assertEquals(2010, ChartDateRange.LAST_YEAR.getFrom().getYear());
		assertEquals(1, ChartDateRange.LAST_YEAR.getFrom().getDayOfYear());
		assertEquals(1, ChartDateRange.LAST_YEAR.getFrom().getMonthOfYear());
		
		assertEquals(2011, ChartDateRange.THIS_YEAR.getFrom().getYear());		
		assertEquals(1, ChartDateRange.THIS_YEAR.getFrom().getDayOfYear());
		assertEquals(1, ChartDateRange.THIS_YEAR.getFrom().getMonthOfYear());
		
		//quarter
		assertEquals(2010, ChartDateRange.LAST_QUARTER.getFrom().getYear());
		assertEquals(DateTimeConstants.OCTOBER, ChartDateRange.LAST_QUARTER.getFrom().getMonthOfYear());
		assertEquals(1, ChartDateRange.LAST_QUARTER.getFrom().getDayOfMonth());
		
		assertEquals(2011, ChartDateRange.THIS_QUARTER.getFrom().getYear());		
		assertEquals(DateTimeConstants.JANUARY, ChartDateRange.THIS_QUARTER.getFrom().getMonthOfYear());
		assertEquals(1, ChartDateRange.THIS_QUARTER.getFrom().getDayOfYear());
				
		//month
		assertEquals(DateTimeConstants.DECEMBER, ChartDateRange.LAST_MONTH.getFrom().getMonthOfYear());		
		assertEquals(2010, ChartDateRange.LAST_MONTH.getFrom().getYear());
		assertEquals(1, ChartDateRange.LAST_MONTH.getFrom().getDayOfMonth());
		
		assertEquals(DateTimeConstants.JANUARY, ChartDateRange.THIS_MONTH.getFrom().getMonthOfYear());		
		assertEquals(2011, ChartDateRange.THIS_MONTH.getFrom().getYear());
		assertEquals(1, ChartDateRange.THIS_MONTH.getFrom().getDayOfMonth());
		
		//week
		assertEquals(DateTimeConstants.DECEMBER, ChartDateRange.LAST_WEEK.getFrom().getMonthOfYear());		
		assertEquals(2010, ChartDateRange.LAST_WEEK.getFrom().getYear());
		assertEquals(1, ChartDateRange.LAST_WEEK.getFrom().getDayOfWeek());

		// CAVEAT : "this week" for jan 1,2011 actually starts in dec and ends in jan.  
		assertEquals(DateTimeConstants.DECEMBER, ChartDateRange.THIS_WEEK.getFrom().getMonthOfYear());		
		assertEquals(2010, ChartDateRange.THIS_WEEK.getFrom().getYear());
		assertEquals(1, ChartDateRange.THIS_WEEK.getFrom().getDayOfWeek());
		
		// all time...forever
		assertEquals(DateTimeConstants.JANUARY, ChartDateRange.FOREVER.getFrom().getMonthOfYear());		
		assertTrue(ChartDateRange.FOREVER.getFrom().getYear()>=2005);
		assertEquals(1, ChartDateRange.FOREVER.getFrom().getDayOfYear());
	}

	@Test
	public void test_ToDate() {
		// note that the "to dates" are exclusive.    
		// .: the from & to dates for last month are Dec 1-Jan 1.   (not Dec 1-Dec 31).
		// this is done to facilitate querying and comparing.
		
		// for testing purposes i will fix the "current time"
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
		//year
		assertEquals(2011, ChartDateRange.LAST_YEAR.getTo().getYear());
		assertEquals(1, ChartDateRange.LAST_YEAR.getTo().getDayOfYear());
		assertEquals(1, ChartDateRange.LAST_YEAR.getTo().getMonthOfYear());
		
		assertEquals(2012, ChartDateRange.THIS_YEAR.getTo().getYear());		
		assertEquals(DateTimeConstants.JANUARY, ChartDateRange.THIS_YEAR.getTo().getMonthOfYear());
		assertEquals(1, ChartDateRange.THIS_YEAR.getTo().getDayOfYear());
		
		//quarter
		assertEquals(2011, ChartDateRange.LAST_QUARTER.getTo().getYear());
		assertEquals(DateTimeConstants.JANUARY, ChartDateRange.LAST_QUARTER.getTo().getMonthOfYear());
		assertEquals(1, ChartDateRange.LAST_QUARTER.getTo().getDayOfMonth());
		
		assertEquals(2011, ChartDateRange.THIS_QUARTER.getTo().getYear());		
		assertEquals(DateTimeConstants.APRIL, ChartDateRange.THIS_QUARTER.getTo().getMonthOfYear());
		assertEquals(1, ChartDateRange.THIS_QUARTER.getTo().getDayOfMonth());
				
		//month
		assertEquals(DateTimeConstants.JANUARY, ChartDateRange.LAST_MONTH.getTo().getMonthOfYear());		
		assertEquals(2011, ChartDateRange.LAST_MONTH.getTo().getYear());
		assertEquals(1, ChartDateRange.LAST_MONTH.getTo().getDayOfMonth());
		
		assertEquals(DateTimeConstants.FEBRUARY, ChartDateRange.THIS_MONTH.getTo().getMonthOfYear());		
		assertEquals(2011, ChartDateRange.THIS_MONTH.getTo().getYear());
		assertEquals(1, ChartDateRange.THIS_MONTH.getTo().getDayOfMonth());
		
		//week
		assertEquals(DateTimeConstants.DECEMBER, ChartDateRange.LAST_WEEK.getTo().getMonthOfYear());		
		assertEquals(2010, ChartDateRange.LAST_WEEK.getTo().getYear());
		assertEquals(1, ChartDateRange.LAST_WEEK.getTo().getDayOfWeek());

		// CAVEAT : "this week" for jan 1,2011 actually starts in dec and ends in jan.  
		assertEquals(DateTimeConstants.JANUARY, ChartDateRange.THIS_WEEK.getTo().getMonthOfYear());		
		assertEquals(2011, ChartDateRange.THIS_WEEK.getTo().getYear());
		assertEquals(1, ChartDateRange.THIS_WEEK.getTo().getDayOfWeek());
		
		// all time...forever
		assertEquals(DateTimeConstants.JANUARY, ChartDateRange.FOREVER.getTo().getMonthOfYear());		
		assertTrue(ChartDateRange.FOREVER.getTo().getYear()>=2005);
		assertEquals(1, ChartDateRange.FOREVER.getTo().getDayOfYear());
	}	
	
	private void assertFromToDelta(ChartDateRange range, Integer... expected) {
		Days delta = Days.daysBetween(range.getFrom(), range.getTo());
		assertThat(delta.getDays(), new IsIn<Integer>(expected) );		
	}

	@Test
	public void test_display() { 
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
		assertEquals("All Time", ChartDateRange.FOREVER.getFromDateDisplayString() );
		
		assertEquals("2010", ChartDateRange.LAST_YEAR.getFromDateDisplayString() );
		assertEquals("2011", ChartDateRange.THIS_YEAR.getFromDateDisplayString() );
		
		assertEquals("Jan", ChartDateRange.THIS_QUARTER.getFromDateDisplayString() );
		assertEquals("Oct", ChartDateRange.LAST_QUARTER.getFromDateDisplayString() );

		assertEquals("Dec 2010", ChartDateRange.LAST_MONTH.getFromDateDisplayString() );
		assertEquals("Jan 2011", ChartDateRange.THIS_MONTH.getFromDateDisplayString() );
		
		assertEquals("Dec 20", ChartDateRange.LAST_WEEK.getFromDateDisplayString() );
		assertEquals("Dec 27", ChartDateRange.THIS_WEEK.getFromDateDisplayString() );

		
		System.out.println( ChartDateRange.FOREVER.getToDateDisplayString() );
		
		System.out.println( ChartDateRange.LAST_YEAR.getToDateDisplayString() );
		System.out.println( ChartDateRange.THIS_YEAR.getToDateDisplayString() );
		
		System.out.println( ChartDateRange.THIS_QUARTER.getToDateDisplayString() );
		System.out.println( ChartDateRange.LAST_QUARTER.getToDateDisplayString() );

		System.out.println( ChartDateRange.LAST_MONTH.getToDateDisplayString() );
		System.out.println( ChartDateRange.THIS_MONTH.getToDateDisplayString() );
		
		System.out.println( ChartDateRange.LAST_WEEK.getToDateDisplayString() );
		System.out.println( ChartDateRange.THIS_WEEK.getToDateDisplayString() );
		
	}
	
}
