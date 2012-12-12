package com.n4systems.fieldid.selenium.misc;

import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
    private static final SimpleDateFormat sdtf = new SimpleDateFormat("MM/dd/yy HH:mm aa");

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

    public static String formatDateTime(Date date) {
        return sdtf.format(date);
    }

    public static Date theDayAfter(Date day) {
        return DateUtils.addDays(day, 1);
    }

    public static Date addDays(Date date, int days) {
    	return DateUtils.addDays(date, days);
    }
}
