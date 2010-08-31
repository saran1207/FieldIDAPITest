package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

@SuppressWarnings("serial")
public abstract class SelectClause implements ClauseArgument {
	private boolean distinct = false;
	
	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
	
	public String getClause(FromTable table) throws InvalidQueryException {
		String distinctOper = "";
		if(distinct) {
			distinctOper = "DISTINCT ";
		}
		
		return "SELECT " + distinctOper + getClauseArgument(table);
	}
	
	abstract protected String getClauseArgument(FromTable table) throws InvalidQueryException;
}
