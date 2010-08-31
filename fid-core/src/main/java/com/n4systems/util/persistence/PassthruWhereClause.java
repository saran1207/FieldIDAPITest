package com.n4systems.util.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Query;

import com.n4systems.exceptions.InvalidQueryException;

public class PassthruWhereClause implements WhereClause<Map<String, Object>> {
	private static final long serialVersionUID = 1L;

	private final String name;
	private final Map<String, Object> params = new HashMap<String, Object>();
	private ChainOp chainOp = ChainOp.AND;
	private String clause;
	
	public PassthruWhereClause(String name, ChainOp chainOp, String clause) {
		this.name = name;
		this.clause = clause;
		
		if (chainOp != null) {
			this.chainOp = chainOp;
		}
	}

	public PassthruWhereClause(String name, String clause) {
		this(name, null, clause);
	}
	
	public PassthruWhereClause(String name) {
		this(name, null);
	}
	
	@Override
	public void bind(Query query) throws InvalidQueryException {
		for (Map.Entry<String, Object> param: params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}
	}

	@Override
	public ChainOp getChainOperator() {
		return chainOp;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<String, Object> getValue() {
		return getParams();
	}

	public Map<String, Object> getParams() {
		return params;
	}
	
	public void setClause(String clause) {
		this.clause = clause;
	}
	
	@Override
	public String getClause(FromTable table) throws InvalidQueryException {
		return clause;
	}

}
