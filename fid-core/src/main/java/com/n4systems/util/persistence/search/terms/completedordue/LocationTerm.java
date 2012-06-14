package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.model.search.EventState;
import com.n4systems.util.persistence.WhereParameterGroup;

public class LocationTerm extends CompleteOrIncompleteTerm {

    public LocationTerm(EventState state) {
        super(state);
    }

    @Override
    protected void populateIncompleteTerm(WhereParameterGroup completedGroup) {
    }

    @Override
    protected void populateCompletedTerm(WhereParameterGroup incompleteGroup) {
    }

}
