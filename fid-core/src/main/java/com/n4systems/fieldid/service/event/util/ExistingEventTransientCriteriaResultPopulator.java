package com.n4systems.fieldid.service.event.util;

import com.n4systems.model.*;

public class ExistingEventTransientCriteriaResultPopulator extends TransientCriteriaResultPopulator {

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
