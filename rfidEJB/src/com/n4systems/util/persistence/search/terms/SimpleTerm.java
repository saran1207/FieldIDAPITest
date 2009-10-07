package com.n4systems.util.persistence.search.terms;

import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;

public class SimpleTerm<T> extends SingleTermDefiner {
	private static final long serialVersionUID = 1L;
	
	private String field;
	private T value;
	
	public SimpleTerm() {}
	
	public SimpleTerm(String path, T value) {
		this.field = path;
		this.value = value;
	}
	
	protected WhereParameter<T> createWhere() {
		WhereParameter<T> param = new WhereParameter<T>(WhereParameter.Comparator.EQ, field, value);
		
		// String fields are automatically ignore case
		if (value instanceof String) {
			param.setOptions(WhereParameter.IGNORE_CASE);
		}
		
		return param;
	}
	
	protected WhereClause<?> getWhereParameter() {
		return createWhere();
	}

	public String getField() {
		return field;
	}

	public void setField(String path) {
		this.field = path;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
