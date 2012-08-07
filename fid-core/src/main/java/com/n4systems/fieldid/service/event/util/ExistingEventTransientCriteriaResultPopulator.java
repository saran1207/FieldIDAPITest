package com.n4systems.fieldid.service.event.util;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;

public class ExistingEventTransientCriteriaResultPopulator extends TransientCriteriaResultPopulator {

    @Override
    protected CriteriaResult getCriteriaResultFor(AbstractEvent event, Criteria criteria) {
        for (CriteriaResult criteriaResult : event.getResults()) {
            if (criteriaResult.getCriteria().getId().equals(criteria.getId())) {
                return criteriaResult;
            }
        }
        return null;
    }

}
