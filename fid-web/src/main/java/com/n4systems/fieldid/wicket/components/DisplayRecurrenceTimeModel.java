package com.n4systems.fieldid.wicket.components;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.n4systems.model.RecurrenceTime;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DisplayRecurrenceTimeModel extends Model<String> {

    private PropertyModel<Set<RecurrenceTime>> model;

    public DisplayRecurrenceTimeModel(PropertyModel<Set<RecurrenceTime>> model) {
        this.model = model;
    }

    @Override
    public String getObject() {
        List<String> result = Lists.newArrayList();
        Iterator<RecurrenceTime> iterator = model.getObject().iterator();
        while (iterator.hasNext()) {
            result.add(convertTimeToString(iterator.next()));
        }
        return Joiner.on(",").join(result);
    }

    protected String convertTimeToString(RecurrenceTime time) {
        String monthDay = (time.hasDay()) ?
                new LocalDate().withMonthOfYear(time.getMonth()).withDayOfMonth(time.getDayOfMonth()).toString("MMM d") + " " :
                "";

        LocalTime localTime = new LocalTime().withHourOfDay(time.getHour()).withMinuteOfHour(time.getMinute());

        String clock = localTime.toString("hh:mm a");

        return monthDay + clock;
    }
}