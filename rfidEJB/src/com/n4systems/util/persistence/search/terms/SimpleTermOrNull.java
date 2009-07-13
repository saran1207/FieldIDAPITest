package com.n4systems.util.persistence.search.terms;

import com.n4systems.util.persistence.WhereParameter;

public class SimpleTermOrNull<T> extends SingleTermDefiner {

	private static final long serialVersionUID = 1L;

	private String field;
	private T value;
	
	public SimpleTermOrNull() {}
	
	public SimpleTermOrNull(String path, T value) {
		this.field = path;
		this.value = value;
	}
	
	
	@Override
	protected WhereParameter<?> getWhereParameter() {
		return new WhereParameter<T>(WhereParameter.Comparator.EQ_OR_NULL, field, value);
	}

}
