package com.n4systems.util.persistence.search.terms;

import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;

public class WildcardTerm extends SimpleTerm<String> {
	private static final long serialVersionUID = 1L;
	
	public WildcardTerm() {
		super();
	}

	public WildcardTerm(String path, String value) {
		super(path, value);
	}

	@Override
    protected WhereClause<?> getWhereParameter() {
		WhereParameter<?> param = createWhere();
		
		param.setComparator(WhereParameter.Comparator.LIKE);
		param.setOptions(param.getOptions() | WhereParameter.WILDCARD_RIGHT);
		
	    return param;
    }
}
