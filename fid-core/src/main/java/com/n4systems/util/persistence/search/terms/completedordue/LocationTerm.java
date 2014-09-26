package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.fieldid.service.search.SimpleOrWildcardTermFactory;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.WorkflowStateCriteria;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

public class LocationTerm extends CompleteOrIncompleteTerm {

    private Long predefinedLocationId;
    private String freeformLocation;

    public LocationTerm(WorkflowStateCriteria state, Long predefinedLocationId, String freeformLocation) {
        super(state, "");
        this.predefinedLocationId = predefinedLocationId;
        this.freeformLocation = freeformLocation;
    }

    public LocationTerm(EventReportCriteria criteria, Long predefinedLocationId) {
        super(criteria.getWorkflowState(), criteria.isShowMostRecentEventsOnly() ? "event.": "");
        this.predefinedLocationId = predefinedLocationId;
        this.freeformLocation = criteria.getLocation().getFreeformLocation();
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
            addFreeformTerm(incompleteGroup, prefix + "advancedLocation.freeformLocation", false);
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
