package com.n4systems.util.persistence.search.terms;

import java.util.List;

import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;

public class SimpleInTerm<T> extends SingleTermDefiner {
	private static final long serialVersionUID = 1L;

	private String field;
	private List<T> value;
	
	public SimpleInTerm() {}
	
	public SimpleInTerm(String path, List<T> value) {
		this.field = path;
		this.value = value;
	}
	
	protected WhereClause<?> getWhereParameter() {
		WhereParameter<List<T>> param = new WhereParameter<List<T>>(WhereParameter.Comparator.IN, field, value);
		return param;
	}

	public String getField() {
		return field;
	}

	public void setField(String path) {
		this.field = path;
	}

	public List<T> getValue() {
		return value;
	}

	public void setValue(List<T> value) {
		this.value = value;
	}

}
