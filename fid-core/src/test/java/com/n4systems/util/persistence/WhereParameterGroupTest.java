package com.n4systems.util.persistence;

import static org.junit.Assert.*;

import javax.persistence.Query;

import org.junit.Test;

import com.n4systems.exceptions.InvalidQueryException;

public class WhereParameterGroupTest {

	@Test
	public void empty_clause_list_returns_empty_string() {
		WhereParameterGroup whereGroup = new WhereParameterGroup();
		
		assertEquals("", whereGroup.getClause(null));
	}
	
	@SuppressWarnings("serial")
	@Test
	public void test_single_clause_generation() {
		WhereParameterGroup whereGroup = new WhereParameterGroup();
		
		whereGroup.getClauses().add(new WhereClause<Object>() {
			public void bind(Query query) throws InvalidQueryException {}

			public WhereClause.ChainOp getChainOperator() {
				return ChainOp.AND;
			}
			public String getKey() { return null; }
			public String getName() { return null; }
			public Object getValue() { return null; }

			public String getClause(FromTable table) throws InvalidQueryException {
				return "clauseA";
			}
		});
		
		assertEquals("(clauseA)", whereGroup.getClause(null));
	}
	
	@SuppressWarnings("serial")
	@Test
	public void test_multi_clause_generation() {
		WhereParameterGroup whereGroup = new WhereParameterGroup();
		
		for (String clauseText: new String[] {"clauseA", "clauseB", "clauseC"}) {
			final String clause = clauseText;
			whereGroup.getClauses().add(new WhereClause<Object>() {
				public void bind(Query query) throws InvalidQueryException {}
	
				public WhereClause.ChainOp getChainOperator() {
					return ChainOp.AND;
				}
				public String getKey() { return null; }
				public String getName() { return null; }
				public Object getValue() { return null; }
	
				public String getClause(FromTable table) throws InvalidQueryException {
					return clause;
				}
			});
		}
		
		assertEquals("(clauseA AND clauseB AND clauseC)", whereGroup.getClause(null));
	}
	
}
