package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.fieldid.service.search.SimpleOrWildcardTermFactory;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.model.search.EventState;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

public class LocationTerm extends CompleteOrIncompleteTerm {

    private Long predefinedLocationId;
    private String freeformLocation;

    public LocationTerm(EventState state, Long predefinedLocationId, String freeformLocation) {
        super(state);
        this.predefinedLocationId = predefinedLocationId;
        this.freeformLocation = freeformLocation;
    }

    @Override
    protected void populateIncompleteTerm(WhereParameterGroup completedGroup) {
        if (freeformLocation != null) {
            addFreeformTerm(completedGroup, "asset.advancedLocation.freeformLocation", false);
        }

        if (predefinedLocationId != null) {
            addPredefTerm(completedGroup, "assetPreLocSearchId");
        }
    }

    @Override
    protected void populateCompletedTerm(WhereParameterGroup incompleteGroup) {
        if (freeformLocation != null) {
            addFreeformTerm(incompleteGroup, "advancedLocation.freeformLocation", false);
        }

        if (predefinedLocationId != null) {
            addPredefTerm(incompleteGroup, "eventPreLocSearchId");
        }
    }

    private void addFreeformTerm(WhereParameterGroup group, String path, boolean dropAlias) {
        SearchTermDefiner freeformLocationTerm = SimpleOrWildcardTermFactory.createSimpleOrWildcardTerm(path, freeformLocation, dropAlias);
        if (freeformLocationTerm != null) {
            for (WhereClause<?> whereClause : freeformLocationTerm.getWhereParameters()) {
                group.addClause(whereClause);
            }
        }
    }

    private void addPredefTerm(WhereParameterGroup group, String path) {
        PredefinedLocationSearchTerm predefTerm = new PredefinedLocationSearchTerm(path, predefinedLocationId);
        for (WhereClause<?> whereClause : predefTerm.getWhereParameters()) {
            group.addClause(whereClause);
        }
    }

}
