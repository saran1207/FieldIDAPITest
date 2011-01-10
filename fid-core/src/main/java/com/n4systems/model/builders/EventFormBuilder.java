package com.n4systems.model.builders;

import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;

import java.util.Collections;
import java.util.List;

public class EventFormBuilder extends EntityWithTenantBuilder<EventForm> {

    private final List<CriteriaSection> sections;

    public EventFormBuilder(List<CriteriaSection> sections) {
        this.sections = sections;
    }

    public static EventFormBuilder anEventForm() {
        return new EventFormBuilder(Collections.<CriteriaSection>emptyList());
    }

    public EventFormBuilder withSections(List<CriteriaSection> sections) {
        return makeBuilder(new EventFormBuilder(sections));
    }

    @Override
    public EventForm createObject() {
        EventForm eventForm = new EventForm();
        eventForm.setSections(sections);

        assignAbstractFields(eventForm);

        return eventForm;
    }

}
