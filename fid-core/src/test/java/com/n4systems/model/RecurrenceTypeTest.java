package com.n4systems.model;

import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecurrenceTypeTest {

    private final LocalDate jan1_2011 = new LocalDate(2011, 1, 1);
    private final LocalDate dec31_2011 = new LocalDate(2011, 12, 31);
    private final LocalDate feb28_2012 = new LocalDate(2012, 2, 28);
    private final LocalDate feb28_2011 = new LocalDate(2011, 2, 28);
    private final LocalDate april30_2011 = new LocalDate(2011, 4, 30);


    @Test
    public void test_daily() {
        assertEquals(new LocalDate(2011, 1,2), RecurrenceType.DAILY.getNext(jan1_2011));
        assertEquals(new LocalDate(2012, 1,1), RecurrenceType.DAILY.getNext(dec31_2011));
        assertEquals(new LocalDate(2011, 3,1), RecurrenceType.DAILY.getNext(feb28_2011));
        assertEquals(new LocalDate(2012, 2,29), RecurrenceType.DAILY.getNext(feb28_2012));
        assertEquals(new LocalDate(2011, 5,1), RecurrenceType.DAILY.getNext(april30_2011));
    }

    @Test
    public void test_weekly_monday() {
        assertEquals(new LocalDate(2011, 1, 3), RecurrenceType.WEEKLY_MONDAY.getNext(jan1_2011));
        assertEquals(new LocalDate(2012, 1,2), RecurrenceType.WEEKLY_MONDAY.getNext(dec31_2011));
        assertEquals(new LocalDate(2011, 3,7), RecurrenceType.WEEKLY_MONDAY.getNext(feb28_2011));
        assertEquals(new LocalDate(2012, 3,5), RecurrenceType.WEEKLY_MONDAY.getNext(feb28_2012));
        assertEquals(new LocalDate(2011, 5,2), RecurrenceType.WEEKLY_MONDAY.getNext(april30_2011));
    }


    @Test
    public void test_weekly_tuesday() {
        assertEquals(new LocalDate(2011, 1,4), RecurrenceType.WEEKLY_TUESDAY.getNext(jan1_2011));
        assertEquals(new LocalDate(2012, 1,3), RecurrenceType.WEEKLY_TUESDAY.getNext(dec31_2011));
        assertEquals(new LocalDate(2011, 3,1), RecurrenceType.WEEKLY_TUESDAY.getNext(feb28_2011));
        assertEquals(new LocalDate(2012, 3,6), RecurrenceType.WEEKLY_TUESDAY.getNext(feb28_2012));
        assertEquals(new LocalDate(2011, 5,3), RecurrenceType.WEEKLY_TUESDAY.getNext(april30_2011));
    }


    @Test
    public void test_weekly_wednesday() {
        assertEquals(new LocalDate(2011, 1,5), RecurrenceType.WEEKLY_WEDNESDAY.getNext(jan1_2011));
        assertEquals(new LocalDate(2012, 1,4), RecurrenceType.WEEKLY_WEDNESDAY.getNext(dec31_2011));
        assertEquals(new LocalDate(2011, 3,2), RecurrenceType.WEEKLY_WEDNESDAY.getNext(feb28_2011));
        assertEquals(new LocalDate(2012, 2,29), RecurrenceType.WEEKLY_WEDNESDAY.getNext(feb28_2012));
        assertEquals(new LocalDate(2011, 5,4), RecurrenceType.WEEKLY_WEDNESDAY.getNext(april30_2011));
    }


    @Test
    public void test_weekly_thursday() {
        assertEquals(new LocalDate(2011, 1,6), RecurrenceType.WEEKLY_THURSDAY.getNext(jan1_2011));
        assertEquals(new LocalDate(2012, 1,5), RecurrenceType.WEEKLY_THURSDAY.getNext(dec31_2011));
        assertEquals(new LocalDate(2011, 3,3), RecurrenceType.WEEKLY_THURSDAY.getNext(feb28_2011));
        assertEquals(new LocalDate(2012, 3,1), RecurrenceType.WEEKLY_THURSDAY.getNext(feb28_2012));
        assertEquals(new LocalDate(2011, 5,5), RecurrenceType.WEEKLY_THURSDAY.getNext(april30_2011));
    }

    @Test
    public void test_weekly_friday() {
        assertEquals(new LocalDate(2011, 1,7), RecurrenceType.WEEKLY_FRIDAY.getNext(jan1_2011));
        assertEquals(new LocalDate(2012, 1,6), RecurrenceType.WEEKLY_FRIDAY.getNext(dec31_2011));
        assertEquals(new LocalDate(2011, 3,4), RecurrenceType.WEEKLY_FRIDAY.getNext(feb28_2011));
        assertEquals(new LocalDate(2012, 3,2), RecurrenceType.WEEKLY_FRIDAY.getNext(feb28_2012));
        assertEquals(new LocalDate(2011, 5,6), RecurrenceType.WEEKLY_FRIDAY.getNext(april30_2011));
    }

    @Test
    public void test_weekly_saturday() {
        assertEquals(new LocalDate(2011, 1,8), RecurrenceType.WEEKLY_SATURDAY.getNext(jan1_2011));
        assertEquals(new LocalDate(2012, 1,7), RecurrenceType.WEEKLY_SATURDAY.getNext(dec31_2011));
        assertEquals(new LocalDate(2011, 3,5), RecurrenceType.WEEKLY_SATURDAY.getNext(feb28_2011));
        assertEquals(new LocalDate(2012, 3,3), RecurrenceType.WEEKLY_SATURDAY.getNext(feb28_2012));
        assertEquals(new LocalDate(2011, 5,7), RecurrenceType.WEEKLY_SATURDAY.getNext(april30_2011));
    }

    @Test
    public void test_weekly_sunday() {
        assertEquals(new LocalDate(2011, 1,2), RecurrenceType.WEEKLY_SUNDAY.getNext(jan1_2011));
        assertEquals(new LocalDate(2012, 1,1), RecurrenceType.WEEKLY_SUNDAY.getNext(dec31_2011));
        assertEquals(new LocalDate(2011, 3,6), RecurrenceType.WEEKLY_SUNDAY.getNext(feb28_2011));
        assertEquals(new LocalDate(2012, 3,4), RecurrenceType.WEEKLY_SUNDAY.getNext(feb28_2012));
        assertEquals(new LocalDate(2011, 5,1), RecurrenceType.WEEKLY_SUNDAY.getNext(april30_2011));
    }

    @Test
    public void test_monthly_15th() {
        assertEquals(new LocalDate(2011, 1,15), RecurrenceType.MONTHLY_15TH.getNext(jan1_2011));
        assertEquals(new LocalDate(2012, 1,15), RecurrenceType.MONTHLY_15TH.getNext(dec31_2011));
        assertEquals(new LocalDate(2011, 3,15), RecurrenceType.MONTHLY_15TH.getNext(feb28_2011));
        assertEquals(new LocalDate(2012, 3,15), RecurrenceType.MONTHLY_15TH.getNext(feb28_2012));
        assertEquals(new LocalDate(2011, 5,15), RecurrenceType.MONTHLY_15TH.getNext(april30_2011));
    }

    @Test
    public void test_monthly_last() {
        assertEquals(new LocalDate(2011, 1,31), RecurrenceType.MONTHLY_LAST.getNext(jan1_2011));
        assertEquals(new LocalDate(2012, 1,31), RecurrenceType.MONTHLY_LAST.getNext(dec31_2011));
        assertEquals(new LocalDate(2011, 3,31), RecurrenceType.MONTHLY_LAST.getNext(feb28_2011));
        assertEquals(new LocalDate(2012, 2,29), RecurrenceType.MONTHLY_LAST.getNext(feb28_2012));
        assertEquals(new LocalDate(2011, 5,31), RecurrenceType.MONTHLY_LAST.getNext(april30_2011));
    }

    @Test
    public void test_monthly_first() {
        assertEquals(new LocalDate(2011, 2,1), RecurrenceType.MONTHLY_1ST.getNext(jan1_2011));
        assertEquals(new LocalDate(2012, 1,1), RecurrenceType.MONTHLY_1ST.getNext(dec31_2011));
        assertEquals(new LocalDate(2011, 3,1), RecurrenceType.MONTHLY_1ST.getNext(feb28_2011));
        assertEquals(new LocalDate(2012, 3,1), RecurrenceType.MONTHLY_1ST.getNext(feb28_2012));
        assertEquals(new LocalDate(2011, 5,1), RecurrenceType.MONTHLY_1ST.getNext(april30_2011));
    }

    @Test
    public void test_yearly() {
        MonthDay jan23 = new MonthDay(1,23);
        MonthDay dec15 = new MonthDay(12,15);
        MonthDay july1 = new MonthDay(7,1);
        MonthDay aug19 = new MonthDay(8,19);

        assertEquals(new LocalDate(2011, 12,15), RecurrenceType.ANNUALLY.getNext(jan1_2011,dec15));
        assertEquals(new LocalDate(2012, 12,15), RecurrenceType.ANNUALLY.getNext(dec31_2011, dec15));
        assertEquals(new LocalDate(2011, 12,15), RecurrenceType.ANNUALLY.getNext(feb28_2011,dec15));
        assertEquals(new LocalDate(2012, 12,15), RecurrenceType.ANNUALLY.getNext(feb28_2012,dec15));
        assertEquals(new LocalDate(2011, 12,15), RecurrenceType.ANNUALLY.getNext(april30_2011,dec15));

        assertEquals(new LocalDate(2011, 7,1), RecurrenceType.ANNUALLY.getNext(jan1_2011,july1));
        assertEquals(new LocalDate(2012, 7,1), RecurrenceType.ANNUALLY.getNext(dec31_2011, july1));
        assertEquals(new LocalDate(2011, 7,1), RecurrenceType.ANNUALLY.getNext(feb28_2011,july1));
        assertEquals(new LocalDate(2012, 7,1), RecurrenceType.ANNUALLY.getNext(feb28_2012,july1));
        assertEquals(new LocalDate(2011, 7,1), RecurrenceType.ANNUALLY.getNext(april30_2011,july1));

        assertEquals(new LocalDate(2011, 8,19), RecurrenceType.ANNUALLY.getNext(jan1_2011,aug19));
        assertEquals(new LocalDate(2012, 8,19), RecurrenceType.ANNUALLY.getNext(dec31_2011, aug19));
        assertEquals(new LocalDate(2011, 8,19), RecurrenceType.ANNUALLY.getNext(feb28_2011,aug19));
        assertEquals(new LocalDate(2012, 8,19), RecurrenceType.ANNUALLY.getNext(feb28_2012,aug19));
        assertEquals(new LocalDate(2011, 8,19), RecurrenceType.ANNUALLY.getNext(april30_2011,aug19));

    }



}
