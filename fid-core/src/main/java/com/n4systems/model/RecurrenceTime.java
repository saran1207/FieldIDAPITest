package com.n4systems.model;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recurrence_time")
public class RecurrenceTime extends BaseEntity {

    private Integer month;
    private Integer day;
    private int hour;
    private int minute;

    @ManyToOne
    @JoinColumn(name = "recurrence_id")
    private Recurrence recurrence;

    public RecurrenceTime() {
    }

    public RecurrenceTime(Recurrence recurrence) {
        setRecurrence(recurrence);
    }

    public RecurrenceTime(int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
    }

    public RecurrenceTime withTime(LocalTime time) {
        return withTime(time.getHourOfDay(), time.getMinuteOfHour());
    }

    public RecurrenceTime withTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        return this;
    }

    public RecurrenceTime withDayAndTime(int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        this.month = monthOfYear;
        this.day = dayOfMonth;
        this.hour = hourOfDay;
        this.minute = minuteOfHour;
        return this;
    }


    public boolean isDaily() {
        // if month/day not specified, then it's daily.
        return !hasDay();
    }

    public boolean hasDay() {
        return month!=null && day!=null;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    public RecurrenceTimeOfDay getRecurrenceTimeOfDay() {
        return RecurrenceTimeOfDay.from(toDateTime());
    }

    public Integer getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public boolean isMidnight() {
        return getHour()==0 && getMinute()==0;
    }

    public DateTime toDateTime() {
        DateTime dateTime = new DateTime().withHourOfDay(hour).withMinuteOfHour(minute);
        if (month!=null && day!=null) {
            dateTime = dateTime.withDayOfMonth(day).withMonthOfYear(month);
        }
        return dateTime;
    }

    public Period toPeriod() {
        Period period = new Period().withHours(hour).withMinutes(minute);
        if (month!=null && day!=null) {
            // note how these fields are 1 indexed.   i.e. JANUARY = 1, not 0.
            period = period.withMonths(month-1).withDays(day-1);
        }
        return period;
    }
}
