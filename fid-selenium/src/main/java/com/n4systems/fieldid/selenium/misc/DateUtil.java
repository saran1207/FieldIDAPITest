package com.n4systems.fieldid.selenium.misc;

import com.ibm.icu.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

    public static Date parseDate(String date) {
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatDate(Date date) {
        return sdf.format(date);
    }

    public static Date theDayAfter(Date day) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(day);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

}
