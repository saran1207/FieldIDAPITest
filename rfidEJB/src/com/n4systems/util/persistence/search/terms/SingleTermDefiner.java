package com.n4systems.util.persistence.search.terms;

import com.n4systems.util.persistence.WhereParameter;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleTermDefiner implements SearchTermDefiner {
	private static final long serialVersionUID = 1L;

	abstract protected WhereParameter<?> getWhereParameter();
	
	public List<WhereParameter<?>> getWhereParameters() {
		List<WhereParameter<?>> params = new ArrayList<WhereParameter<?>>();
		params.add(getWhereParameter());
		return params;
	}	
}
