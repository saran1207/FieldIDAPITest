package com.n4systems.fieldid.service.event.util;

import com.n4systems.api.conversion.event.CriteriaResultFactory;
import com.n4systems.model.*;

import java.util.List;

/**
 * Created by jheath on 2014-09-17.
 */
public class EditExistingEventTransientResultPopulator extends TransientCriteriaResultPopulator {

    private CriteriaResultFactory resultFactory;

    public EditExistingEventTransientResultPopulator() {
        resultFactory = new CriteriaResultFactory();
    }

    @Override
    protected void addSectionToTransientResults(List<AbstractEvent.SectionResults> transientResults, AbstractEvent.SectionResults sectionResults, List<CriteriaResult> transientSectionResults) {
        transientResults.add(sectionResults);
    }

    @Override
    protected CriteriaResult getCriteriaResultFor(AbstractEvent<ThingEventType, Asset> event, Criteria criteria) {
        for (CriteriaResult criteriaResult : event.getResults()) {
            if (criteriaResult.getCriteria().getId().equals(criteria.getId())) {
                return criteriaResult;
            }
        }

        return null;
    }

    @Override
    protected CriteriaResult getEditableCriteriaResult(AbstractEvent<ThingEventType, Asset> event, Criteria criteria) {
        return resultFactory.createCriteriaResult(criteria.getCriteriaType());
    }
}
