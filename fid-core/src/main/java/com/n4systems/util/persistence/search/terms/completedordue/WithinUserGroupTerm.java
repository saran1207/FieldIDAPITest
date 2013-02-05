package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.model.search.WorkflowState;
import com.n4systems.model.user.UserGroup;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.search.JoinTerm;

public class WithinUserGroupTerm extends CompleteOrIncompleteTerm {

    public static final String ASSIGNEE_GROUP_JOIN_EXPRESSION = "assignee.group";
    public static final String PERFORMEDBY_GROUP_JOIN_EXPRESSION = "performedBy.group";

    public static final String ASSIGNEE_JOIN_ALAIS = "theAssigneeOfTheEvent";
    public static final String PERFORMEDBY_JOIN_ALAIS = "thePerformerOfTheEvent";

    private UserGroup userGroup;

    public WithinUserGroupTerm(WorkflowState workflowState, UserGroup userGroup) {
        super(workflowState);
        this.userGroup = userGroup;
    }

    @Override
    protected void populateIncompleteTerm(WhereParameterGroup completedGroup) {
        // TODO: Assigned Group.
        completedGroup.addClause(new WhereParameter<Long>(WhereParameter.Comparator.EQ, "assigneeGroup", ASSIGNEE_JOIN_ALAIS+".id", userGroup.getId(), null, true, WhereClause.ChainOp.AND));
    }

    @Override
    protected void populateCompletedTerm(WhereParameterGroup incompleteGroup) {
        incompleteGroup.addClause(new WhereParameter<Long>(WhereParameter.Comparator.EQ, "performedByGroup", PERFORMEDBY_JOIN_ALAIS+".id", userGroup.getId(), null, true, WhereClause.ChainOp.AND));
    }

    @Override
    public void applyToQuery(QueryBuilder queryBuilder) {
        applyJoinTerms(queryBuilder);
        super.applyToQuery(queryBuilder);
    }

    public static void applyJoinTerms(QueryBuilder queryBuilder) {
        queryBuilder.addJoin(new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER,  ASSIGNEE_GROUP_JOIN_EXPRESSION, ASSIGNEE_JOIN_ALAIS, true).toJoinClause());
        queryBuilder.addJoin(new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER,  PERFORMEDBY_GROUP_JOIN_EXPRESSION, PERFORMEDBY_JOIN_ALAIS, true).toJoinClause());
    }

}
