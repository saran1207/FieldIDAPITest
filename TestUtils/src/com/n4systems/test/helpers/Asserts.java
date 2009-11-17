package com.n4systems.test.helpers;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Asserts {
	public static void assertInRange(Date expected, Date actual, long deltaInMilliSeconds) {
		assertInRange("value is not inside the range", expected, actual, deltaInMilliSeconds);
	}
	public static void assertInRange(String message, Date expected, Date actual, long deltaInMilliSeconds) {
		boolean result = false;
		if (expected == null) {
			if (actual == null ) {
				result = true;
			} 
		} else if (((expected.getTime() + deltaInMilliSeconds) >= actual.getTime()) && ((expected.getTime() - deltaInMilliSeconds) <= actual.getTime())) {
			result = true;
		}	
		assertTrue(message, result);
	}
	
	public static void assertConatainsExpectedValues(Map<String,Object> expected, Map<String,Object> actual) {
		for (Entry<String, Object> expectedEntry : expected.entrySet()) {
			assertTrue(actual.containsKey(expectedEntry.getKey()));
			assertEquals(expectedEntry.getValue(), actual.get(expectedEntry.getKey()));	
		}
	}
	
	public static void assertSetsEquals(HashSet<?> expected, Set<?> actual) {
		if (actual == null) {
			fail("actual set is null and shouldn't be");
		}
		if (expected.size() != actual.size()) {
			fail("sizes are different the expected set size is " + expected.toString() + " and the actual set is " + actual.toString());
		}
		
		assertTrue("expected set is " + expected.toString() + " and the actual set is " + actual.toString(), expected.containsAll(actual));
	}
}
