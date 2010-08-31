package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

public class SimpleSelect extends SelectClause {
	private static final long serialVersionUID = 1L;
	
	private String param;
	private boolean dropAlias;
	
	public SimpleSelect() {}
	
	public SimpleSelect(String param) {
		this(param, false);
	}
	
	public SimpleSelect(String param, boolean dropAlias) {
		this.param = param;
		this.dropAlias = dropAlias;
	}
	
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Override
	protected String getClauseArgument(FromTable table) throws InvalidQueryException {
		return (param == null) ? table.getAlias() : table.prepareField(param, dropAlias);
	}
}
