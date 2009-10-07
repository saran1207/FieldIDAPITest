package com.n4systems.util.persistence;

import javax.persistence.Query;

import com.n4systems.exceptions.InvalidQueryException;

// XXX this is a hack to create a sub select where clause.  It's got logic copied from WhereParameter
// really where parameter should be able to handle a sub select QueryBuilder as a value
public class SubSelectInClause implements WhereClause<QueryBuilder<?>> {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String param;
	private ChainOp chainOp = ChainOp.AND;
	private QueryBuilder<?> subSelect;
	
	public SubSelectInClause() {}
	
	public SubSelectInClause(String param) {
		this(param, null);
	}
	
	public SubSelectInClause(String param, QueryBuilder<?> subSelect) {
		this.param = param;
		this.subSelect = subSelect;
	}
	
	public String getName() {
		// if name is null, use the param name (or null)
		return (name != null) ? name : (param != null) ? param.replace('.', '_') : null;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getParam() {
		return param;
	}
	
	public void setParam(String param) {
		this.param = param;
	}
	
	public ChainOp getChainOperator() {
		return chainOp;
	}

	public void setChainOperator(ChainOp chainOp) {
		this.chainOp = chainOp;
	}

	public QueryBuilder<?> getValue() {
		return subSelect;
	}
	
	public void setSubSelect(QueryBuilder<?> subSelect) {
		this.subSelect = subSelect;
	}
	
	public String getClause(FromTable table) throws InvalidQueryException {
		// Total hack below.  We don't want the alias of our sub query to match our main query.
		subSelect.getFromArgument().setAlias("sub");
		
		String clause = table.prepareField(param) + " IN ( " + subSelect.getQueryString() + " )";
		return clause;
	}
	
	public void bind(Query query) throws InvalidQueryException {
		subSelect.bindParams(query);
	}
}
