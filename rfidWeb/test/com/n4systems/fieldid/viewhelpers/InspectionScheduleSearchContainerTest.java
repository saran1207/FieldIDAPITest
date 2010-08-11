package com.n4systems.fieldid.viewhelpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.testutils.TestDoubleLoaderFactory;
import com.n4systems.util.BitField;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class InspectionScheduleSearchContainerTest extends SearchContainerTestCase {

	private InspectionScheduleSearchContainer inspectionScheduleSearchContainer;
	
	@Before
	public void setUp() {
		SecurityFilter filter = new OpenSecurityFilter();
		inspectionScheduleSearchContainer = new InspectionScheduleSearchContainer(filter, new TestDoubleLoaderFactory(filter));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void no_search_terms_should_give_one_clause_of_status() {
		// Set no fields on the container.
		
		WhereParameter<?> clause = getSingleWhereClause(inspectionScheduleSearchContainer);
		
		assertEquals("status", clause.getName());
		List<ScheduleStatus> items = (List<ScheduleStatus>) clause.getValue();
		assertTrue(items.contains(ScheduleStatus.IN_PROGRESS));
		assertTrue(items.contains(ScheduleStatus.SCHEDULED));
		assertFalse(items.contains(ScheduleStatus.COMPLETED));
	}

	@Test
	public void single_search_term_no_asterisk_should_be_eq_comparator() throws Exception {
		inspectionScheduleSearchContainer.setSerialNumber("12345");
		
		WhereParameter<?> whereClause = getWhereClauseNamed(inspectionScheduleSearchContainer, "product_serialNumber");

		assertEquals("product_serialNumber", whereClause.getName());
		assertEquals("12345", whereClause.getValue());

		assertEquals(whereClause.getComparator(), Comparator.EQ);

		BitField options = new BitField(whereClause.getOptions());
		assertFalse("Should NOT have wildcard right option", options.isSet(WhereParameter.WILDCARD_RIGHT));
	}

}
