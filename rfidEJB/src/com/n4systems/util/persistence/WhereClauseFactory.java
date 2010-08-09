package com.n4systems.util.persistence;

import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class WhereClauseFactory {

	private WhereClauseFactory() {}
	
	public static <T> WhereClause<T> create(Comparator comparator, String name, String param, T value, Integer options, ChainOp chainOp) {
		WhereParameter<T> where = new WhereParameter<T>() ;
		where.setParam(param);
		where.setValue(value);
		
		if (comparator != null) {
			where.setComparator(comparator);
		}
		
		if (name != null) {
			where.setName(name);
		}
		
		if (options != null) {
			where.setOptions(options);
		}
		
		if (chainOp != null) {
			where.setChainOperator(chainOp);
		}
		
		return where;
	}
	
	public static <T> WhereClause<T> create(Comparator comparator, String param, T value, Integer options, ChainOp chainOp) {
		return create(comparator, null, param, value, options, chainOp);
	}

	public static <T> WhereClause<T> create(String param, T value, ChainOp chainOp) {
		return create(null, null, param, value, null, chainOp);
	}

	/*
	 * NOTE: the name has been moved to the last parameter here since if it were first, it could create an ambiguous situation
	 * with the method create(String param, T value, Integer options, ChainOp chainOp) in the case when you call
	 * create(String, String, Integer, ChainOp)
	 */
	public static <T> WhereClause<T> create(String param, T value, ChainOp chainOp, String name) {  
		return create(null, name, param, value, null, chainOp);
	}
	
	public static <T> WhereClause<T> create(Comparator comparator, String name, String param, T value) {
		return create(comparator, name, param, value, null, null);
	}
	
	public static <T> WhereClause<T> create(Comparator comparator, String param, T value) {
		return create(comparator, null, param, value, null, null);
	}
	
	public static <T> WhereClause<T> create(Comparator comparator, String param, T value, ChainOp chainOp) {
		return create(comparator, null, param, value, null, chainOp);
	}
	
	public static <T> WhereClause<T> create(String param, T value) {
		return create(null, null, param, value, null, null);
	}
	
	public static <T> WhereClause<T> create(String name, String param, T value) {
		return create(null, name, param, value, null, null);
	}
	
	public static <T> WhereClause<T> create(String param, T value, Integer options, ChainOp chainOp) {
		return create(null, null, param, value, options, chainOp);
	}
	
	public static WhereClause<Object> createIsNull(String param) {
		return create(Comparator.NULL, null, param, null, null, null);
	}
	
	public static WhereClause<Object> createNotNull(String param) {
		return create(Comparator.NOTNULL, null, param, null, null, null);
	}
	
	public static WhereClause<String> createNoVariable(String leftSize, String rightSide) {
		return new NoVariableClause(leftSize+rightSide, Comparator.EQ, leftSize, rightSide, ChainOp.AND);
	}
}
