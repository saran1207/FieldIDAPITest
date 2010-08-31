package com.n4systems.util.persistence;

public interface QueryFilter {
	
	/**
	 * Applies where clauses to a QueryBuilder.
	 * @param builder	A QueryBuilder to prepare
	 */
	public void applyFilter(QueryBuilder<?> builder);
	
}
