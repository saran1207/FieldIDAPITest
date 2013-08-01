package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.model.search.WorkflowStateCriteria;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.search.JoinTerm;

import java.util.Collection;
import java.util.Set;

public class WithinUserGroupTerm extends CompleteOrIncompleteTerm {

    public static final String ASSIGNED_GROUP_JOIN_EXPRESSION = "assignedGroup";

    public static final String ASSIGNED_GROUP_JOIN_ALIAS = "theAssignedGroupOfTheEvent";

    private User user;

    public WithinUserGroupTerm(WorkflowStateCriteria workflowStateCriteria, User user) {
        super(workflowStateCriteria);
        this.user = user;
    }

    @Override
    protected void populateIncompleteTerm(WhereParameterGroup incompleteGroup) {
        WhereParameterGroup assignedToMyGroupOrToAUserInMyGroup = new WhereParameterGroup("assignedToMyGroupOrToAUserInMyGroup");
        assignedToMyGroupOrToAUserInMyGroup.setChainOperator(WhereClause.ChainOp.AND);
        Collection<User> visibleUsers = ThreadLocalInteractionContext.getInstance().getVisibleUsers();
        assignedToMyGroupOrToAUserInMyGroup.addClause(new WhereParameter<Set>(WhereParameter.Comparator.IN, "assignedGroupFilter", ASSIGNED_GROUP_JOIN_ALIAS, user.getGroups(), null, true, WhereClause.ChainOp.OR));
        assignedToMyGroupOrToAUserInMyGroup.addClause(new WhereParameter<Collection>(WhereParameter.Comparator.IN, "assigneeFilter", "assignee", visibleUsers, null, false, WhereClause.ChainOp.OR));
        incompleteGroup.addClause(assignedToMyGroupOrToAUserInMyGroup);
    }

    @Override
    protected void populateCompletedTerm(WhereParameterGroup completeGroup) {
        // Logic here is basically:
        // ... AND ( performer.id = me OR ( performer in ( myfriends ) and ( assignedGroup is null OR assignedGroup in ( mygroups )  ) )  )
        // See WEB-3667
        // where myfriends is a list of all users who share at least one group with you

        WhereParameterGroup doneByMeOrGroupVisibleByMe = new WhereParameterGroup("doneByMeOrGroupVisibleByMe");
        doneByMeOrGroupVisibleByMe.setChainOperator(WhereClause.ChainOp.AND);
        doneByMeOrGroupVisibleByMe.addClause(WhereClauseFactory.create("performedBy.id", user.getId(), WhereClause.ChainOp.OR));

        Collection<User> visibleUsers = ThreadLocalInteractionContext.getInstance().getVisibleUsers();

        WhereParameterGroup performerIsInMyGroupAndWasntAssignedOutsideMyGroup = new WhereParameterGroup("performerIsInMyGroupAndWasntAssignedOutsideMyGroup");
        performerIsInMyGroupAndWasntAssignedOutsideMyGroup.setChainOperator(WhereClause.ChainOp.OR);
        performerIsInMyGroupAndWasntAssignedOutsideMyGroup.addClause(new WhereParameter<Collection>(WhereParameter.Comparator.IN, "assigneeIsVisibleToMeFilter", "performedBy", visibleUsers, null, false, WhereClause.ChainOp.AND));

        WhereParameterGroup wasntAssignedOutsideMyGroup = new WhereParameterGroup("wasntAssignedOutsideMyGroup");
        wasntAssignedOutsideMyGroup.setChainOperator(WhereClause.ChainOp.AND);
        wasntAssignedOutsideMyGroup.addClause(new WhereParameter<Collection>(WhereParameter.Comparator.NULL, "nullAssignee", "assignedGroup", null, null, false, WhereClause.ChainOp.OR));
        wasntAssignedOutsideMyGroup.addClause(new WhereParameter<Collection>(WhereParameter.Comparator.IN, "assignedGroupWhoICanSee", "assignedGroup", user.getGroups(), null, false, WhereClause.ChainOp.OR));

        performerIsInMyGroupAndWasntAssignedOutsideMyGroup.addClause(wasntAssignedOutsideMyGroup);
        doneByMeOrGroupVisibleByMe.addClause(performerIsInMyGroupAndWasntAssignedOutsideMyGroup);

        completeGroup.addClause(doneByMeOrGroupVisibleByMe);
    }

    @Override
    public void applyToQuery(QueryBuilder queryBuilder) {
        applyJoinTerms(queryBuilder);
        super.applyToQuery(queryBuilder);
    }

    public static void applyJoinTerms(QueryBuilder queryBuilder) {
        queryBuilder.addJoin(new JoinTerm(JoinTerm.JoinTermType.LEFT_OUTER,  ASSIGNED_GROUP_JOIN_EXPRESSION, ASSIGNED_GROUP_JOIN_ALIAS, true).toJoinClause());
    }

}
