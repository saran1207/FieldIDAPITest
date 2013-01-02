package com.n4systems.testutils;

import com.n4systems.util.persistence.QueryBuilder;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;

public class QueryMatcher implements IArgumentMatcher {

    private Class<?> queryType;

    public QueryMatcher(Class<?> queryType) {
        this.queryType = queryType;
    }

    public static final QueryBuilder eq(Class<?> queryType) {
        EasyMock.reportMatcher(new QueryMatcher(queryType));
        return null;
    }

    @Override
    public String toString() {
        return "Query{" +
                "queryType=" + queryType +
                '}';
    }

    @Override
    public void appendTo(StringBuffer buffer) {
        buffer.append(toString());
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof QueryBuilder) ) {
            return false;
        }
        QueryBuilder actual = (QueryBuilder) argument;
        return actual.getFromArgument().getTableClass().equals(queryType);
    }

}

