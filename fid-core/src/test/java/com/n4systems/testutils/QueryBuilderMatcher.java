package com.n4systems.testutils;

import com.n4systems.util.persistence.QueryBuilder;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;

public class QueryBuilderMatcher implements IArgumentMatcher {

    private final String asString;

    public QueryBuilderMatcher(QueryBuilder queryBuilder) {
        asString = queryBuilder == null ? "<NULL>" : queryBuilder.getQueryString();
    }

    public static final QueryBuilder eq(QueryBuilder queryBuilder) {
        EasyMock.reportMatcher(new QueryBuilderMatcher(queryBuilder));
        return null;
    }

    @Override
    public String toString() {
        return asString;
    }

    @Override
    public void appendTo(StringBuffer buffer) {
        buffer.append("QUERY["+asString+"}");
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof QueryBuilder) ) {
            return false;
        }
        QueryBuilder actual = (QueryBuilder) argument;
        return actual.getQueryString().compareToIgnoreCase(asString)==0;
    }

}

