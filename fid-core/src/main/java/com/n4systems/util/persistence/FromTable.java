package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;


public class FromTable {
	private static final long serialVersionUID = 1L;
	
	private Class<?> tableClass;
	private String alias;
	
	public FromTable() {}
	
	public FromTable(Class<?> tableClass, String alias) {
		this.tableClass = tableClass;
		this.alias = alias;
	}
	
	public Class<?> getTableClass() {
		return tableClass;
	}

	public void setTableClass(Class<?> tableClass) {
		this.tableClass = tableClass;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getClause() throws InvalidQueryException {
		if(tableClass == null || alias == null) {
			throw new InvalidQueryException("Both the table class and alias are required for a FromTable");
		}
		
		return tableClass.getName() + " " + alias;
	}
	
	public String prepareField(String field) {
		return prepareField(field, false);
	}
	public String prepareField(String field, boolean dropAlias) {
		return ((dropAlias) ? "" : alias + ".") + field ;
	}
}

