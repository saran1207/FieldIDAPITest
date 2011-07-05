package com.n4systems.model.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;

public class EventFormBuilder extends EntityWithTenantBuilder<EventForm> {

    private final List<CriteriaSection> sections;

    public EventFormBuilder(List<CriteriaSection> sections) {
        this.sections = sections;
    }

    public static EventFormBuilder anEventForm() {
        return new EventFormBuilder(new ArrayList<CriteriaSection>());
    }

    public EventFormBuilder withSections(CriteriaSection... sections) {
        ArrayList<CriteriaSection> newSectionsList = new ArrayList<CriteriaSection>();
        newSectionsList.addAll(Arrays.asList(sections));
        return makeBuilder(new EventFormBuilder(newSectionsList));
    }

    @Override 
    public EventForm createObject() {
        EventForm eventForm = new EventForm();

        for (CriteriaSection section : sections) {
            eventForm.getSections().add(section);
        }

        assignAbstractFields(eventForm);

        return eventForm;
    }

}
