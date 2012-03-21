package com.n4systems.fieldid;

import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class FieldIdUnitTest {

    protected void setCurrentMillisFixed(long time) {
        DateTimeZone.setDefault(DateTimeZone.UTC);
        DateTimeUtils.setCurrentMillisFixed(time);
    }

    protected void setCurrentMillisFixed(long time, TimeZone zone) {
        DateTimeZone.setDefault(DateTimeZone.forTimeZone(zone));
        DateTimeUtils.setCurrentMillisFixed(time);
    }

}
