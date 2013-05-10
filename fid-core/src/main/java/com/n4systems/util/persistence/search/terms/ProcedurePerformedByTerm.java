package com.n4systems.util.persistence.search.terms;

import com.n4systems.model.user.User;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameterGroup;

import java.util.ArrayList;
import java.util.List;

public class ProcedurePerformedByTerm implements SearchTermDefiner {

    private User performer;

    public ProcedurePerformedByTerm(User performer) {
        this.performer = performer;
    }

    @Override
    public List<WhereClause<?>> getWhereParameters() {
        List<WhereClause<?>> clauses = new ArrayList<WhereClause<?>>();
        WhereParameterGroup performedByGroup = new WhereParameterGroup();
        performedByGroup.addClause(WhereClauseFactory.create("lockedBy", performer, WhereClause.ChainOp.OR));
        performedByGroup.addClause(WhereClauseFactory.create("unlockedBy", performer, WhereClause.ChainOp.OR));

        clauses.add(performedByGroup);
        return clauses;
    }

}
