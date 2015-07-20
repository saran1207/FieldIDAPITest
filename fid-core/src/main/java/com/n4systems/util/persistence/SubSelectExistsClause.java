package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

import javax.persistence.Query;

public class SubSelectExistsClause implements WhereClause<QueryBuilder<?>> {

	private String name;
	private QueryBuilder<?> subQuery;
	private ChainOp chainOp;
	private boolean exists;

	public SubSelectExistsClause(String name, QueryBuilder<?> subQuery, boolean exists) {
		this(name, subQuery, exists, ChainOp.AND);
	}

	public SubSelectExistsClause(String name, QueryBuilder<?> subQuery, boolean exists, ChainOp chainOp) {
		this.name = name;
		this.subQuery = subQuery;
		this.exists = exists;
		this.chainOp = chainOp;
	}

	@Override
	public String getName() {
		return name;
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
		// the alias of the sub query cannot match the main
		if (subQuery.getFromArgument().getAlias().equals(table.getAlias())) {
			subQuery.getFromArgument().setAlias("sub");
		}

		return (exists ? " " : " NOT ") + "EXISTS ( " + subQuery.getQueryString() + " )";
	}

	@Override
	public String getKey() {
		return getName();
	}
}
