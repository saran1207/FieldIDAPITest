package com.n4systems.fieldid.service.event.util;

import com.n4systems.model.*;

import java.util.List;

public class ExistingEventTransientCriteriaResultPopulator extends TransientCriteriaResultPopulator {


    @Override
    protected void addSectionToTransientResults(List<AbstractEvent.SectionResults> transientResults, AbstractEvent.SectionResults sectionResults, List<CriteriaResult> transientSectionResults) {
        if(!transientSectionResults.isEmpty()) {
            transientResults.add(sectionResults);
        }
    }

    @Override
    protected CriteriaResult getCriteriaResultFor(AbstractEvent<ThingEventType,Asset> event, Criteria criteria) {
        for (CriteriaResult criteriaResult : event.getResults()) {
            if (criteriaResult.getCriteria().getId().equals(criteria.getId())) {
                return criteriaResult;
            }
        }
        return null;
    }

}
