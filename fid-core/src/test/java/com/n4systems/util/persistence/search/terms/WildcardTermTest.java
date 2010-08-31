package com.n4systems.util.persistence.search.terms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.n4systems.util.BitField;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class WildcardTermTest {
	
	@Test
	public void wildcard_asterisk_right() throws Exception {
		WildcardTerm wct = new WildcardTerm("name", "123*");
		
		WhereParameter<?> clause = getSingleWhereClause(wct);
		BitField bf = new BitField(clause.getOptions());
		
		assertEquals(Comparator.LIKE, clause.getComparator());
		assertTrue(bf.isSet(WhereParameter.WILDCARD_RIGHT));
		assertFalse(bf.isSet(WhereParameter.WILDCARD_LEFT));
		assertEquals("123", clause.getValue());
	}
	
	@Test
	public void wildcard_asterisk_left() throws Exception {
		WildcardTerm wct = new WildcardTerm("name", "*123");
		
		WhereParameter<?> clause = getSingleWhereClause(wct);
		BitField bf = new BitField(clause.getOptions());
		
		assertEquals(Comparator.LIKE, clause.getComparator());
		assertTrue(bf.isSet(WhereParameter.WILDCARD_LEFT));
		assertFalse(bf.isSet(WhereParameter.WILDCARD_RIGHT));
		assertEquals("123", clause.getValue());
	}
	
	@Test
	public void wildcard_asterisk_both() throws Exception {
		WildcardTerm wct = new WildcardTerm("name", "*123*");
		
		WhereParameter<?> clause = getSingleWhereClause(wct);
		BitField bf = new BitField(clause.getOptions());
		
		assertEquals(Comparator.LIKE, clause.getComparator());
		assertTrue(bf.isSet(WhereParameter.WILDCARD_LEFT));
		assertTrue(bf.isSet(WhereParameter.WILDCARD_RIGHT));
		assertEquals("123", clause.getValue());
	}
	
	private WhereParameter<?> getSingleWhereClause(WildcardTerm term) {
		List<WhereClause<?>> params = term.getWhereParameters();
		
		assertEquals("Should be a single param", 1, params.size());
		
		return (WhereParameter<?>) params.get(0);
	}

}
