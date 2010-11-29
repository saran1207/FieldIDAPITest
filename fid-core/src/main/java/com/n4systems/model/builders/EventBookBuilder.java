package com.n4systems.model.builders;

import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EventBookBuilder extends EntityWithOwnerBuilder<EventBook> {

    private String name;
    private boolean open;
    private Set<Event> events;

    private EventBookBuilder(Tenant tenant, BaseOrg owner, String name, boolean open, Set<Event> events) {
        super(tenant, owner);
        this.name = name;
        this.open = open;
        this.events = events;
    }

    public EventBookBuilder forTenant(Tenant tenant) {
        return makeBuilder(new EventBookBuilder(tenant, owner, name, open, events));
    }

    public EventBookBuilder withOwner(BaseOrg owner) {
        return makeBuilder(new EventBookBuilder(tenant, owner, name, open, events));
    }

    public EventBookBuilder withName(String name) {
        return makeBuilder(new EventBookBuilder(tenant, owner, name, open, events));
    }

    public EventBookBuilder open(boolean open) {
        return makeBuilder(new EventBookBuilder(tenant, owner, name, open, events));
    }

    public EventBookBuilder withEvents(Event... evts) {
        Set<Event> events = new HashSet<Event>(Arrays.asList(evts));
        return makeBuilder(new EventBookBuilder(tenant, owner, name, open, events));
    }

    @Override
    public EventBook createObject() {
        EventBook book = super.assignAbstractFields(new EventBook());
        book.setName(name);
        book.setOpen(open);

        if (events != null) {
            book.setEvents(events);
            for (Event event : events) {
                event.setBook(book);
            }
        }

        return book;
    }

    public static EventBookBuilder anEventBook() {
        return new EventBookBuilder(null, null, null, false, null);
    }
}
