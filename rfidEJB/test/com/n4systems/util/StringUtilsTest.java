package com.n4systems.util;
import static org.junit.Assert.*;

import org.junit.Test;
public class StringUtilsTest {

	@Test
	public void string_or_empty_returns_empty_on_null() {
		assertEquals("", StringUtils.stringOrEmpty(null));
	}
	
	@Test
	public void string_or_empty_uses_to_string_when_not_null() {
		assertEquals("10", StringUtils.stringOrEmpty(new Integer(10)));
	}
	
	@Test
	public void clean_handles_null() {
		assertNull(StringUtils.clean(null));
	}
	
	@Test
	public void clean_returns_null_on_empty_string() {
		assertNull(StringUtils.clean("     "));
	}
	
	@Test
	public void clean_trims_non_empty_string() {
		assertEquals("a", StringUtils.clean("   a  "));
	}
	
	@Test
	public void test_is_not_empty_false_conditions() {
		assertFalse(StringUtils.isNotEmpty("     "));
		assertFalse(StringUtils.isNotEmpty("   \n  "));
		assertFalse(StringUtils.isNotEmpty(""));
		assertFalse(StringUtils.isNotEmpty(null));
	}
	
	@Test
	public void test_is_not_empty_true_conditions() {
		assertTrue(StringUtils.isNotEmpty("abc"));
		assertTrue(StringUtils.isNotEmpty("   a   "));
	}
	
	@Test
	public void test_is_empty_false_conditions() {
		assertTrue(StringUtils.isEmpty("     "));
		assertTrue(StringUtils.isEmpty("   \n  "));
		assertTrue(StringUtils.isEmpty(""));
		assertTrue(StringUtils.isEmpty(null));
	}
	
	@Test
	public void test_is_empty_true_conditions() {
		assertFalse(StringUtils.isEmpty("abc"));
		assertFalse(StringUtils.isEmpty("   a   "));
	}
	
	@Test
	public void test_path_to_name() {
		assertEquals("hello", StringUtils.pathToName("hello"));
		assertEquals("_hello_", StringUtils.pathToName(".hello."));
		assertEquals("hello_mark_how_are_you", StringUtils.pathToName("hello.mark.how.are.you"));
	}
}
