package com.n4systems.util.chart;

import com.n4systems.fieldid.FieldIdUnitTest;
import com.n4systems.model.utils.DateRange;
import org.hamcrest.collection.IsIn;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.EnumSet;

import static org.junit.Assert.*;


public class DateRangeTest extends FieldIdUnitTest {

	private final LocalDate jan1_2011 = new LocalDate(2011, 1, 1);
	
	@Test
	public void testFromToDelta() {
		// for testing purposes i will set the current time.
		DateTimeUtils.setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
		assertFromToDelta(new DateRange(RangeType.THIS_WEEK), 7);
		assertFromToDelta(new DateRange(RangeType.THIS_MONTH), 28, 29, 30, 31);
		assertFromToDelta(new DateRange(RangeType.THIS_QUARTER), 88, 89, 90, 91, 92, 93);
		assertFromToDelta(new DateRange(RangeType.THIS_YEAR), 365);
        assertFromToDelta(new DateRange(RangeType.TOMORROW), 1);
	}

    @Test
    public void testEnum() {
        // make sure underlying enums are in expected order.
        EnumSet<RangeType> notDaily = EnumSet.range(RangeType.TODAY, RangeType.NEXT_YEAR);
        for (RangeType rangeType:notDaily) {
            assertFalse(rangeType.isDaysFromNowRangeType());
            assertTrue(rangeType.isPredefinedType());
        }

        EnumSet<RangeType> daysFromNow = EnumSet.range(RangeType.SEVEN_DAYS, RangeType.NINETY_DAYS);
        for (RangeType rangeType:daysFromNow) {
            assertFalse(rangeType.isDaily());
            assertTrue(rangeType.isDaysFromNowRangeType());
            assertTrue(rangeType.isPredefinedType());
        }

        EnumSet<RangeType> notPredefinedType = EnumSet.range(RangeType.FOREVER, RangeType.CUSTOM);
        for (RangeType rangeType:notPredefinedType) {
            assertFalse(rangeType.isDaily());
            assertFalse(rangeType.isDaysFromNowRangeType());
            assertFalse(rangeType.isPredefinedType());
        }
    }

