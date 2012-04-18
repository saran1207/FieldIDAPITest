package com.n4systems.util;
import org.junit.Test;

import static org.junit.Assert.*;
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
	
	@Test
	public void test_strip_whitespace() {
		assertEquals("helloworld", StringUtils.stripWhitespace(" hello wor\tld"));
	}
	
	@Test
	public void test_isWholeNumber() {
		assertTrue(StringUtils.isWholeNumber("1231231239999933333321120045854954954"));  // note that there is no limit to length. even if bigger than MaxInt.
		assertTrue(StringUtils.isWholeNumber("123123123"));
		assertTrue(StringUtils.isWholeNumber("12312312  "));
		assertFalse(StringUtils.isWholeNumber("  12 312312  "));
		assertTrue(StringUtils.isWholeNumber("   12312312  "));
		assertFalse(StringUtils.isWholeNumber("12312312X"));
		assertFalse(StringUtils.isWholeNumber("0x12312312"));
		assertFalse(StringUtils.isWholeNumber("-1"));
		assertTrue(StringUtils.isWholeNumber("0"));
		assertFalse(StringUtils.isWholeNumber(""));
		assertFalse(StringUtils.isWholeNumber(null));
	}
    
    @Test 
    public void test_getFileCopyName() {
        assertEquals("Copy of hello", StringUtils.getFileCopyName("hello"));
        assertEquals("Copy of hello (1)", StringUtils.getFileCopyName("Copy of hello"));
        assertEquals("Copy of hello (3)", StringUtils.getFileCopyName("Copy of hello (2)"));
        assertEquals("Copy of Coooopy of hello", StringUtils.getFileCopyName("Coooopy of hello"));
        assertEquals("Copy of hello (2)", StringUtils.getFileCopyName("hello (2)"));
    }
    
	
}
