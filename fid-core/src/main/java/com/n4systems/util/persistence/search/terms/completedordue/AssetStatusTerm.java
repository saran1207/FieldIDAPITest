package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.WorkflowStateCriteria;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;

public class AssetStatusTerm extends CompleteOrIncompleteTerm {

    private AssetStatus assetStatus;

    public AssetStatusTerm(WorkflowStateCriteria state, AssetStatus assetStatus) {
        super(state, "");
        this.assetStatus = assetStatus;
    }

    public AssetStatusTerm(EventReportCriteria criteria) {
        super(criteria.getWorkflowState(), criteria.isShowMostRecentEventsOnly() ? "event.": "");
        this.assetStatus = criteria.getAssetStatus();
    }

    @Override
    protected void populateIncompleteTerm(WhereParameterGroup completedGroup) {
        completedGroup.addClause(new WhereParameter<Long>(WhereParameter.Comparator.EQ, "assetStatus", "asset.assetStatus.id", assetStatus.getId(), null, false, WhereClause.ChainOp.AND));
    }

    @Override
    protected void populateCompletedTerm(WhereParameterGroup incompleteGroup) {
        incompleteGroup.addClause(new WhereParameter<Long>(WhereParameter.Comparator.EQ, "assetStatus", prefix + "assetStatus.id", assetStatus.getId(), null, false, WhereClause.ChainOp.AND));
    }

}
