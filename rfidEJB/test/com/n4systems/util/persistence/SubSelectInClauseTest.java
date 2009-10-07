package com.n4systems.util.persistence;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.exceptions.InvalidQueryException;

public class SubSelectInClauseTest {
	
	@Test
	public void test_generated_query() {
		QueryBuilder<Long> subQuery = new QueryBuilder<Long>(null) {
			@Override
			public String getQueryString() throws InvalidQueryException {
				return "sub_query";
			}
		};
		
		SubSelectInClause subSelectClause = new SubSelectInClause("subfield", subQuery);
		
		assertEquals("o.subfield IN ( sub_query )", subSelectClause.getClause(new FromTable(Object.class, "o")));
	}
}
