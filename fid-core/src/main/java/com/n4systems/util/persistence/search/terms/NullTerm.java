package com.n4systems.util.persistence.search.terms;

import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;

public class NullTerm extends SingleTermDefiner {
	private static final long serialVersionUID = 1L;
	
	private String field;
	
	public NullTerm() {}
	
	public NullTerm(String field) {
		this.field = field;
	}
	
	protected WhereClause<?> getWhereParameter() {
		return new WhereParameter<Object>(WhereParameter.Comparator.NULL, field);
	}

	public String getField() {
		return field;
	}

	public void setField(String path) {
		this.field = path;
	}
}