	@Test
	public void test_FromDate() {
		setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
		//year
        LocalDate lastYear = new DateRange(RangeType.LAST_YEAR).getFrom();
        assertEquals(2010, lastYear.getYear());
		assertEquals(1, lastYear.getDayOfYear());
		assertEquals(1, lastYear.getMonthOfYear());

        LocalDate thisYear = new DateRange(RangeType.THIS_YEAR).getFrom();
        assertEquals(2011, thisYear.getYear());
        assertEquals(1, thisYear.getDayOfYear());
        assertEquals(1, thisYear.getMonthOfYear());

        LocalDate nextYear = new DateRange(RangeType.NEXT_YEAR).getFrom();
        assertEquals(2012, nextYear.getYear());
        assertEquals(1, nextYear.getDayOfYear());
        assertEquals(1, nextYear.getMonthOfYear());


        //quarter
        LocalDate lastQuarter = new DateRange(RangeType.LAST_QUARTER).getFrom();
        assertEquals(2010, lastQuarter.getYear());
		assertEquals(DateTimeConstants.OCTOBER, lastQuarter.getMonthOfYear());
		assertEquals(1, lastQuarter.getDayOfMonth());

        LocalDate thisQuarter = new DateRange(RangeType.THIS_QUARTER).getFrom();
        assertEquals(2011, thisQuarter.getYear());
		assertEquals(DateTimeConstants.JANUARY, thisQuarter.getMonthOfYear());
		assertEquals(1, thisQuarter.getDayOfYear());

        LocalDate nextQuarter = new DateRange(RangeType.NEXT_QUARTER).getFrom();
        assertEquals(2011, nextQuarter.getYear());
        assertEquals(DateTimeConstants.APRIL, nextQuarter.getMonthOfYear());
        assertEquals(1, nextQuarter.getDayOfMonth());


        //month
        DateRange lastMonth = new DateRange(RangeType.LAST_MONTH);
        assertEquals(DateTimeConstants.DECEMBER, lastMonth.getFrom().getMonthOfYear());
		assertEquals(2010, lastMonth.getFrom().getYear());
		assertEquals(1, lastMonth.getFrom().getDayOfMonth());

        LocalDate thisMonth = new DateRange(RangeType.THIS_MONTH).getFrom();
        assertEquals(DateTimeConstants.JANUARY, thisMonth.getMonthOfYear());
        assertEquals(2011, thisMonth.getYear());
        assertEquals(1, thisMonth.getDayOfMonth());

        LocalDate nextMonth = new DateRange(RangeType.NEXT_MONTH).getFrom();
        assertEquals(DateTimeConstants.FEBRUARY, nextMonth.getMonthOfYear());
        assertEquals(2011, nextMonth.getYear());
        assertEquals(1, nextMonth.getDayOfMonth());


        //week
        LocalDate lastWeek = new DateRange(RangeType.LAST_WEEK).getFrom();
        assertEquals(DateTimeConstants.DECEMBER, lastWeek.getMonthOfYear());
        assertEquals(2010, lastWeek.getYear());
        assertEquals(1, lastWeek.getDayOfWeek());

        LocalDate thisWeek = new DateRange(RangeType.THIS_WEEK).getFrom();
        assertEquals(DateTimeConstants.DECEMBER, thisWeek.getMonthOfYear());
        assertEquals(2010, thisWeek.getYear());
        assertEquals(1, thisWeek.getDayOfWeek());

        LocalDate nextWeek = new DateRange(RangeType.NEXT_WEEK).getFrom();
        assertEquals(DateTimeConstants.JANUARY, nextWeek.getMonthOfYear());
        assertEquals(2011, nextWeek.getYear());
        assertEquals(1, nextWeek.getDayOfWeek());

		//day
        DateRange yesterday = new DateRange(RangeType.YESTERDAY);
        assertEquals(jan1_2011.minusDays(1), yesterday.getFrom());
        assertEquals(jan1_2011, new DateRange(RangeType.TODAY).getFrom());
        assertEquals(jan1_2011.plusDays(1), new DateRange(RangeType.TOMORROW).getFrom());

		// CAVEAT : "this week" for jan 1,2011 actually starts in dec and ends in jan.
		assertEquals(DateTimeConstants.DECEMBER, thisWeek.getMonthOfYear());
		assertEquals(2010, thisWeek.getYear());
		assertEquals(1, thisWeek.getDayOfWeek());
		
		// all time...forever
		assertNull(new DateRange(RangeType.FOREVER).getFrom());
		assertNull(new DateRange(RangeType.CUSTOM).getFrom());
	}

