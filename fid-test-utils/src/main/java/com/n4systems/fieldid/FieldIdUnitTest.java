package com.n4systems.fieldid;

import org.joda.time.*;
import org.junit.Before;

import java.util.TimeZone;

public class FieldIdUnitTest {

    protected LocalDate jan1_2011 = new LocalDate(getTimeZone()).withYear(2011).withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1);
    protected LocalDate jan1_2015 = new LocalDate(getTimeZone()).withYear(2015).withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1);
    protected LocalDate feb29_2012 = new LocalDate(getTimeZone()).withYear(2012).withMonthOfYear(DateTimeConstants.FEBRUARY).withDayOfMonth(29);
    protected LocalDate dec27_2011 = new LocalDate(getTimeZone()).withYear(2011).withMonthOfYear(DateTimeConstants.DECEMBER).withDayOfMonth(27);
    protected DateTime jan1_2011_midnight = new DateTime(jan1_2011.toDate());

    protected void setCurrentMillisFixed(long time) {
        DateTimeUtils.setCurrentMillisFixed(time);
    }

    protected void setCurrentMillisFixed(DateTime dateTime) {
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

    protected long getTestTime() {
        return jan1_2011.toDate().getTime();
    }

    @Before
    public void setUp() {
        setTestTime();
    }
}
