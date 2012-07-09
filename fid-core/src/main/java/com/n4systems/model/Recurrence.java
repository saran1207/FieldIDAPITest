package com.n4systems.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.model.orgs.BaseOrg;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.List;

@Embeddable
public class Recurrence implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(name="recurrence_type")
    private RecurrenceType type = RecurrenceType.NONE;
    private int hour;

    public Recurrence() {
        this(RecurrenceType.NONE, 0, null);
    }

    public Recurrence(RecurrenceType type, int hour, BaseOrg org) {
        setType(type);
        setHour(hour);
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

    public boolean requiresScheduleOn(LocalDate day) {
        return type.getNext(day.minusDays(1)).equals(day);
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
    public List<DateTime> getScheduledTimes(LocalDate from, LocalDate to) {
        List<DateTime> result = Lists.newArrayList();
        from = from.minusDays(1);
        LocalDate nextDate = type.getNext(from);
        while (nextDate.isBefore(to)) {
            result.add(new DateTime(nextDate.toDate()).withHourOfDay(hour));
            nextDate = type.getNext(nextDate);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Recurrence{" +
                "hour=" + hour +
                ", type=" + type +
                '}';
    }

}
