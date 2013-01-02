package com.n4systems.model.builders;

import com.google.common.collect.Sets;
import com.n4systems.model.Recurrence;
import com.n4systems.model.RecurrenceTime;
import com.n4systems.model.RecurrenceTimeOfDay;
import com.n4systems.model.RecurrenceType;
import org.joda.time.LocalTime;

import java.util.Set;

public class RecurrenceBuilder extends BaseBuilder<Recurrence> {

    private Set<RecurrenceTime> times;
    private RecurrenceType type = RecurrenceType.DAILY;

    private RecurrenceBuilder(Set<RecurrenceTime> times,RecurrenceType type ) {
        super(null);
        this.times = times;
        this.type = type;
    }

    public static RecurrenceBuilder aRecurrence() {
        return new RecurrenceBuilder(Sets.newHashSet(new RecurrenceTime().withTime(new LocalTime(10,45))), RecurrenceType.DAILY);
    }

    public RecurrenceBuilder withTimes(Set<RecurrenceTime> times) {
        return new RecurrenceBuilder(times,type);
    }

    public RecurrenceBuilder withTimes(RecurrenceTime... times) {
        return new RecurrenceBuilder(Sets.newHashSet(times),type);
    }

    public RecurrenceBuilder withTimes(RecurrenceTimeOfDay... times) {
        Set<RecurrenceTime> set = Sets.newHashSet();
        for (RecurrenceTimeOfDay timeOfDay:times) {
            set.add(new RecurrenceTime().withTime(timeOfDay.asLocalTime()));
        }
        return new RecurrenceBuilder(set,type);
    }

    public RecurrenceBuilder withType(RecurrenceType type) {
        return new RecurrenceBuilder(times,type);
    }

    @Override
    public Recurrence createObject() {
        Recurrence recurrence = new Recurrence();
        recurrence.setTimes(times);
        recurrence.setType(type);
        return recurrence;
    }

}
