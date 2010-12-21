package com.n4systems.model.builders;

import com.n4systems.model.EventForm;

public class EventFormBuilder extends EntityWithTenantBuilder<EventForm> {

    public EventFormBuilder() {
    }

    public static EventFormBuilder anEventForm() {
        return new EventFormBuilder();
    }

    @Override
    public EventForm createObject() {
        EventForm eventForm = new EventForm();

        assignAbstractFields(eventForm);

        return eventForm;
    }

}
