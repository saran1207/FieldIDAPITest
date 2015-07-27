package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.util.persistence.WhereParameter.Comparator;

import javax.persistence.Query;


public class NoVariableClause implements WhereClause<String> {
	private static final long serialVersionUID = 1L;

	private String name;
	private Comparator comparator;
	private String left;
	private String right;
	private ChainOp op;
    private boolean noAliasLeft = false;
	private boolean noAliasRight = false;

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

	public String getKey() { 
		return getName(); 
	}
	
	@Override
	public String getClause(FromTable table) throws InvalidQueryException {
		String comparison = table.prepareField(left, noAliasLeft)
                + " " + comparator.getOperator()
                + " " + table.prepareField(right, noAliasRight);
		return comparison;
	}

	@Override
	public void bind(Query query) throws InvalidQueryException {}
	
	@Override
	public String getValue() {
		return null;
	}

    public NoVariableClause setNoAliasLeft(boolean noAliasLeft) {
		this.noAliasLeft = noAliasLeft;
		return this;
	}

	public NoVariableClause setNoAliasRight(boolean noAliasRight) {
		this.noAliasRight = noAliasRight;
		return this;
	}
}
