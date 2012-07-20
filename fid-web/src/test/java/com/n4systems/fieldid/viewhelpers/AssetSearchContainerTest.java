package com.n4systems.fieldid.viewhelpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.permissions.AlwaysOffSystemSecurityGuardTestDouble;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.testutils.TestDoubleLoaderFactory;
import com.n4systems.util.BitField;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

public class AssetSearchContainerTest extends SearchContainerTestCase {

	private AssetSearchContainer assetSearchContainer;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	@Before
	public void setUp() {
		SecurityFilter filter = new OpenSecurityFilter();
		assetSearchContainer = new AssetSearchContainer(filter, new TestDoubleLoaderFactory(filter), new AlwaysOffSystemSecurityGuardTestDouble());
	}

	@Test
	public void single_search_term_non_wildcard() throws Exception {
		assetSearchContainer.setRfidNumber("testsearch");

		WhereParameter<?> whereClause = getSingleWhereClause(assetSearchContainer);

		assertEquals("rfidNumber", whereClause.getName());
		assertEquals("testsearch", whereClause.getValue());

		assertEquals(whereClause.getComparator(), Comparator.EQ);

		BitField options = new BitField(whereClause.getOptions());
		assertFalse("Should have wildcard right option", options.isSet(WhereParameter.WILDCARD_RIGHT));
	}

	@Test
	public void single_search_term_no_asterisk_should_be_eq_comparator() throws Exception {
		assetSearchContainer.setIdentifier("12345");

		WhereParameter<?> whereClause = getSingleWhereClause(assetSearchContainer);

		assertEquals("identifier", whereClause.getName());
		assertEquals("12345", whereClause.getValue());

		assertEquals(whereClause.getComparator(), Comparator.EQ);

		BitField options = new BitField(whereClause.getOptions());
		assertFalse("Should NOT have wildcard right option", options.isSet(WhereParameter.WILDCARD_RIGHT));
	}

	@Test
	public void single_simple_term_date_range() throws Exception {
		assetSearchContainer.setFromDate(dateFormat.parse("05/02/1990"));
		assetSearchContainer.setToDate(dateFormat.parse("09/09/2010"));

		SearchTermDefiner term = getSingleSearchTerm(assetSearchContainer);

		List<WhereClause<?>> wheres = term.getWhereParameters();

		assertEquals(2, wheres.size());
		WhereParameter<?> param = (WhereParameter<?>) wheres.get(0);
		WhereParameter<?> param2 = (WhereParameter<?>) wheres.get(1);
		
		assertEquals("identified_start", param.getName());
		assertEquals(Comparator.GE, param.getComparator());
		assertEquals("05/02/1990", dateFormat.format(param.getValue()));
		
		assertEquals("identified_end", param2.getName());
		assertEquals(Comparator.LT, param2.getComparator());
		assertEquals("09/09/2010", dateFormat.format(param2.getValue()));
	}
	
	@Test
	public void test_search_with_wildcard() throws Exception {
		assetSearchContainer.setIdentifier("555*");
		
		WhereParameter<?> whereClause = getSingleWhereClause(assetSearchContainer);

		assertEquals("identifier", whereClause.getName());
		assertEquals("555", whereClause.getValue());

		assertEquals(whereClause.getComparator(), Comparator.LIKE);

		BitField options = new BitField(whereClause.getOptions());
		assertTrue("Should have wildcard right option", options.isSet(WhereParameter.WILDCARD_RIGHT));
	}

}
