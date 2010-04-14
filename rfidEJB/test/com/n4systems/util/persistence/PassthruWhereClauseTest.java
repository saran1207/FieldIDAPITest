package com.n4systems.util.persistence;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import javax.persistence.Query;

import org.junit.Test;

public class PassthruWhereClauseTest {

	@Test
	public void bind_binds_map_params() {
		PassthruWhereClause clause = new PassthruWhereClause("clause name");
		
		clause.getParams().put("field1", "val1");
		clause.getParams().put("field2", 2);
		
		Query query = createMock(Query.class);
		expect(query.setParameter("field1", "val1")).andReturn(query);
		expect(query.setParameter("field2", 2)).andReturn(query);
		replay(query);
		
		clause.bind(query);
		
		verify(query);
	}
	
	@Test
	public void get_clause_leaves_clause_unmodified() {
		String clauseStr = "SELECT hat FROM cat";
		
		PassthruWhereClause clause = new PassthruWhereClause("clause name");
		clause.setClause(clauseStr);
		
		assertEquals(clauseStr, clause.getClause(null));
	}
}
