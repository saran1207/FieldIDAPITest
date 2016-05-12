package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.api.UnsecuredEntity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubSelectInClauseTest {
	
	@Test
	public void test_generated_query() {
		QueryBuilder<Long> subQuery = new QueryBuilder<Long>(UnsecuredEntity.class) {
			@Override
			public String getQueryString() throws InvalidQueryException {
				return "sub_query";
			}
		};
		
		SubSelectInClause subSelectClause = new SubSelectInClause("subfield", subQuery);
		
		assertEquals("o.subfield IN ( sub_query )", subSelectClause.getClause(new FromTable(Object.class, "o")));
	}
}
