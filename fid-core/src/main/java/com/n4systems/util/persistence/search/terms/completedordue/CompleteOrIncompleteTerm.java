package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.model.search.WorkflowStateCriteria;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

import java.util.ArrayList;
import java.util.List;

public abstract class CompleteOrIncompleteTerm implements SearchTermDefiner {

    private WorkflowStateCriteria state;

    public CompleteOrIncompleteTerm() {
        this(WorkflowStateCriteria.ALL);
    }

    public CompleteOrIncompleteTerm(WorkflowStateCriteria state) {
        this.state = state;
    }

    @Override
    public final List<WhereClause<?>> getWhereParameters() {
        List<WhereClause<?>> params = new ArrayList<WhereClause<?>>();

        WhereParameterGroup outerGroup = new WhereParameterGroup(getClass().getName()+".outer");

        if (state == null || state == WorkflowStateCriteria.ALL) {
            createAndPopulateCompleteAndIncompleteOuterGroup(outerGroup);
        } else if (state == WorkflowStateCriteria.COMPLETE) {
            populateCompletedTerm(outerGroup);
        } else {
            // Either it's OPEN or CLOSED, which are treated the same for reporting purposes for now
            // (ie our criteria apply to the asset rather than the event).
            populateIncompleteTerm(outerGroup);
        }

        params.add(outerGroup);

        return params;
    }

    private void createAndPopulateCompleteAndIncompleteOuterGroup(WhereParameterGroup outerGroup) {
        WhereParameterGroup completedGroup = new WhereParameterGroup(getClass().getName()+".completed");
        completedGroup.setChainOperator(WhereClause.ChainOp.OR);
        completedGroup.addClause(WhereClauseFactory.create("workflowState", com.n4systems.model.WorkflowState.COMPLETED, WhereClause.ChainOp.AND, "completedWorkflowState"));

        populateCompletedTerm(completedGroup);

        WhereParameterGroup incompleteGroup = new WhereParameterGroup(getClass().getName()+".incomplete");
        incompleteGroup.setChainOperator(WhereClause.ChainOp.OR);
        incompleteGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.NE, "workflowState", com.n4systems.model.WorkflowState.COMPLETED, WhereClause.ChainOp.AND, "nonCompletedWorkflowState"));

        populateIncompleteTerm(incompleteGroup);

        outerGroup.addClause(completedGroup);
        outerGroup.addClause(incompleteGroup);
    }

    public void applyToQuery(QueryBuilder queryBuilder) {
        for (WhereClause<?> whereClause : getWhereParameters()) {
            queryBuilder.addWhere(whereClause);
        }
    }

    protected abstract void populateIncompleteTerm(WhereParameterGroup completedGroup);
    protected abstract void populateCompletedTerm(WhereParameterGroup incompleteGroup);

}
