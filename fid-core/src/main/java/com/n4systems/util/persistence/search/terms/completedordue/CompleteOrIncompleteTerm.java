package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.model.Event;
import com.n4systems.model.search.EventState;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

import java.util.ArrayList;
import java.util.List;

public abstract class CompleteOrIncompleteTerm implements SearchTermDefiner {

    private EventState state;

    public CompleteOrIncompleteTerm() {
        this(EventState.ALL);
    }

    public CompleteOrIncompleteTerm(EventState state) {
        this.state = state;
    }

    @Override
    public final List<WhereClause<?>> getWhereParameters() {
        List<WhereClause<?>> params = new ArrayList<WhereClause<?>>();

        WhereParameterGroup outerGroup = new WhereParameterGroup(getClass().getName()+".outer");

        if (state == EventState.ALL) {
            createAndPopulateCompleteAndIncompleteOuterGroup(outerGroup);
        } else if (state == EventState.COMPLETE) {
            populateCompletedTerm(outerGroup);
        } else if (state == EventState.OPEN) {
            populateIncompleteTerm(outerGroup);
        }

        params.add(outerGroup);

        return params;
    }

    private void createAndPopulateCompleteAndIncompleteOuterGroup(WhereParameterGroup outerGroup) {
        WhereParameterGroup completedGroup = new WhereParameterGroup(getClass().getName()+".completed");
        completedGroup.setChainOperator(WhereClause.ChainOp.OR);
        completedGroup.addClause(WhereClauseFactory.create("eventState", Event.EventState.COMPLETED, WhereClause.ChainOp.AND));

        populateCompletedTerm(completedGroup);

        WhereParameterGroup incompleteGroup = new WhereParameterGroup(getClass().getName()+".incomplete");
        incompleteGroup.setChainOperator(WhereClause.ChainOp.OR);
        incompleteGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.NE, "eventState", Event.EventState.COMPLETED, WhereClause.ChainOp.AND));

        populateIncompleteTerm(incompleteGroup);

        outerGroup.addClause(completedGroup);
        outerGroup.addClause(incompleteGroup);
    }

    protected abstract void populateIncompleteTerm(WhereParameterGroup completedGroup);
    protected abstract void populateCompletedTerm(WhereParameterGroup incompleteGroup);

}
