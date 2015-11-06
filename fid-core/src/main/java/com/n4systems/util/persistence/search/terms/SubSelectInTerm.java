package com.n4systems.util.persistence.search.terms;

import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SubSelectInClause;
import com.n4systems.util.persistence.WhereClause;

public class SubSelectInTerm extends SingleTermDefiner {

    String value;
    QueryBuilder subQuery;

    public SubSelectInTerm(String value, QueryBuilder subQuery) {
        this.value = value;
        this.subQuery = subQuery;
    }

    @Override
    protected WhereClause<?> getWhereParameter() {
        return new SubSelectInClause("id", subQuery);
    }
}
