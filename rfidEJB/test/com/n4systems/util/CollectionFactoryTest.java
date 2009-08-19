package com.n4systems.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

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
