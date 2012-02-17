package com.n4systems.util.persistence.search.terms;

import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;

public class NotNullTerm extends SingleTermDefiner {

    private String field;

    public NotNullTerm(String field) {
        this.field = field;
    }

    @Override
    protected WhereClause<?> getWhereParameter() {
        return new WhereParameter<Object>(WhereParameter.Comparator.NOTNULL, field);
    }

}
