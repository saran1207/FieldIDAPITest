package com.n4systems.fieldid.service.event.util;

import com.n4systems.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class TransientCriteriaResultPopulator {

    public void populateTransientCriteriaResultsForNewEvent(AbstractEvent event) {
        EventForm eventForm = event.getEventForm();

        List<AbstractEvent.SectionResults> transientResults = new ArrayList<AbstractEvent.SectionResults>();

        if (eventForm != null) {
            for (CriteriaSection section : eventForm.getAvailableSections()) {
                List<CriteriaResult> transientSectionResults = new ArrayList<CriteriaResult>();
                AbstractEvent.SectionResults sectionResults = new AbstractEvent.SectionResults();

                for (Criteria criteria : section.getAvailableCriteria()) {

                    CriteriaResult transientResult = getCriteriaResultFor(event, criteria);

                    if (transientResult == null) {
                        continue;
                    }

                    transientResult.setCriteria(criteria);
                    transientResult.setTenant(event.getTenant());
                    transientSectionResults.add(transientResult);
                }
                sectionResults.results = transientSectionResults;
                sectionResults.section = section;
                transientResults.add(sectionResults);
            }
        }

        event.setSectionResults(transientResults);
    }

    protected abstract CriteriaResult getCriteriaResultFor(AbstractEvent<ThingEventType> event, Criteria criteria);

}
