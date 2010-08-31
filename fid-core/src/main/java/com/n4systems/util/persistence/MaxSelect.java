package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

public class MaxSelect extends SelectClause {
	private static final long serialVersionUID = 1L;

	private String field = "*";
	
	public MaxSelect() {}
	
	public MaxSelect(String field) {
		this.field = field;
	}
	
	public String getField() {
		return field;
	}

	public void setField(String countArg) {
		this.field = countArg;
	}

	@Override
	protected String getClauseArgument(FromTable table) throws InvalidQueryException {
		return "max(" + table.prepareField(field) + ")";
	}

}
