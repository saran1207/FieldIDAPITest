package com.n4systems.util.persistence;

import javax.persistence.Query;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.util.persistence.WhereParameter.Comparator;


public class NoVariableClause implements WhereClause<String> {
	private static final long serialVersionUID = 1L;

	private String name;
	private Comparator comparator;
	private String left;
	private String right;
	private ChainOp op;
	
	public NoVariableClause(String name, Comparator comparator, String left, String right, ChainOp op) {
		this.name = name;
		this.comparator = comparator;
		this.left = left;
		this.right = right;
		this.op = op;
	}
	
	@Override
	public ChainOp getChainOperator() {
		return op;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getClause(FromTable table) throws InvalidQueryException {
		String comparison = table.prepareField(left) + " " + comparator.getOperator() + " " + table.prepareField(right);
		return comparison;
	}

	@Override
	public void bind(Query query) throws InvalidQueryException {}
	
	@Override
	public String getValue() {
		return null;
	}
}
