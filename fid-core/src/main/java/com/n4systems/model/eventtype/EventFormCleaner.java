package com.n4systems.model.eventtype;

import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.api.EntityWithTenantCleaner;
import com.n4systems.model.criteriasection.CriteriaSectionCleaner;

import java.util.ArrayList;
import java.util.List;

public class EventFormCleaner extends EntityWithTenantCleaner<EventForm> {

    private final Cleaner<CriteriaSection> sectionCleaner;

    public EventFormCleaner(Tenant newTenant, Cleaner<CriteriaSection> sectionCleaner) {
        super(newTenant);
        this.sectionCleaner = sectionCleaner;
    }

    public EventFormCleaner(Tenant newTenant) {
        this(newTenant, new CriteriaSectionCleaner(newTenant));
    }

    @Override
    public void clean(EventForm eventForm) {
        super.clean(eventForm);
        cleanSections(eventForm);
    }

    private void cleanSections(EventForm eventForm) {
        // we want to create a new list of sections, rather then removing old ones to avoid ConcurrentModification while we iterate
        List<CriteriaSection> cleanedSections = new ArrayList<CriteriaSection>();
        for (CriteriaSection section: eventForm.getSections()) {
            // there's no need to copy the retired criteria
            if (!section.isRetired()) {
                sectionCleaner.clean(section);

                cleanedSections.add(section);
            }
        }

        eventForm.setSections(cleanedSections);
    }

}
