package com.n4systems.util.persistence;

import javax.persistence.Query;

import com.n4systems.exceptions.InvalidQueryException;

public interface WhereClause<T> extends ClauseArgument {
	public enum ChainOp {
		AND("AND"), 
		OR("OR");
		private final String jpql;
		ChainOp(String jpql) {
			this.jpql = jpql;
		}
		public String getQueryString() {
			return jpql;
		}
		@Override
		public String toString() {
			return getQueryString();
		}
		
	}
	
	public String getName(); 
	public T getValue();
	public ChainOp getChainOperator();
	public void bind(Query query) throws InvalidQueryException;
}
