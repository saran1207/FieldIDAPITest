package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.model.search.EventStatus;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;

public class AssignedUserTerm extends CompleteOrIncompleteTerm {

    private Long assignedUserId;

    public AssignedUserTerm(EventStatus status, Long assignedUserId) {
        super(status);
        this.assignedUserId = assignedUserId;
    }

    @Override
    protected void populateIncompleteTerm(WhereParameterGroup completedGroup) {
        if (assignedUserId == 0) {
            completedGroup.addClause(new WhereParameter<Long>(WhereParameter.Comparator.NULL, "eventAssignedTo", "asset.assignedUser.id", null, null, false, WhereClause.ChainOp.AND));
        } else {
            completedGroup.addClause(new WhereParameter<Long>(WhereParameter.Comparator.EQ, "eventAssignedTo", "asset.assignedUser.id", assignedUserId, null, false, WhereClause.ChainOp.AND));
        }
    }

    @Override
    protected void populateCompletedTerm(WhereParameterGroup incompleteGroup) {
        incompleteGroup.addClause(new WhereParameter<Boolean>(WhereParameter.Comparator.EQ, "assetAssignmentApplied", "outer_event.assignedTo.assignmentApplyed", Boolean.TRUE, null, true, WhereClause.ChainOp.AND));

        if (assignedUserId == 0) {
            incompleteGroup.addClause(new WhereParameter<Long>(WhereParameter.Comparator.NULL, "assetAssignedTo", "outer_event.assignedTo.assignedUser.id", null, null, true, WhereClause.ChainOp.AND));
        } else {
            incompleteGroup.addClause(new WhereParameter<Long>(WhereParameter.Comparator.EQ, "assetAssignedTo", "outer_event.assignedTo.assignedUser.id", assignedUserId, null, true, WhereClause.ChainOp.AND));
        }
    }

}
