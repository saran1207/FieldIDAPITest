package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

public class GroupByClause implements ClauseArgument {
	private static final long serialVersionUID = 1L;
	
	private String param;
	private boolean dropAlias;
	
	public GroupByClause() {
		this(null, false);
	}
	
	public GroupByClause(String param, boolean dropAlias) {
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
	public String getClause(FromTable table) throws InvalidQueryException {
		if(param == null) {
			throw new InvalidQueryException("Parameter is requried for an group by clause");
		}
		
		return table.prepareField(param, dropAlias);
	}
}
