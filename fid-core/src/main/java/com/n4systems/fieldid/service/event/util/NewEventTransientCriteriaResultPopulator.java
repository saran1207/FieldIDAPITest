package com.n4systems.fieldid.service.event.util;

import com.n4systems.api.conversion.event.CriteriaResultFactory;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;

public class NewEventTransientCriteriaResultPopulator extends TransientCriteriaResultPopulator {

    private CriteriaResultFactory resultFactory;

    public NewEventTransientCriteriaResultPopulator() {
        resultFactory = new CriteriaResultFactory();
    }

    @Override
    protected CriteriaResult getCriteriaResultFor(AbstractEvent event, Criteria criteria) {
        return resultFactory.createCriteriaResult(criteria.getCriteriaType());
    }

}
