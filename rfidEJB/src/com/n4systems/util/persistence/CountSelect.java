package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

public class CountSelect extends SelectClause {
	private static final long serialVersionUID = 1L;

	private String countArg = "*";
	
	public CountSelect() {}
	
	public CountSelect(String countArg) {
		this.countArg = countArg;
	}
	
	public String getCountArg() {
		return countArg;
	}

	public void setCountArg(String countArg) {
		this.countArg = countArg;
	}

	@Override
	protected String getClauseArgument(FromTable table) throws InvalidQueryException {
		return "count(" + countArg + ")";
	}

}
