package com.n4systems.fieldid.viewhelpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.testutils.TestDoubleLoaderFactory;
import com.n4systems.util.BitField;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class InspectionSearchContainerTest extends SearchContainerTestCase {
	
	private InspectionSearchContainer inspectionSearchContainer;
	
	@Before
	public void setUp() {
		SecurityFilter filter = new OpenSecurityFilter();
		inspectionSearchContainer = new InspectionSearchContainer(filter, new TestDoubleLoaderFactory(filter));
	}
	
	@Test
	public void single_search_term_no_asterisk_should_be_eq_comparator() throws Exception {
		inspectionSearchContainer.setSerialNumber("12345");

		WhereParameter<?> whereClause = getSingleWhereClause(inspectionSearchContainer);

		assertEquals("product_serialNumber", whereClause.getName());
		assertEquals("12345", whereClause.getValue());

		assertEquals(whereClause.getComparator(), Comparator.EQ);

		BitField options = new BitField(whereClause.getOptions());
		assertFalse("Should NOT have wildcard right option", options.isSet(WhereParameter.WILDCARD_RIGHT));
	}
	
	@Test
	public void single_search_term_with_right_asterisk() throws Exception {
		inspectionSearchContainer.setSerialNumber("12345*");

		WhereParameter<?> whereClause = getSingleWhereClause(inspectionSearchContainer);

		assertEquals("product_serialNumber", whereClause.getName());
		assertEquals("12345", whereClause.getValue());

		assertEquals(whereClause.getComparator(), Comparator.LIKE);

		BitField options = new BitField(whereClause.getOptions());
		assertTrue("Should have wildcard right option", options.isSet(WhereParameter.WILDCARD_RIGHT));
	}
	
	@Test
	public void single_search_term_with_left_asterisk() throws Exception {
		inspectionSearchContainer.setSerialNumber("*12345");

		WhereParameter<?> whereClause = getSingleWhereClause(inspectionSearchContainer);

		assertEquals("product_serialNumber", whereClause.getName());
		assertEquals("12345", whereClause.getValue());

		assertEquals(whereClause.getComparator(), Comparator.LIKE);

		BitField options = new BitField(whereClause.getOptions());
		assertTrue("Should have wildcard right option", options.isSet(WhereParameter.WILDCARD_LEFT));
	}

	
}
