package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.model.search.WorkflowState;
import com.n4systems.model.user.UserGroup;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.search.JoinTerm;

public class WithinUserGroupTerm extends CompleteOrIncompleteTerm {

    public static final String ASSIGNED_GROUP_JOIN_EXPRESSION = "assignedGroup";
    public static final String GROUP_OF_THE_ASSIGNEE_JOIN_EXPRESSION = "assignee.group";
    public static final String GROUP_OF_THE_PERFORMER_JONI_EXPRESSION = "performedBy.group";

    public static final String ASSIGNED_GROUP_JOIN_ALAIS = "theAssignedGroupOfTheEvent";
    public static final String GROUP_OF_THE_ASSIGNEE_JOIN_ALIAS = "theGroupOfTheAssigneeOfTheEvent";
    public static final String GROUP_OF_THE_PERFORMER_JOIN_ALAIS = "theGroupOfThePerformerOfTheEvent";

    private UserGroup userGroup;

    public WithinUserGroupTerm(WorkflowState workflowState, UserGroup userGroup) {
        super(workflowState);
        this.userGroup = userGroup;
    }

    @Override
    protected void populateIncompleteTerm(WhereParameterGroup incompleteGroup) {
        WhereParameterGroup assignedToMyGroupOrToAUserInMyGroup = new WhereParameterGroup(getClass().getName()+".completed");
        assignedToMyGroupOrToAUserInMyGroup.setChainOperator(WhereClause.ChainOp.AND);
        assignedToMyGroupOrToAUserInMyGroup.addClause(new WhereParameter<Long>(WhereParameter.Comparator.EQ, "assignedGroupFilter", ASSIGNED_GROUP_JOIN_ALAIS +".id", userGroup.getId(), null, true, WhereClause.ChainOp.OR));
        assignedToMyGroupOrToAUserInMyGroup.addClause(new WhereParameter<Long>(WhereParameter.Comparator.EQ, "groupOfTheAssigneeFilter", GROUP_OF_THE_ASSIGNEE_JOIN_ALIAS +".id", userGroup.getId(), null, true, WhereClause.ChainOp.OR));
        incompleteGroup.addClause(assignedToMyGroupOrToAUserInMyGroup);
    }

    @Override
    protected void populateCompletedTerm(WhereParameterGroup completeGroup) {
        completeGroup.addClause(new WhereParameter<Long>(WhereParameter.Comparator.EQ, "groupOfThePerformerFilter", GROUP_OF_THE_PERFORMER_JOIN_ALAIS +".id", userGroup.getId(), null, true, WhereClause.ChainOp.AND));
    }

    @Override
    public void applyToQuery(QueryBuilder queryBuilder) {
        applyJoinTerms(queryBuilder);
        super.applyToQuery(queryBuilder);
    }

    public static void applyJoinTerms(QueryBuilder queryBuilder) {
        queryBuilder.addJoin(new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER,  ASSIGNED_GROUP_JOIN_EXPRESSION, ASSIGNED_GROUP_JOIN_ALAIS, true).toJoinClause());
        queryBuilder.addJoin(new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER, GROUP_OF_THE_ASSIGNEE_JOIN_EXPRESSION, GROUP_OF_THE_ASSIGNEE_JOIN_ALIAS, true).toJoinClause());
        queryBuilder.addJoin(new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER, GROUP_OF_THE_PERFORMER_JONI_EXPRESSION, GROUP_OF_THE_PERFORMER_JOIN_ALAIS, true).toJoinClause());
    }

}
