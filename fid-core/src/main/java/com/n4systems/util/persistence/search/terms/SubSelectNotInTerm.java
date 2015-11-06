package com.n4systems.util.persistence.search.terms;

import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SubSelectNotInClause;
import com.n4systems.util.persistence.WhereClause;

public class SubSelectNotInTerm extends SingleTermDefiner {

    String value;
    QueryBuilder subQuery;

    public SubSelectNotInTerm(String value, QueryBuilder subQuery) {
        this.value = value;
        this.subQuery = subQuery;
    }

    @Override
    protected WhereClause<?> getWhereParameter() {
        return new SubSelectNotInClause("id", subQuery);
    }
}
