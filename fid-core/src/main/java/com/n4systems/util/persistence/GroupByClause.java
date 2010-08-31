package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

public class GroupByClause implements ClauseArgument {
	private static final long serialVersionUID = 1L;
	
	private String param;
	
	public GroupByClause() {}
	
	public GroupByClause(String param) {
		this.param = param;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getClause(FromTable table) throws InvalidQueryException {
		if(param == null) {
			throw new InvalidQueryException("Parameter is requried for an group by clause");
		}
		
		return table.prepareField(param);
	}
}
