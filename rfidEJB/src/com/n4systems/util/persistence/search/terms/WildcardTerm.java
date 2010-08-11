package com.n4systems.util.persistence.search.terms;

import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;

public class WildcardTerm extends SimpleTerm<String> {
	private static final long serialVersionUID = 1L;
	
	private boolean wildcardLeft = false;
	private boolean wildcardRight = false;
	
	public WildcardTerm() {}

	public WildcardTerm(String path, String value) {
		super(path, value);
		modifyValueForAsterisksInSearch();
	}

	private void modifyValueForAsterisksInSearch() {
		if (getValue().endsWith("*")) {
			setValue(getValue().substring(0, getValue().length() - 1));
			wildcardRight = true;
		}
		if (getValue().startsWith("*")) {
			setValue(getValue().substring(1));
			wildcardLeft = true;
		}
	}

	@Override
    protected WhereClause<?> getWhereParameter() {
		WhereParameter<?> param = createWhere();
		
		param.setComparator(WhereParameter.Comparator.LIKE);
		if (wildcardLeft) {
			param.setOptions(param.getOptions() | WhereParameter.WILDCARD_LEFT);
		}
		if (wildcardRight) {
			param.setOptions(param.getOptions() | WhereParameter.WILDCARD_RIGHT);
		}
		
	    return param;
    }

}
