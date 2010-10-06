package com.n4systems.util.persistence.customclauses;

import java.util.List;

import javax.persistence.Query;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.util.persistence.FromTable;
import com.n4systems.util.persistence.WhereClause;

public class PredefinedLocationInClause implements WhereClause<List<Long>> {
	private static final long serialVersionUID = 1L;

	private final String joinAlias;
	private final List<Long> predefinedLocationIds;
	private final ChainOp chainOp;
	
	public PredefinedLocationInClause(String path, List<Long> predefinedLocationIds, ChainOp chainOp) {
		this.joinAlias = path;
		this.predefinedLocationIds = predefinedLocationIds;
		this.chainOp = chainOp;
	}

	@Override
	public void bind(Query query) throws InvalidQueryException {
		query.setParameter(getName(), predefinedLocationIds);
	}

	@Override
	public ChainOp getChainOperator() {
		return chainOp;
	}

	@Override
	public String getName() {
		return joinAlias;
	}

	@Override
	public List<Long> getValue() {
		return predefinedLocationIds;
	}

	@Override
	public String getClause(FromTable table) throws InvalidQueryException {
		return String.format("%s IN (:%s)", joinAlias, getName());
	}

	@Override
	public String getKey() {
		return getName();
	}
}
