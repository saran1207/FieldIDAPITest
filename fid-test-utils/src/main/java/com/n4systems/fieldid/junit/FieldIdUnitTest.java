package com.n4systems.fieldid.junit;

import org.joda.time.*;
import org.junit.Before;

import java.util.TimeZone;

public class FieldIdUnitTest {

    protected final LocalDate firstMonIn2011 = new LocalDate(getTimeZone()).withYear(2011).withDayOfYear(3);
    protected final LocalDate jan1_2011 = new LocalDate(getTimeZone()).withYear(2011).withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1);
    protected final LocalDate jan31_2011 = new LocalDate(getTimeZone()).withYear(2011).withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(31);
    protected final LocalDate feb1_2011 = new LocalDate(getTimeZone()).withYear(2011).withMonthOfYear(DateTimeConstants.FEBRUARY).withDayOfMonth(1);
    protected final LocalDate mar1_2011 = new LocalDate(getTimeZone()).withYear(2011).withMonthOfYear(DateTimeConstants.MARCH).withDayOfMonth(1);
    protected final LocalDate jan1_2015 = new LocalDate(getTimeZone()).withYear(2015).withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1);
    protected final LocalDate feb28_2012 = new LocalDate(2012, 2, 28);
    protected final LocalDate feb29_2012 = new LocalDate(getTimeZone()).withYear(2012).withMonthOfYear(DateTimeConstants.FEBRUARY).withDayOfMonth(29);
    protected final LocalDate dec27_2011 = new LocalDate(getTimeZone()).withYear(2011).withMonthOfYear(DateTimeConstants.DECEMBER).withDayOfMonth(27);
    protected final LocalDateTime jan1_2011_midnight = new LocalDateTime(jan1_2011.toDate());


    @Before
    public void setUp() {
        setTestTime();
    }

    protected void setCurrentMillisFixed(long time) {
        DateTimeUtils.setCurrentMillisFixed(time);
    }

    protected void setCurrentMillisFixed(LocalDateTime dateTime) {
        setCurrentMillisFixed(dateTime.toDate().getTime());
    }

    protected void setCurrentMillisFixed(LocalDate localDate) {
        setCurrentMillisFixed(localDate.toDate().getTime());
    }

    protected DateTimeZone getTimeZone() {
        return DateTimeZone.UTC;
    }

    protected final void setTestTime() {
        // by default, we'll reset all tests to a fixed time.
        TimeZone.setDefault(DateTimeZone.UTC.toTimeZone());
        DateTimeZone.setDefault(DateTimeZone.UTC);
        setCurrentMillisFixed(getTestTime());
    }

    protected LocalDateTime getTestTime() {
        return jan1_2011_midnight;
    }

}
