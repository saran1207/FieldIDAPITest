package com.n4systems.fieldid.wicket.pages.escalationrules;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by rrana on 2015-08-24.
 */
public enum RulesDateRange {

    ONEHOUR     ("1 Hour"),
    THREEHOURS  ("3 Hours"),
    SIXHOURS    ("6 Hours"),
    NINEHOURS   ("9 Hours"),
    TWELVEHOURS ("12 Hours"),
    ONEDAY      ("1 Day"),
    TWODAYS     ("2 Days"),
    THREEDAYS   ("3 Days"),
    FOURDAYS    ("4 Days"),
    FIVEDAYS    ("5 Days"),
    ONEWEEK     ("1 Week");

    private String dueDate;

    RulesDateRange(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getLabel() {
        return dueDate;
    }

    public String getName() {
        return name();
    }

    public static List<Long> getLongValues() {
        ArrayList<Long> list = new ArrayList<>();

        list.add(TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS));
        list.add(TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS));
        list.add(TimeUnit.MILLISECONDS.convert(6, TimeUnit.HOURS));
        list.add(TimeUnit.MILLISECONDS.convert(9, TimeUnit.HOURS));
        list.add(TimeUnit.MILLISECONDS.convert(12, TimeUnit.HOURS));
        list.add(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        list.add(TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS));
        list.add(TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS));
        list.add(TimeUnit.MILLISECONDS.convert(4, TimeUnit.DAYS));
        list.add(TimeUnit.MILLISECONDS.convert(5, TimeUnit.DAYS));
        list.add(TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS));

        return list;
    }

    public static String getLabelFor(Long dateRange) {
        if(dateRange.equals(TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS))) {
            return ONEHOUR.getLabel();
        } else if(dateRange.equals(TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS))) {
            return THREEHOURS.getLabel();
        } else if(dateRange.equals(TimeUnit.MILLISECONDS.convert(6, TimeUnit.HOURS))) {
            return SIXHOURS.getLabel();
        } else if(dateRange.equals(TimeUnit.MILLISECONDS.convert(9, TimeUnit.HOURS))) {
            return NINEHOURS.getLabel();
        } else if(dateRange.equals(TimeUnit.MILLISECONDS.convert(12, TimeUnit.HOURS))) {
            return TWELVEHOURS.getLabel();
        } else if(dateRange.equals(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))) {
            return ONEDAY.getLabel();
        } else if(dateRange.equals(TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS))) {
            return TWODAYS.getLabel();
        } else if(dateRange.equals(TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS))) {
            return THREEDAYS.getLabel();
        } else if(dateRange.equals(TimeUnit.MILLISECONDS.convert(4, TimeUnit.DAYS))) {
            return FOURDAYS.getLabel();
        } else if(dateRange.equals(TimeUnit.MILLISECONDS.convert(5, TimeUnit.DAYS))) {
            return FIVEDAYS.getLabel();
        } else {
            return ONEWEEK.getLabel();
        }
    }

}
