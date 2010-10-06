package com.n4systems.model.location;

import javax.persistence.Query;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.util.persistence.FromTable;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.search.terms.SingleTermDefiner;

public class PredefinedLocationSearchTerm extends SingleTermDefiner implements WhereClause<Long> {
	private static final long serialVersionUID = 1L;

	private final String joinAlias;
	private final Long predefinedLocationId;
	
	public PredefinedLocationSearchTerm(String path, Long predefinedLocationId) {
		this.joinAlias = path;
		this.predefinedLocationId = predefinedLocationId;
	}
	
	@Override
	protected WhereClause<?> getWhereParameter() {
		return this;
	}

	@Override
	public void bind(Query query) throws InvalidQueryException {
		query.setParameter(getName(), predefinedLocationId);
	}

	@Override
	public ChainOp getChainOperator() {
		return ChainOp.AND;
	}

	@Override
	public String getName() {
		return joinAlias;
	}

	@Override
	public Long getValue() {
		return predefinedLocationId;
	}

	@Override
	public String getClause(FromTable table) throws InvalidQueryException {
		return String.format("%s = :%s", joinAlias, getName());
	}

	@Override
	public String getKey() {
		return getName();
	}

}
