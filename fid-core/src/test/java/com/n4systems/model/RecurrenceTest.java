package com.n4systems.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RecurrenceTest {

    private final LocalDate jan1_2011 = new LocalDate(2011, 1, 1);
    private final LocalDate jan1_2012 = new LocalDate(2012, 1, 1);
    private final LocalDate jan31_2011 = new LocalDate(2011, 1, 31);
    private final LocalDate dec31_2012 = new LocalDate(2012, 12, 31);
    private final LocalDate dec31_2011 = new LocalDate(2011, 12, 31);
    private final LocalDate feb28_2012 = new LocalDate(2012, 2, 28);
    private final LocalDate feb28_2011 = new LocalDate(2011, 2, 28);
    private final LocalDate april30_2011 = new LocalDate(2011, 4, 30);

    @Test
    public void test_daily() {

        Recurrence recurrence = new Recurrence(RecurrenceType.DAILY, 0, 0);
        List<DateTime> times;
        times = recurrence.getScheduledTimes(jan1_2011, jan31_2011);
        assertEquals(31, times.size());
        assertEquals(new LocalDate(2011,1,1), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,1,31), new LocalDate(times.get(30)));

        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(365, times.size());
        assertEquals(new LocalDate(2011,1,1), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,12,31), new LocalDate(times.get(364)));

        times = recurrence.getScheduledTimes(jan1_2012, dec31_2012);
        assertEquals(366, times.size());  // it's a LEAP YEAR!
        assertEquals(new LocalDate(2012,1,1), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2012,2,29), new LocalDate(times.get(59)));  // feb 29th.
        assertEquals(new LocalDate(2012,12,31), new LocalDate(times.get(365)));

    }

    @Test(expected =  IllegalArgumentException.class)
    public void test_bad_minutes() {
        Recurrence x = new Recurrence(RecurrenceType.MONTHLY_15TH, 0, 99);
    }

    @Test(expected =  IllegalArgumentException.class)
    public void test_bad_hours() {
        Recurrence x = new Recurrence(RecurrenceType.DAILY, 77, 00);
    }

    @Test
    public void test_monthly() {
        Recurrence recurrence = new Recurrence(RecurrenceType.MONTHLY_1ST, 15, 44);
        List<DateTime> times;
        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(12, times.size());
        assertEquals(new LocalDate(2011,1,1), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,12,1), new LocalDate(times.get(11)));

        times = recurrence.getScheduledTimes(jan1_2011, dec31_2012);
        assertEquals(24, times.size());
        assertEquals(new LocalDate(2011,1,1), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2012,12,1), new LocalDate(times.get(23)));

        recurrence = new Recurrence(RecurrenceType.MONTHLY_15TH, 15, 44);
        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(12, times.size());
        assertEquals(new LocalDate(2011,1,15), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,12,15), new LocalDate(times.get(11)));

        times = recurrence.getScheduledTimes(jan1_2011, dec31_2012);
        assertEquals(24, times.size());
        assertEquals(new LocalDate(2011,1,15), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2012,12,15), new LocalDate(times.get(23)));

        recurrence = new Recurrence(RecurrenceType.MONTHLY_LAST, 15, 44);
        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(12, times.size());
        assertEquals(new LocalDate(2011,1,31), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,2,28), new LocalDate(times.get(1)));
        assertEquals(new LocalDate(2011,4,30), new LocalDate(times.get(3)));
        assertEquals(new LocalDate(2011,12,31), new LocalDate(times.get(11)));

        times = recurrence.getScheduledTimes(jan1_2011, dec31_2012);
        assertEquals(24, times.size());
        assertEquals(new LocalDate(2011,1,31), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,2,28), new LocalDate(times.get(1)));
        assertEquals(new LocalDate(2011,4,30), new LocalDate(times.get(3)));
        assertEquals(new LocalDate(2012,2,29), new LocalDate(times.get(13)));
        assertEquals(new LocalDate(2012,12,31), new LocalDate(times.get(23)));

    }

    @Test
    public void test_weekly() {
        Recurrence recurrence = null;new Recurrence(RecurrenceType.WEEKLY_FRIDAY, 15, 44);
        List<DateTime> times;

        recurrence = new Recurrence(RecurrenceType.WEEKLY_MONDAY, 15, 44);
        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(52, times.size());
        assertEquals(new LocalDate(2011,1,3), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,12,26), new LocalDate(times.get(51)));

        recurrence = new Recurrence(RecurrenceType.WEEKLY_TUESDAY, 15, 44);
        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(52, times.size());
        assertEquals(new LocalDate(2011,1,4), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,12,27), new LocalDate(times.get(51)));

        recurrence = new Recurrence(RecurrenceType.WEEKLY_WEDNESDAY, 15, 44);
        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(52, times.size());
        assertEquals(new LocalDate(2011,1,5), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,12,28), new LocalDate(times.get(51)));

        recurrence = new Recurrence(RecurrenceType.WEEKLY_THURSDAY, 15, 44);
        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(52, times.size());
        assertEquals(new LocalDate(2011,1,6), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,12,29), new LocalDate(times.get(51)));

        recurrence = new Recurrence(RecurrenceType.WEEKLY_FRIDAY, 15, 44);
        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(52, times.size());
        assertEquals(new LocalDate(2011,1,7), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,12,30), new LocalDate(times.get(51)));

        recurrence = new Recurrence(RecurrenceType.WEEKLY_SATURDAY, 15, 44);
        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(53, times.size());  // NOTE that this borders on a week boundary.
        assertEquals(new LocalDate(2011,1,1), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,12,31), new LocalDate(times.get(52)));

        recurrence = new Recurrence(RecurrenceType.WEEKLY_SUNDAY, 15, 44);
        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(52, times.size());
        assertEquals(new LocalDate(2011,1,2), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2011,12,25), new LocalDate(times.get(51)));

    }

    @Test
    public void test_annually() {

        Recurrence recurrence = null;
        List<DateTime> times;

        recurrence = new Recurrence(RecurrenceType.ANNUALLY, 15, 44, april30_2011.toDate());  // once a year. every april 30th
        times = recurrence.getScheduledTimes(jan1_2011, dec31_2011);
        assertEquals(1, times.size());
        assertEquals(new LocalDate(2011,4,30), new LocalDate(times.get(0)));

        times = recurrence.getScheduledTimes(jan1_2011, jan31_2011);
        assertEquals(0, times.size());

        times = recurrence.getScheduledTimes(jan1_2011, dec31_2012);
        assertEquals(2, times.size());
        assertEquals(new LocalDate(2011,4,30), new LocalDate(times.get(0)));
        assertEquals(new LocalDate(2012,4,30), new LocalDate(times.get(1)));

    }


}
