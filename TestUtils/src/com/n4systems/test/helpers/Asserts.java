package com.n4systems.test.helpers;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Assert;

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
		Assert.assertTrue(message, result);
	}
	
	public static void assertConatainsExpectedValues(Map<String,Object> expected, Map<String,Object> actual) {
		for (Entry<String, Object> expectedEntry : expected.entrySet()) {
			Assert.assertTrue(actual.containsKey(expectedEntry.getKey()));
			Assert.assertEquals(expectedEntry.getValue(), actual.get(expectedEntry.getKey()));	
		}
	}
	
	public static void assertSetsEquals(Set<?> expected, Set<?> actual) {
		if (actual == null) {
			Assert.fail("actual set is null and shouldn't be");
		}
		if (expected.size() != actual.size()) {
			Assert.fail("sizes are different the expected set size is " + expected.toString() + " and the actual set is " + actual.toString());
		}
		
		Assert.assertTrue("expected set is " + expected.toString() + " and the actual set is " + actual.toString(), expected.containsAll(actual));
	}
	
	public static void assertNotEquals(Object expected, Object actual) {
		assertNotEquals("was expecting [" + expected + "] but got [" + actual + "]", expected, actual);
	}
	
	public static void assertNotEquals(String message, Object expected, Object actual) {
		Assert.assertFalse(message, expected.equals(actual));
	}
	
}
