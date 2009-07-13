package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

public class OrderClause implements ClauseArgument {
	private static final long serialVersionUID = 1L;
	
	private String param;
	private boolean ascending = true;
	
	public OrderClause() {}
	
	public OrderClause(String param, boolean ascending) {
		this.param = param;
		this.ascending = ascending;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public String getClause(FromTable table) throws InvalidQueryException {
		if(param == null) {
			throw new InvalidQueryException("Parameter is requried for an order by clause");
		}
		
		String direction = "ASC";
		if(!ascending) {
			direction = "DESC";
		}
		
		return table.prepareField(param) + " " + direction;
	}
}
