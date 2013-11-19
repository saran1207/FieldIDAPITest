package com.n4systems.fieldid.wicket.model.time;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.n4systems.model.RecurrenceTime;
import com.n4systems.util.StringUtils;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RecurrenceTimeModel extends Model<String> {

    private PropertyModel<Set<RecurrenceTime>> model;
    private final String format;

    public RecurrenceTimeModel(PropertyModel<Set<RecurrenceTime>> model) {
        this(model,null);
    }

    public RecurrenceTimeModel(PropertyModel<Set<RecurrenceTime>> model, String format) {
        this.model = model;
        this.format = format;
    }

    @Override
    public String getObject() {
        List<String> result = Lists.newArrayList();
        Iterator<RecurrenceTime> iterator = model.getObject().iterator();
        while (iterator.hasNext()) {
            result.add(convertTimeToString(iterator.next()));
        }
        String text = Joiner.on(",").join(result);
        if (StringUtils.isEmpty(text)) {
            return text;
        }
        return format==null ? text : String.format(format,text);
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
