package com.n4systems.util.persistence.search.terms;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.util.persistence.WhereClause;

public abstract class SingleTermDefiner implements SearchTermDefiner {
	private static final long serialVersionUID = 1L;

	abstract protected WhereClause<?> getWhereParameter();
	
	public List<WhereClause<?>> getWhereParameters() {
		List<WhereClause<?>> params = new ArrayList<WhereClause<?>>();
		params.add(getWhereParameter());
		return params;
	}	
}
