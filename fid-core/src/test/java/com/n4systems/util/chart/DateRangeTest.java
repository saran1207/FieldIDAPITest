package com.n4systems.util.chart;

import static org.junit.Assert.*;

import org.hamcrest.collection.IsIn;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Test;

import com.n4systems.model.utils.DateRange;


public class DateRangeTest {

	private final LocalDate jan1_2011 = new LocalDate(2011, 1, 1);
	
	@Test
	public void testFromToDelta() {
		// for testing purposes i will set the current time.
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
		assertFromToDelta(new DateRange(RangeType.THIS_WEEK), 7);
		assertFromToDelta(new DateRange(RangeType.THIS_MONTH), 28, 29, 30, 31);
		assertFromToDelta(new DateRange(RangeType.THIS_QUARTER), 88, 89, 90, 91, 92, 93);
		assertFromToDelta(new DateRange(RangeType.THIS_YEAR), 365);
	}

	@Test
	public void test_FromDate() {
		// for testing purposes i will set the current time.
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
		//year
		assertEquals(2010, new DateRange(RangeType.LAST_YEAR).getFrom().getYear());
		assertEquals(1, new DateRange(RangeType.LAST_YEAR).getFrom().getDayOfYear());
		assertEquals(1, new DateRange(RangeType.LAST_YEAR).getFrom().getMonthOfYear());
		
		assertEquals(2011, new DateRange(RangeType.THIS_YEAR).getFrom().getYear());
		assertEquals(1, new DateRange(RangeType.THIS_YEAR).getFrom().getDayOfYear());
		assertEquals(1, new DateRange(RangeType.THIS_YEAR).getFrom().getMonthOfYear());
		
		//quarter
		assertEquals(2010, new DateRange(RangeType.LAST_QUARTER).getFrom().getYear());
		assertEquals(DateTimeConstants.OCTOBER, new DateRange(RangeType.LAST_QUARTER).getFrom().getMonthOfYear());
		assertEquals(1, new DateRange(RangeType.LAST_QUARTER).getFrom().getDayOfMonth());
		
		assertEquals(2011, new DateRange(RangeType.THIS_QUARTER).getFrom().getYear());
		assertEquals(DateTimeConstants.JANUARY, new DateRange(RangeType.THIS_QUARTER).getFrom().getMonthOfYear());
		assertEquals(1, new DateRange(RangeType.THIS_QUARTER).getFrom().getDayOfYear());
				
		//month
		assertEquals(DateTimeConstants.DECEMBER, new DateRange(RangeType.LAST_MONTH).getFrom().getMonthOfYear());
		assertEquals(2010, new DateRange(RangeType.LAST_MONTH).getFrom().getYear());
		assertEquals(1, new DateRange(RangeType.LAST_MONTH).getFrom().getDayOfMonth());
		
		assertEquals(DateTimeConstants.JANUARY, new DateRange(RangeType.THIS_MONTH).getFrom().getMonthOfYear());
		assertEquals(2011, new DateRange(RangeType.THIS_MONTH).getFrom().getYear());
		assertEquals(1, new DateRange(RangeType.THIS_MONTH).getFrom().getDayOfMonth());
		
		//week
		assertEquals(DateTimeConstants.DECEMBER, new DateRange(RangeType.LAST_WEEK).getFrom().getMonthOfYear());
		assertEquals(2010, new DateRange(RangeType.LAST_WEEK).getFrom().getYear());
		assertEquals(1, new DateRange(RangeType.LAST_WEEK).getFrom().getDayOfWeek());

		//day
		assertEquals(jan1_2011.minusDays(1), new DateRange(RangeType.YESTERDAY).getFrom());
		assertEquals(jan1_2011, new DateRange(RangeType.TODAY).getFrom());

		// CAVEAT : "this week" for jan 1,2011 actually starts in dec and ends in jan.  
		assertEquals(DateTimeConstants.DECEMBER, new DateRange(RangeType.THIS_WEEK).getFrom().getMonthOfYear());
		assertEquals(2010, new DateRange(RangeType.THIS_WEEK).getFrom().getYear());
		assertEquals(1, new DateRange(RangeType.THIS_WEEK).getFrom().getDayOfWeek());
		
		// all time...forever
		assertNull(new DateRange(RangeType.FOREVER).getFrom());
		assertNull(new DateRange(RangeType.CUSTOM).getFrom());
	}

