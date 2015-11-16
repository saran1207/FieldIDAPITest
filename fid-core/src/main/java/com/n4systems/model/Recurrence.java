package com.n4systems.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "recurrence")
public class Recurrence extends BaseEntity {

    private static final Logger logger=Logger.getLogger(Recurrence.class);

    @OneToMany(mappedBy = "recurrence", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RecurrenceTime> times = new TreeSet<RecurrenceTime>();

    @Enumerated(EnumType.STRING)
    @Column(name="recurrence_type")
    private RecurrenceType type = RecurrenceType.WEEKDAYS;

    public Recurrence() {
        this(RecurrenceType.WEEKDAYS);
    }

    public Recurrence(RecurrenceType type) {
        setType(type);
    }

    public Recurrence withTime(LocalTime localTime) {
        times.add(new RecurrenceTime(this).withTime(localTime));
        return this;
    }


    public Recurrence withTime(RecurrenceTimeOfDay time) {
        return withTime(time.asLocalTime());
    }

    public Recurrence withTimes(List<RecurrenceTimeOfDay> times) {
        for (RecurrenceTimeOfDay time:times) {
            withTime(time);
        }
        return this;
    }

    public Recurrence withDayAndTime(int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        times.add(new RecurrenceTime(this).withDayAndTime(monthOfYear, dayOfMonth, hourOfDay, minuteOfHour));
        return this;
    }

    public Recurrence withDayAndTime(Date date) {
        Preconditions.checkState(type.requiresDate(), "you can't add day for recurrence type " + this);
        LocalDateTime localDateTime = new LocalDateTime(date);
        return withDayAndTime(localDateTime.getMonthOfYear(), localDateTime.getDayOfMonth(), localDateTime.getHourOfDay(), localDateTime.getMinuteOfHour());
    }

    public RecurrenceType getType() {
        return type;
    }

    public void setType(RecurrenceType type) {
        Preconditions.checkArgument(type != null, "recurrence type can not be null");
        this.type = type;
    }

    public Set<RecurrenceTime> getTimes() {
        return times;
    }

    public void setTimes(Set<RecurrenceTime> times) {
        this.times.clear();
        this.times.addAll(times);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recurrence)) return false;
        if (!super.equals(o)) return false;

        Recurrence that = (Recurrence) o;

        if (times != null ? !times.equals(that.times) : that.times != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (times != null ? times.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Recurrence{" +
                "times=" + times +
                ", type=" + type +
                '}';
    }

}
