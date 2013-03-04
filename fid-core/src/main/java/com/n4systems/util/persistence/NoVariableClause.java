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
    private boolean dropAlias;
	
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
		String comparison = table.prepareField(left, dropAlias)
                + " " + comparator.getOperator()
                + " " + table.prepareField(right, dropAlias);
		return comparison;
	}

	@Override
	public void bind(Query query) throws InvalidQueryException {}
	
	@Override
	public String getValue() {
		return null;
	}

    public void setDropAlias(boolean dropAlias) {
        this.dropAlias = dropAlias;
    }
}
