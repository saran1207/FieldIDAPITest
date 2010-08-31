package com.n4systems.util.persistence;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class WhereParameterTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test public void test_equal_or_null_clause_production() {
		WhereParameter<Long> parameter = new WhereParameter<Long>(Comparator.EQ_OR_NULL, "field", 1L);
		
		assertEquals("(schedule.field = :field OR schedule.field IS NULL)", parameter.getClause(new FromTable(InspectionSchedule.class, "schedule")));
	}

}
