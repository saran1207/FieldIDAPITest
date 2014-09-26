package com.n4systems.fieldid.service.event.util;

import com.n4systems.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class TransientCriteriaResultPopulator {

    public void populateTransientCriteriaResultsForEvent(AbstractEvent event) {
        EventForm eventForm = event.getEventForm();

        List<AbstractEvent.SectionResults> transientResults = new ArrayList<>();

        if (eventForm != null) {
            for (CriteriaSection section : eventForm.getAvailableSections()) {
                List<CriteriaResult> transientSectionResults = new ArrayList<>();
                AbstractEvent.SectionResults sectionResults = new AbstractEvent.SectionResults();

                for (Criteria criteria : section.getAvailableCriteria()) {

                    CriteriaResult transientResult = getCriteriaResultFor(event, criteria);

                    if (transientResult == null) {
                        sectionResults.disabled = true;
                        if((transientResult = getEditableCriteriaResult(event, criteria)) == null) {
                            continue;
                        }
                    }

                    transientResult.setCriteria(criteria);
                    transientResult.setTenant(event.getTenant());
                    transientSectionResults.add(transientResult);
                }

                sectionResults.results = transientSectionResults;
                sectionResults.section = section;

                if(!transientSectionResults.isEmpty()) {
                    transientResults.add(sectionResults);
                }
            }
        }

        event.setSectionResults(transientResults);
    }

    protected abstract void addSectionToTransientResults(List<AbstractEvent.SectionResults> transientResults, AbstractEvent.SectionResults sectionResults, List<CriteriaResult> transientSectionResults);

    protected abstract CriteriaResult getCriteriaResultFor(AbstractEvent<ThingEventType,Asset> event, Criteria criteria);

    protected CriteriaResult getEditableCriteriaResult(AbstractEvent<ThingEventType,Asset> event, Criteria criteria) {
        return null;
    }

}
