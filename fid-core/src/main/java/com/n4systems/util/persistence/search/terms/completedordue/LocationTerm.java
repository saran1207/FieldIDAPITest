package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.model.search.EventStatus;
import com.n4systems.util.persistence.WhereParameterGroup;

public class LocationTerm extends CompleteOrIncompleteTerm {

    public LocationTerm(EventStatus status) {
        super(status);
    }

    @Override
    protected void populateIncompleteTerm(WhereParameterGroup completedGroup) {
    }

    @Override
    protected void populateCompletedTerm(WhereParameterGroup incompleteGroup) {
    }

}
