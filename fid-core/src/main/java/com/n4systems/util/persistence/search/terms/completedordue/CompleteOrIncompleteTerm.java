package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.model.EventSchedule;
import com.n4systems.model.search.EventStatus;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

import java.util.ArrayList;
import java.util.List;

public abstract class CompleteOrIncompleteTerm implements SearchTermDefiner {

    private EventStatus status;

    public CompleteOrIncompleteTerm() {
        this(EventStatus.ALL);
    }

    public CompleteOrIncompleteTerm(EventStatus status) {
        this.status = status;
    }

    @Override
    public final List<WhereClause<?>> getWhereParameters() {
        List<WhereClause<?>> params = new ArrayList<WhereClause<?>>();

        WhereParameterGroup outerGroup = new WhereParameterGroup(getClass().getName()+".outer");

        if (status == EventStatus.ALL) {
            createAndPopulateOuter(outerGroup);
        } else if (status == EventStatus.COMPLETE) {
            populateCompletedTerm(outerGroup);
        } else if (status == EventStatus.INCOMPLETE) {
            populateIncompleteTerm(outerGroup);
        }

        params.add(outerGroup);

        return params;
    }

    private void createAndPopulateOuter(WhereParameterGroup outerGroup) {
        WhereParameterGroup completedGroup = new WhereParameterGroup(getClass().getName()+".completed");
        completedGroup.setChainOperator(WhereClause.ChainOp.OR);
        completedGroup.addClause(WhereClauseFactory.create("status", EventSchedule.ScheduleStatus.COMPLETED, WhereClause.ChainOp.AND));

        populateCompletedTerm(completedGroup);

        WhereParameterGroup incompleteGroup = new WhereParameterGroup(getClass().getName()+".incomplete");
        incompleteGroup.setChainOperator(WhereClause.ChainOp.OR);
        incompleteGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.NE, "status", EventSchedule.ScheduleStatus.COMPLETED, WhereClause.ChainOp.AND));

        populateIncompleteTerm(incompleteGroup);

        outerGroup.addClause(completedGroup);
        outerGroup.addClause(incompleteGroup);
    }

    protected abstract void populateIncompleteTerm(WhereParameterGroup completedGroup);
    protected abstract void populateCompletedTerm(WhereParameterGroup incompleteGroup);

}
