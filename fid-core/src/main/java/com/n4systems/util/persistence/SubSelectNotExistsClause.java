package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

import javax.persistence.Query;

public class SubSelectNotExistsClause implements WhereClause<QueryBuilder<?>> {

    private String param;
    private QueryBuilder<?> subQuery;
    private ChainOp chainOp = ChainOp.AND;

    public SubSelectNotExistsClause(String param, QueryBuilder<?> subQuery) {
        this.param = param;
        this.subQuery = subQuery;
    }

    @Override
    public String getName() {
        return (param != null) ? param.replace('.', '_') : null;
    }

    @Override
    public QueryBuilder<?> getValue() {
        return subQuery;
    }

    @Override
    public ChainOp getChainOperator() {
        return chainOp;
    }

    @Override
    public void bind(Query query) throws InvalidQueryException {
        subQuery.bindParams(query);
    }

    @Override
    public String getClause(FromTable table) throws InvalidQueryException {
        // Total hack below.  We don't want the alias of our sub query to match our main query.
		subQuery.getFromArgument().setAlias("sub");

		return " NOT EXISTS ( " + subQuery.getQueryString() + " )";
    }
}