	@Test
	public void test_ToDate() {
		// note that the "to dates" are exclusive.    
		// .: the from & to dates for last month are Dec 1-Jan 1.   (not Dec 1-Dec 31).
		// this is done to facilitate querying and comparing.
		
		// for testing purposes i will fix the "current time"
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
		//year
		assertEquals(2011, new DateRange(RangeType.LAST_YEAR).getTo().getYear());
		assertEquals(1, new DateRange(RangeType.LAST_YEAR).getTo().getDayOfYear());
		assertEquals(1, new DateRange(RangeType.LAST_YEAR).getTo().getMonthOfYear());
		
		assertEquals(2012, new DateRange(RangeType.THIS_YEAR).getTo().getYear());
		assertEquals(DateTimeConstants.JANUARY, new DateRange(RangeType.THIS_YEAR).getTo().getMonthOfYear());
		assertEquals(1, new DateRange(RangeType.THIS_YEAR).getTo().getDayOfYear());
		
		//quarter
		assertEquals(2011, new DateRange(RangeType.LAST_QUARTER).getTo().getYear());
		assertEquals(DateTimeConstants.JANUARY, new DateRange(RangeType.LAST_QUARTER).getTo().getMonthOfYear());
		assertEquals(1, new DateRange(RangeType.LAST_QUARTER).getTo().getDayOfMonth());
		
		assertEquals(2011, new DateRange(RangeType.THIS_QUARTER).getTo().getYear());
		assertEquals(DateTimeConstants.APRIL, new DateRange(RangeType.THIS_QUARTER).getTo().getMonthOfYear());
		assertEquals(1, new DateRange(RangeType.THIS_QUARTER).getTo().getDayOfMonth());
				
		//month
		assertEquals(DateTimeConstants.JANUARY, new DateRange(RangeType.LAST_MONTH).getTo().getMonthOfYear());
		assertEquals(2011, new DateRange(RangeType.LAST_MONTH).getTo().getYear());
		assertEquals(1, new DateRange(RangeType.LAST_MONTH).getTo().getDayOfMonth());
		
		assertEquals(DateTimeConstants.FEBRUARY, new DateRange(RangeType.THIS_MONTH).getTo().getMonthOfYear());
		assertEquals(2011, new DateRange(RangeType.THIS_MONTH).getTo().getYear());
		assertEquals(1, new DateRange(RangeType.THIS_MONTH).getTo().getDayOfMonth());
		
		//week
		assertEquals(DateTimeConstants.DECEMBER, new DateRange(RangeType.LAST_WEEK).getTo().getMonthOfYear());
		assertEquals(2010, new DateRange(RangeType.LAST_WEEK).getTo().getYear());
		assertEquals(1, new DateRange(RangeType.LAST_WEEK).getTo().getDayOfWeek());

		// CAVEAT : "this week" for jan 1,2011 actually starts in dec and ends in jan.  
		assertEquals(DateTimeConstants.JANUARY, new DateRange(RangeType.THIS_WEEK).getTo().getMonthOfYear());
		assertEquals(2011, new DateRange(RangeType.THIS_WEEK).getTo().getYear());
		assertEquals(1, new DateRange(RangeType.THIS_WEEK).getTo().getDayOfWeek());

		// daily
		assertEquals(jan1_2011.plusDays(1), new DateRange(RangeType.TODAY).getTo());
		assertEquals(jan1_2011, new DateRange(RangeType.YESTERDAY).getTo());
		
		// all time...forever
		assertNull(new DateRange(RangeType.FOREVER).getTo());
		assertNull(new DateRange(RangeType.CUSTOM).getTo());
	}	
	
	private void assertFromToDelta(DateRange dateRange, Integer... expected) {
		Days delta = Days.daysBetween(dateRange.getFrom(), dateRange.getTo());
		assertThat(delta.getDays(), new IsIn<Integer>(expected) );		
	}

	@Test
	public void test_display() { 
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
		assertEquals("All Time", new DateRange(RangeType.FOREVER).getFromDateDisplayString() );
		
		assertEquals("2010", new DateRange(RangeType.LAST_YEAR).getFromDateDisplayString() );
		assertEquals("2011", new DateRange(RangeType.THIS_YEAR).getFromDateDisplayString() );
		
		assertEquals("Jan", new DateRange(RangeType.THIS_QUARTER).getFromDateDisplayString() );
		assertEquals("Oct", new DateRange(RangeType.LAST_QUARTER).getFromDateDisplayString() );

		assertEquals("Dec 2010", new DateRange(RangeType.LAST_MONTH).getFromDateDisplayString() );
		assertEquals("Jan 2011", new DateRange(RangeType.THIS_MONTH).getFromDateDisplayString() );
		
		assertEquals("Dec 20", new DateRange(RangeType.LAST_WEEK).getFromDateDisplayString() );
		assertEquals("Dec 27", new DateRange(RangeType.THIS_WEEK).getFromDateDisplayString() );

		
		assertEquals("", new DateRange(RangeType.FOREVER).getToDateDisplayString() );
		
		assertEquals("2010", new DateRange(RangeType.LAST_YEAR).getToDateDisplayString() );
		assertEquals("2011", new DateRange(RangeType.THIS_YEAR).getToDateDisplayString() );
		
		assertEquals("Mar 2011", new DateRange(RangeType.THIS_QUARTER).getToDateDisplayString() );
		assertEquals("Dec 2010", new DateRange(RangeType.LAST_QUARTER).getToDateDisplayString() );

		assertEquals("Dec 2010", new DateRange(RangeType.LAST_MONTH).getToDateDisplayString() );
		assertEquals("Jan 2011", new DateRange(RangeType.THIS_MONTH).getToDateDisplayString() );
		
		assertEquals("Dec 26", new DateRange(RangeType.LAST_WEEK).getToDateDisplayString() );
		assertEquals("Jan 2", new DateRange(RangeType.THIS_WEEK).getToDateDisplayString() );
		
		assertEquals("Jan 1", new DateRange(RangeType.TODAY).getFromDateDisplayString());
		assertEquals("Dec 31", new DateRange(RangeType.YESTERDAY).getFromDateDisplayString());
				
	}
	
}
