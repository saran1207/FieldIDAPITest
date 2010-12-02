package com.n4systems.model.builders;

import java.util.Set;
import java.util.TreeSet;

import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import com.n4systems.model.Tenant;

public class EventGroupBuilder extends BaseBuilder<EventGroup> {

    private Tenant tenant;
    private Set<Event> events = new TreeSet<Event>();

    public static EventGroupBuilder anEventGroup() {
        return new EventGroupBuilder(null, null);
    }

    public EventGroupBuilder(Tenant tenant, Set<Event> events) {
        this.tenant = tenant;
        this.events = events;
    }

    public EventGroupBuilder forTenant(Tenant tenant) {
        return makeBuilder(new EventGroupBuilder(tenant, events));
    }

    public EventGroupBuilder withEvents(Set<Event> events) {
        return makeBuilder(new EventGroupBuilder(tenant, events));
    }
    @Override
    public EventGroup createObject() {
        EventGroup group = new EventGroup();
        group.setTenant(tenant);
        group.setEvents(events);
        return group;
    }
}
