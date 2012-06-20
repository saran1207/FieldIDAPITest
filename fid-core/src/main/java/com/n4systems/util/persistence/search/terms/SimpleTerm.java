package com.n4systems.util.persistence.search.terms;

import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;

public class SimpleTerm<T> extends SingleTermDefiner {
	private static final long serialVersionUID = 1L;

    protected boolean dropAlias = false;
	private String field;
	private T value;
    private WhereParameter.Comparator comparator;
	
	public SimpleTerm() {}

    public SimpleTerm(String path, T value) {
        this(path, value, WhereParameter.Comparator.EQ);
    }
	
	public SimpleTerm(String path, T value, WhereParameter.Comparator comparator) {
		this.field = path;
		this.value = value;
        this.comparator = comparator;
	}
	
	protected WhereParameter<T> createWhere() {
		WhereParameter<T> param = new WhereParameter<T>(comparator, field, value, dropAlias);
		
		// String fields are automatically ignore case & trim.
		if (value instanceof String) {
			param.setOptions(WhereParameter.IGNORE_CASE | WhereParameter.TRIM);
		}
		
		return param;
	}
	
	@Override
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

    public boolean isDropAlias() {
        return dropAlias;
    }

    public void setDropAlias(boolean dropAlias) {
        this.dropAlias = dropAlias;
    }

}
