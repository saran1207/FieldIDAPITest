package com.n4systems.util.time;

import com.n4systems.model.utils.DateRange;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


public class DateUtilTest {

	private LocalDate jan1_2011 = new LocalDate(2011, 1,1);
	private LocalDate jan12_2011 = new LocalDate(2011, 1,12);
	private LocalDate feb1_2011 = new LocalDate(2011, 2,1);
	private LocalDate mar10_2011 = new LocalDate(2011, 3,10);
	private LocalDate jun18_2011 = new LocalDate(2011, 6,18);
	private LocalDate aug9_2011 = new LocalDate(2011, 8,9);
	private LocalDate oct1_2011 = new LocalDate(2011, 10,1);
	private LocalDate dec31_2011 = new LocalDate(2011, 12,31);
	private LocalDate oct1_2012 = new LocalDate(2012, 10,1);


	@Test
	public void test_getMonthInQuarter() {
		assertEquals(1, DateUtil.getMonthInQuarter(jan1_2011));
		assertEquals(1, DateUtil.getMonthInQuarter(jan12_2011));
		assertEquals(2, DateUtil.getMonthInQuarter(feb1_2011));
		assertEquals(3, DateUtil.getMonthInQuarter(mar10_2011));
		assertEquals(2, DateUtil.getMonthInQuarter(aug9_2011));
		assertEquals(1, DateUtil.getMonthInQuarter(oct1_2011));
		assertEquals(3, DateUtil.getMonthInQuarter(dec31_2011));
		assertEquals(1, DateUtil.getMonthInQuarter(oct1_2012));
	}

	@Test
	public void test_quarterMonth() {
		assertEquals(DateTimeConstants.JANUARY, DateUtil.getQuarterMonth(jan12_2011));
		assertEquals(DateTimeConstants.JULY, DateUtil.getQuarterMonth(aug9_2011));
		assertEquals(DateTimeConstants.JANUARY, DateUtil.getQuarterMonth(mar10_2011));
		assertEquals(DateTimeConstants.OCTOBER, DateUtil.getQuarterMonth(oct1_2012));
	}

	@Test
	public void test_getQuarter() {
		assertEquals(1, DateUtil.getQuarter(jan1_2011));
		assertEquals(1, DateUtil.getQuarter(jan12_2011));
		assertEquals(1, DateUtil.getQuarter(feb1_2011));
		assertEquals(1, DateUtil.getQuarter(mar10_2011));
		assertEquals(2, DateUtil.getQuarter(jun18_2011));
		assertEquals(3, DateUtil.getQuarter(aug9_2011));
		assertEquals(4, DateUtil.getQuarter(oct1_2011));
		assertEquals(4, DateUtil.getQuarter(dec31_2011));
		assertEquals(4, DateUtil.getQuarter(oct1_2012));
	}

    @Ignore // TODO : currently broken. fix on next check in.
    @Test
    public void test_isMidnight() {
        DateTime nonMidnight = new DateTime().withHourOfDay(2).withDayOfYear(44).withYear(2012);
        DateTime midnight = new DateTime(new LocalDate().toDate());
        assertFalse(DateUtil.isMidnight(nonMidnight.toDate()));
        assertTrue(DateUtil.isMidnight(midnight.toDate()));
    }

    @Test
    public void test_sundayAfterWeek() {
        LocalDate sunday = DateUtil.getSundayAfterWeek(jan1_2011);
        assertEquals(new LocalDate(2011, 1, 2), sunday);
        assertEquals(DateTimeConstants.SUNDAY, sunday.getDayOfWeek());
        assertTrue(!sunday.isBefore(jan1_2011));

        sunday = DateUtil.getSundayAfterWeek(aug9_2011);
        assertEquals(new LocalDate(2011, 8, 14), sunday);
        assertEquals(DateTimeConstants.SUNDAY, sunday.getDayOfWeek());
        assertTrue(!sunday.isBefore(aug9_2011));

        sunday = DateUtil.getSundayAfterWeek(dec31_2011);
        assertEquals(new LocalDate(2012, 1, 1), sunday);
        assertEquals(DateTimeConstants.SUNDAY, sunday.getDayOfWeek());
        assertTrue(!sunday.isBefore(dec31_2011));

        sunday = DateUtil.getSundayAfterWeek(feb1_2011);
        assertEquals(new LocalDate(2011, 2, 6), sunday);
        assertEquals(DateTimeConstants.SUNDAY, sunday.getDayOfWeek());
        assertTrue(!sunday.isBefore(feb1_2011));
    }

    @Test
    public void test_sundayOfWeek() {
        LocalDate sunday = DateUtil.getSundayOfWeek(jan1_2011);
        assertEquals(new LocalDate(2010, 12, 26), sunday);
        assertEquals(DateTimeConstants.SUNDAY, sunday.getDayOfWeek());
        assertTrue(!sunday.isAfter(jan1_2011));

        sunday = DateUtil.getSundayOfWeek(aug9_2011);
        assertEquals(new LocalDate(2011, 8, 7), sunday);
        assertEquals(DateTimeConstants.SUNDAY, sunday.getDayOfWeek());
        assertTrue(!sunday.isAfter(aug9_2011));

        sunday = DateUtil.getSundayOfWeek(dec31_2011);
        assertEquals(new LocalDate(2011, 12, 25), sunday);
        assertEquals(DateTimeConstants.SUNDAY, sunday.getDayOfWeek());
        assertTrue(!sunday.isAfter(dec31_2011));

        sunday = DateUtil.getSundayOfWeek(this.feb1_2011);
        assertEquals(new LocalDate(2011, 1, 30), sunday);
        assertEquals(DateTimeConstants.SUNDAY, sunday.getDayOfWeek());
        assertTrue(!sunday.isAfter(feb1_2011));
    }

    @Test
    public void test_sundayMonthDateRange() {
        DateRange range = DateUtil.getSundayMonthDateRange(jan1_2011);
        assertEquals(new DateRange(new LocalDate(2010, 12,26), new LocalDate(2011,2,6)), range);
        assertEquals(DateTimeConstants.SUNDAY, range.getFrom().getDayOfWeek());
        assertEquals(DateTimeConstants.SUNDAY, range.getTo().getDayOfWeek());

        range = DateUtil.getSundayMonthDateRange(aug9_2011);
        assertEquals(new DateRange(new LocalDate(2011,7,31), new LocalDate(2011,9,4)), range);
        assertEquals(DateTimeConstants.SUNDAY, range.getFrom().getDayOfWeek());
        assertEquals(DateTimeConstants.SUNDAY, range.getTo().getDayOfWeek());

        range = DateUtil.getSundayMonthDateRange(dec31_2011);
        assertEquals(new DateRange(new LocalDate(2011,11,27), new LocalDate(2012,1,1)), range);
        assertEquals(DateTimeConstants.SUNDAY, range.getFrom().getDayOfWeek());
        assertEquals(DateTimeConstants.SUNDAY, range.getTo().getDayOfWeek());

        range = DateUtil.getSundayMonthDateRange(this.feb1_2011);
        assertEquals(new DateRange(new LocalDate(2011,1,30), new LocalDate(2011,3,6)), range);
        assertEquals(DateTimeConstants.SUNDAY, range.getFrom().getDayOfWeek());
        assertEquals(DateTimeConstants.SUNDAY, range.getTo().getDayOfWeek());
    }

}