    @Test
	public void test_ToDate() {
		// note that the "to dates" are exclusive.    
		// .: the from & to dates for last month are Dec 1-Jan 1.   (not Dec 1-Dec 31).
		// this is done to facilitate querying and comparing.
		
		setCurrentMillisFixed(jan1_2011.toDate().getTime());
		
		//year
        LocalDate lastYear = new DateRange(RangeType.LAST_YEAR).getTo();
        assertEquals(2011, lastYear.getYear());
		assertEquals(1, lastYear.getDayOfYear());
		assertEquals(1, lastYear.getMonthOfYear());

        LocalDate thisYear = new DateRange(RangeType.THIS_YEAR).getTo();
        assertEquals(2012, thisYear.getYear());
        assertEquals(DateTimeConstants.JANUARY, thisYear.getMonthOfYear());
        assertEquals(1, thisYear.getDayOfYear());

        LocalDate nextYear = new DateRange(RangeType.NEXT_YEAR).getTo();
        assertEquals(2013, nextYear.getYear());
        assertEquals(DateTimeConstants.JANUARY, nextYear.getMonthOfYear());
        assertEquals(1, nextYear.getDayOfYear());


        //quarter
        LocalDate lastQuarter = new DateRange(RangeType.LAST_QUARTER).getTo();
        assertEquals(2011, lastQuarter.getYear());
		assertEquals(DateTimeConstants.JANUARY, lastQuarter.getMonthOfYear());
		assertEquals(1, lastQuarter.getDayOfMonth());

        LocalDate thisQuarter = new DateRange(RangeType.THIS_QUARTER).getTo();
        assertEquals(2011, thisQuarter.getYear());
        assertEquals(DateTimeConstants.APRIL, thisQuarter.getMonthOfYear());
        assertEquals(1, thisQuarter.getDayOfMonth());

        LocalDate nextQuarter = new DateRange(RangeType.NEXT_QUARTER).getTo();
        assertEquals(2011, nextQuarter.getYear());
        assertEquals(DateTimeConstants.JULY, nextQuarter.getMonthOfYear());
        assertEquals(1, nextQuarter.getDayOfMonth());


        //month
        LocalDate lastMonth = new DateRange(RangeType.LAST_MONTH).getTo();
        assertEquals(DateTimeConstants.JANUARY, lastMonth.getMonthOfYear());
		assertEquals(2011, lastMonth.getYear());
		assertEquals(1, lastMonth.getDayOfMonth());

        LocalDate thisMonth = new DateRange(RangeType.THIS_MONTH).getTo();
        assertEquals(DateTimeConstants.FEBRUARY, thisMonth.getMonthOfYear());
        assertEquals(2011, thisMonth.getYear());
        assertEquals(1, thisMonth.getDayOfMonth());

        LocalDate nextMonth = new DateRange(RangeType.NEXT_MONTH).getTo();
        assertEquals(DateTimeConstants.MARCH, nextMonth.getMonthOfYear());
        assertEquals(2011, nextMonth.getYear());
        assertEquals(1, nextMonth.getDayOfMonth());

        //week
        LocalDate lastWeek = new DateRange(RangeType.LAST_WEEK).getTo();
        assertEquals(DateTimeConstants.DECEMBER, lastWeek.getMonthOfYear());
		assertEquals(2010, lastWeek.getYear());
		assertEquals(1, lastWeek.getDayOfWeek());

		// CAVEAT : "this week" for jan 1,2011 actually starts in dec and ends in jan 3.
        LocalDate thisWeek = new DateRange(RangeType.THIS_WEEK).getTo();
        assertEquals(DateTimeConstants.JANUARY, thisWeek.getMonthOfYear());
        assertEquals(2011, thisWeek.getYear());
        assertEquals(1,thisWeek.getWeekOfWeekyear());
        assertEquals(3, thisWeek.getDayOfMonth());
        assertEquals(1, thisWeek.getDayOfWeek());

        // next = jan 3--jan 10
        LocalDate nextWeek = new DateRange(RangeType.NEXT_WEEK).getTo();
        assertEquals(DateTimeConstants.JANUARY, nextWeek.getMonthOfYear());
        assertEquals(2011, nextWeek.getYear());
        assertEquals(2,nextWeek.getWeekOfWeekyear());
        assertEquals(10, nextWeek.getDayOfMonth());
        assertEquals(1, nextWeek.getDayOfWeek());

		// daily
		assertEquals(jan1_2011.plusDays(1), new DateRange(RangeType.TODAY).getTo());
        assertEquals(jan1_2011, new DateRange(RangeType.YESTERDAY).getTo());
        assertEquals(jan1_2011.plusDays(2), new DateRange(RangeType.TOMORROW).getTo());

		// all time...forever
		assertNull(new DateRange(RangeType.FOREVER).getTo());
		assertNull(new DateRange(RangeType.CUSTOM).getTo());
	}	
	
	private void assertFromToDelta(DateRange dateRange, Integer... expected) {
		Days delta = Days.daysBetween(dateRange.getFrom(), dateRange.getTo());
		assertThat(delta.getDays(), new IsIn<Integer>(expected) );		
	}

    // CAVEAT : because the output of the date relies on TimeZone, make sure you have it set correctly.
    //   this works fine in Eclipse & Jenkins but Intellij testsuite runner (not individual run) requires you specifically set it in JVM args.
    // i.e. -Duser.timezone=UTC
	@Test
	public void test_display() {
		setCurrentMillisFixed(jan1_2011.toDate().getTime());

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
