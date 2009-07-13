package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;


public class FetchClause implements ClauseArgument {
	private static final long serialVersionUID = 1L;
	
	private String param;
	
	public FetchClause() {}
	
	public FetchClause(String param) {
		this.param = param;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getClause(FromTable table) throws InvalidQueryException {
		if(param == null || param.length() < 1) {
			throw new InvalidQueryException("The fetch parameter is requried for a FetchClause");
		}
		
		return "LEFT JOIN FETCH " + table.prepareField(param);
	}
	
}
