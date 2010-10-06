package com.n4systems.util.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.n4systems.exceptions.InvalidQueryException;

public class WhereParameterGroup implements WhereClause<List<WhereClause<?>>> {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private ChainOp chainOp = ChainOp.AND;
	private List<WhereClause<?>> clauses = new ArrayList<WhereClause<?>>();

	public WhereParameterGroup() {}
	
	public WhereParameterGroup(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}	
	
	public List<WhereClause<?>> getClauses() {
		return clauses;
	}

	public void setClauses(List<WhereClause<?>> clauses) {
		this.clauses = clauses;
	}
	
	public void addClause(WhereClause<?> clause) {
		clauses.add(clause);
	}

	public void bind(Query query) throws InvalidQueryException {
		for (WhereClause<?> clause: clauses) {
			clause.bind(query);
		}
	}

	public ChainOp getChainOperator() {
		return chainOp;
	}
	
	public void setChainOperator(ChainOp chainOp) {
		this.chainOp = chainOp;
	}

	public List<WhereClause<?>> getValue() {
		return clauses;
	}
	
	public String getClause(FromTable table) throws InvalidQueryException {
		StringBuilder clauseString = new StringBuilder();
		
		if (clauses != null && !clauses.isEmpty()) {
			clauseString.append("(");
			
			boolean firstWhere = true;
			for (WhereClause<?> clause: clauses) {
				if(firstWhere) {
					firstWhere = false;
				} else {
					clauseString.append(" ").append(clause.getChainOperator()).append(" ");
				}
				clauseString.append(clause.getClause(table));
			}
			
			clauseString.append(")");
		}
		
		return clauseString.toString();
	}

	@Override
	public String getKey() {
		return getName();
	}

}
