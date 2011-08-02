package com.n4systems.fieldid.viewhelpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.permissions.AlwaysOffSystemSecurityGuardTestDouble;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.testutils.TestDoubleLoaderFactory;
import com.n4systems.util.BitField;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EventSearchContainerTest extends SearchContainerTestCase {
	
	private EventSearchContainer eventSearchContainer;
	
	@Before
	public void setUp() {
		SecurityFilter filter = new OpenSecurityFilter();
		eventSearchContainer = new EventSearchContainer(filter, new TestDoubleLoaderFactory(filter), new AlwaysOffSystemSecurityGuardTestDouble());
	}
	
	@Test
	public void single_search_term_no_asterisk_should_be_eq_comparator() throws Exception {
		eventSearchContainer.setIdentifier("12345");

		WhereParameter<?> whereClause = getSingleWhereClause(eventSearchContainer);

		assertEquals("asset_identifier", whereClause.getName());
		assertEquals("12345", whereClause.getValue());

		assertEquals(whereClause.getComparator(), Comparator.EQ);

		BitField options = new BitField(whereClause.getOptions());
		assertFalse("Should NOT have wildcard right option", options.isSet(WhereParameter.WILDCARD_RIGHT));
	}
	
	@Test
	public void single_search_term_with_right_asterisk() throws Exception {
		eventSearchContainer.setIdentifier("12345*");

		WhereParameter<?> whereClause = getSingleWhereClause(eventSearchContainer);

		assertEquals("asset_identifier", whereClause.getName());
		assertEquals("12345", whereClause.getValue());

		assertEquals(whereClause.getComparator(), Comparator.LIKE);

		BitField options = new BitField(whereClause.getOptions());
		assertTrue("Should have wildcard right option", options.isSet(WhereParameter.WILDCARD_RIGHT));
	}
	
	@Test
	public void single_search_term_with_left_asterisk() throws Exception {
		eventSearchContainer.setIdentifier("*12345");

		WhereParameter<?> whereClause = getSingleWhereClause(eventSearchContainer);

		assertEquals("asset_identifier", whereClause.getName());
		assertEquals("12345", whereClause.getValue());

		assertEquals(whereClause.getComparator(), Comparator.LIKE);

		BitField options = new BitField(whereClause.getOptions());
		assertTrue("Should have wildcard right option", options.isSet(WhereParameter.WILDCARD_LEFT));
	}

	
}
