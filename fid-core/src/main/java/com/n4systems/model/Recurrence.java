package com.n4systems.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Embeddable
public class Recurrence implements Serializable {

    private static final Logger logger=Logger.getLogger(Recurrence.class);

    private int hour;
    private int minute;

    @Enumerated(EnumType.STRING)
    @Column(name="recurrence_type")
    private RecurrenceType type = RecurrenceType.MONTHLY_1ST;

    @Temporal(TemporalType.TIMESTAMP)
    // NOTE : the year part is ignored.  we just are concerned with the day.
    private Date day;

    public Recurrence() {
        this(RecurrenceType.MONTHLY_1ST, 0, 0);
    }

    public Recurrence(RecurrenceType type, int hour, int minute) {
        setType(type);
        setHour(hour);
        setMinute(minute);
    }

    public Recurrence(RecurrenceType type, int hour, int minutes, Date day) {
        this(type, hour, minutes);
        setDay(day);
    }

    public RecurrenceType getType() {
        return type;
    }

    public void setType(RecurrenceType type) {
        Preconditions.checkArgument(type!=null, "recurrence type can not be null");
        this.type = type;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        Preconditions.checkArgument(hour >= 0 && hour<24, "you must specify a [0..24] value for hour. " + hour + " is not valid.");
        this.hour = hour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recurrence)) return false;

        Recurrence that = (Recurrence) o;

        if (hour != that.hour) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + hour;
        return result;
    }

    // CAVEAT : this is on a day granularity (i.e. from & to don't currently support hours).
    public List<LocalDateTime> getScheduledTimes(LocalDate from, LocalDate to) {
        List<LocalDateTime> result = Lists.newArrayList();
        from = from.minusDays(1);

        LocalDateTime fromDateTime = new LocalDateTime(from.toDate());
        LocalDate nextDate = type.getNext(from, day);
        while (!nextDate.isAfter(to)) {
            LocalDateTime dateTime = new LocalDateTime(nextDate.toDate()).withHourOfDay(hour).withMinuteOfHour(minute);
            if (!dateTime.isBefore(fromDateTime)) {
                result.add(dateTime);
            }
            nextDate = getNext(nextDate);
        }
        return result;
    }

    private LocalDate getNext(LocalDate nextDate) {
        return type.getNext(nextDate, day);
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        Preconditions.checkArgument(minute>=0 && minute<60, "minute must be [0..59] : " + minute);
        this.minute = minute;
    }

    public String getDisplayTime() {
        String monthDay = (type.requiresDate() && day!=null) ? new LocalDate(day).toString("MMM d") + " " : "";
        LocalTime time = new LocalTime().withHourOfDay(hour).withMinuteOfHour(minute);
        String clock = time.toString("K:mm a");
        if (hour==0 && minute==0) {
            clock = type.requiresDate() ? "" : "12:00 AM";  // workaround formatter which is military.
        }
        return monthDay + clock;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        if (!type.requiresDate()) {
            logger.warn("can't set date on recurrence for type : " + type);
            return;  // ignore invalid request to set day.
        }
        this.day = new LocalDate(day).toDate();
    }

    @Override
    public String toString() {
        return "Recurrence{" +
                "hour=" + hour +
                ", type=" + type +
                '}';
    }

}
