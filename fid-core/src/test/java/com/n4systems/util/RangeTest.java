package com.n4systems.util;

import static com.n4systems.test.helpers.Asserts.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;



public class RangeTest {

	
	@Test(expected=InvalidArgumentException.class)
	public void should_not_allow_a_null_for_beginning_of_range() throws Exception {
		new Range<Integer>(null, 2);
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void should_not_allow_a_null_for_ending_of_range() throws Exception {
		new Range<Integer>(2, null);
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void should_not_allow_begining_of_range_to_be_greater_than_ending() throws Exception {
		new Range<Integer>(2, 1);
	}
	
	@Test
	public void should_allow_begining_of_range_to_be_equal_to_ending() throws Exception {
		Range<Integer> sut = new Range<Integer>(1, 1);
		assertEquals(new Integer(1), sut.getBeginning());
		assertEquals(new Integer(1), sut.getEnding());
	}
	
	@Test
	public void should_allow_begining_of_range_to_be_smaller_than_ending() throws Exception {
		Range<Integer> sut = new Range<Integer>(1, 5);
		assertEquals(new Integer(1), sut.getBeginning());
		assertEquals(new Integer(5), sut.getEnding());
	}
	
	
	
	@Test
	public void test_equals() {
		assertEquals(new Range<Integer>(1, 2), new Range<Integer>(1, 2));
		
		assertNotEquals(new Range<Integer>(1, 2), new Range<Integer>(2, 2));
		assertNotEquals(new Range<Integer>(1, 2), new Range<Integer>(0, 2));
		assertNotEquals(new Range<Integer>(1, 10), new Range<Integer>(2, 4));
		
		assertNotEquals(new Range<String>("asdf", "asdfa"), new Range<Integer>(2, 4));
	}
	
	
	
	@Test
	public void should_find_that_single_value_is_in_the_range() throws Exception {
		assertTrue(new Range<Integer>(1, 10).contains(5));
		assertTrue(new Range<Integer>(1, 10).contains(1));
		assertTrue(new Range<Integer>(1, 10).contains(10));
		assertTrue(new Range<Integer>(1, 1).contains(1));
	}

	@Test
	public void should_find_that_single_value_is_outside_the_range() throws Exception {
		assertFalse(new Range<Integer>(1, 10).contains(20));
		assertFalse(new Range<Integer>(1, 10).contains(-20));
		assertFalse(new Range<Integer>(1, 10).contains(0));
		assertFalse(new Range<Integer>(1, 10).contains(11));
		assertFalse(new Range<Integer>(1, 1).contains(0));
	}
	
	
	
	
	
}
