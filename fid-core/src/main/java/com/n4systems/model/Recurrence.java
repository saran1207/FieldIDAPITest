 package com.n4systems.model;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.List;

public enum Recurrence {

    // TODO DD : make this an embeddable type = {time, type}
    NONE,
    DAILY,
    WEEKLY_MONDAY,
    WEEKLY_TUESDAY,
    WEEKLY_WEDNESDAY,
    WEEKLY_THURSDAY,
    WEEKLY_FRIDAY,
    WEEKLY_SATURDAY,
    WEEKLY_SUNDAY,
    MONTHLY_1ST,
    MONTHLY_15TH,
    MONTHLY_LAST,
    YEARLY;

    Recurrence() {

    }

    public List<DateTime> getTimesForDay(LocalDate day) {
        if (requiresScheduleOn(day)) {
            return getTimesForDay(day, this);
        }
        return Lists.newArrayList();
    }

    private List<DateTime> getTimesForDay(LocalDate day, Recurrence recurrence) {
        // TODO DD : for now, just return one per day.  arbitrarily set hour to 9:00am just so i can test code.
        return Lists.newArrayList(new DateTime(day.toDate().getTime()).withHourOfDay(9));
    }

    public boolean requiresScheduleOn(LocalDate day) {
        switch (this) {
            case NONE:
                return false;
            case DAILY :
                return true;
            case WEEKLY_MONDAY:
                return day.getDayOfWeek()== DateTimeConstants.MONDAY;
            case WEEKLY_TUESDAY:
                return day.getDayOfWeek()== DateTimeConstants.TUESDAY;
            case WEEKLY_WEDNESDAY:
                return day.getDayOfWeek()== DateTimeConstants.WEDNESDAY;
            case WEEKLY_THURSDAY:
                return day.getDayOfWeek()== DateTimeConstants.THURSDAY;
            case WEEKLY_FRIDAY:
                return day.getDayOfWeek()== DateTimeConstants.FRIDAY;
            case WEEKLY_SATURDAY:
                return day.getDayOfWeek()== DateTimeConstants.SATURDAY;
            case WEEKLY_SUNDAY:
                return day.getDayOfWeek()== DateTimeConstants.SUNDAY;
            case MONTHLY_1ST:
                return day.getDayOfMonth()==-1;
            case MONTHLY_15TH:
                return day.getDayOfMonth()==15;
            case MONTHLY_LAST:
                return day.getDayOfMonth()==day.dayOfMonth().getMaximumValue();
            case YEARLY:
                return day.getDayOfYear()==1;
            default:
                throw new IllegalStateException("Recurrence " + this.name() + " not supported");
        }
    }
}
