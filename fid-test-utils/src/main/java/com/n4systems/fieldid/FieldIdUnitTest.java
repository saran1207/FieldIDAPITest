package com.n4systems.fieldid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.util.Date;

public class FieldIdUnitTest {

    protected void setCurrentMillisFixed(long time) {
        LocalDate JAN1 = new LocalDate(new Date(), DateTimeZone.UTC);
        DateTimeUtils.setCurrentMillisFixed(time);
    }

    protected void setCurrentMillisFixed(DateTime dateTime) {
        setCurrentMillisFixed(dateTime.toDate().getTime());
    }

    protected void setCurrentMillisFixed(LocalDate localDate) {
        setCurrentMillisFixed(localDate.toDate().getTime());
    }

}
