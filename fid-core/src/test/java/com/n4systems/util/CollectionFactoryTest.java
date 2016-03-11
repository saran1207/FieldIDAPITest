package com.n4systems.util;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class CollectionFactoryTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_create_collection() {
		assertTrue(CollectionFactory.createCollection(	new ArrayList<Object>())	instanceof List);
		assertTrue(CollectionFactory.createCollection(	new TreeSet<Object>())		instanceof SortedSet);
		assertTrue(CollectionFactory.createCollection(	new HashSet<Object>())		instanceof Set);
		assertTrue(CollectionFactory.createCollection(	new LinkedList<Object>())	instanceof Queue);
	}

}
