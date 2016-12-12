package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;
import org.junit.Test;

import javax.persistence.Query;

import static org.junit.Assert.assertEquals;

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
			@Override
			public void bind(Query query) throws InvalidQueryException {}

			@Override
			public WhereClause.ChainOp getChainOperator() {
				return ChainOp.AND;
			}
			@Override
			public String getKey() { return null; }
			@Override
			public String getName() { return null; }
			@Override
			public Object getValue() { return null; }

			@Override
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
				@Override
				public void bind(Query query) throws InvalidQueryException {}
	
				@Override
				public WhereClause.ChainOp getChainOperator() {
					return ChainOp.AND;
				}
				@Override
				public String getKey() { return null; }
				@Override
				public String getName() { return null; }
				@Override
				public Object getValue() { return null; }
	
				@Override
				public String getClause(FromTable table) throws InvalidQueryException {
					return clause;
				}
			});
		}
		
		assertEquals("(clauseA AND clauseB AND clauseC)", whereGroup.getClause(null));
	}
	
	
	
	
}
