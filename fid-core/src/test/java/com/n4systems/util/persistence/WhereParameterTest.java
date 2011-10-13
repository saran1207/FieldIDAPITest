package com.n4systems.util.persistence;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.EventSchedule;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class WhereParameterTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test public void test_equal_or_null_clause_production() {
		WhereParameter<Long> parameter = new WhereParameter<Long>(Comparator.EQ_OR_NULL, "field.name", 1L);
		
		assertEquals("(schedule.field.name = :field_name OR schedule.field.name IS NULL)", parameter.getClause(new FromTable(EventSchedule.class, "schedule")));
	}

	@Test public void test_trim() {	
		WhereParameter<String> parameter = new WhereParameter<String>(Comparator.EQ, "foo", "column", "untrimmedValue    ", WhereParameter.TRIM, false);

		Query query = createMock(Query.class);
		expect(query.setParameter("foo", "untrimmedValue")).andReturn(query);
		replay(query);

		System.out.println( parameter.getClause(new FromTable(EventSchedule.class, "table")));
		parameter.bind(query);
		
		verify(query);
	}
	
	@Test public void test_ignore_case() {	
		WhereParameter<String> parameter = new WhereParameter<String>(Comparator.EQ, "foo", "column", " MixEdCaSeVALUE ", WhereParameter.IGNORE_CASE, false);

		Query query = createMock(Query.class);
		expect(query.setParameter("foo", " mixedcasevalue ")).andReturn(query); // query should flatten value and selection to lowercase 
		replay(query);

		assertEquals("lower(table.column) = :foo", parameter.getClause(new FromTable(EventSchedule.class, "table")));
		parameter.bind(query);
		
		verify(query);
	}
	
	@Test public void test_wildcard() {	
		WhereParameter<String> parameter = new WhereParameter<String>(Comparator.EQ, "foo", "column", "someValue", WhereParameter.WILDCARD_BOTH, false);

		Query query = createMock(Query.class);
		expect(query.setParameter("foo", "%someValue%")).andReturn(query); 
		replay(query);

		assertEquals("table.column = :foo", parameter.getClause(new FromTable(EventSchedule.class, "table")));
		parameter.bind(query);
		
		verify(query);
	}
	
	@Test public void test_all_options_at_once() {	
		WhereParameter<String> parameter = new WhereParameter<String>(Comparator.EQ, "foo", "column", "   SomeValue    ", 
				WhereParameter.WILDCARD_BOTH | WhereParameter.IGNORE_CASE | WhereParameter.TRIM | WhereParameter.WILDCARD_LEFT | WhereParameter.WILDCARD_RIGHT, false);

		Query query = createMock(Query.class);
		expect(query.setParameter("foo", "%somevalue%")).andReturn(query); 
		replay(query);

		assertEquals("lower(table.column) = :foo", parameter.getClause(new FromTable(EventSchedule.class, "table")));
		parameter.bind(query);
		
		verify(query);
	}
	
	@Test public void test_drop_alias() {
		WhereParameter<Long> parameter = new WhereParameter<Long>(Comparator.EQ_OR_NULL, "field", "field", 1L, 0, true);
		
		assertEquals("(field = :field OR field IS NULL)", parameter.getClause(new FromTable(EventSchedule.class, "schedule")));
	}
	
	
}
