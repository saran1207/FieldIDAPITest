package com.n4systems.fieldid.service.event.util;

import com.n4systems.api.conversion.event.CriteriaResultFactory;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;

import java.util.List;

public class NewEventTransientCriteriaResultPopulator extends TransientCriteriaResultPopulator {

    private CriteriaResultFactory resultFactory;

    public NewEventTransientCriteriaResultPopulator() {
        resultFactory = new CriteriaResultFactory();
    }

    @Override
    protected CriteriaResult getCriteriaResultFor(AbstractEvent event, Criteria criteria) {
        return resultFactory.createCriteriaResult(criteria.getCriteriaType());
    }

    @Override
    protected void addSectionToTransientResults(List<AbstractEvent.SectionResults> transientResults, AbstractEvent.SectionResults sectionResults, List<CriteriaResult> transientSectionResults) {
        transientResults.add(sectionResults);
    }
}
