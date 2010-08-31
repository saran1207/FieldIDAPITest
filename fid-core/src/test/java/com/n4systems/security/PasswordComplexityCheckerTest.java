package com.n4systems.security;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordComplexityCheckerTest {
	
	
	@Test
	public void test_different_lenghs() {
		PasswordComplexityChecker pcc = new PasswordComplexityChecker(5, 0, 0, 0, 0);
		
		assertFalse(pcc.isValid("1234"));
		assertTrue(pcc.isValid("12345"));
		assertTrue(pcc.isValid("123456"));
	}
	
	@Test
	public void test_lower_alpha() {
		PasswordComplexityChecker pcc = new PasswordComplexityChecker(6, 4, 0, 0, 0);
		
		assertFalse(pcc.isValid("Aaaa111"));
		assertTrue(pcc.isValid("aaaa11"));
		assertTrue(pcc.isValid("abas11"));
	}
	
	@Test
	public void test_upper_alpha() {
		PasswordComplexityChecker pcc = new PasswordComplexityChecker(6, 0, 4, 0, 0);
		
		assertFalse(pcc.isValid("aAAA111"));
		assertTrue(pcc.isValid("AAAA11"));
		assertTrue(pcc.isValid("ABAS11"));
	}
	
	@Test
	public void test_numeric() {
		PasswordComplexityChecker pcc = new PasswordComplexityChecker(6, 0, 0, 4, 0);
		
		assertFalse(pcc.isValid("111aaa"));
		assertTrue(pcc.isValid("1111aa"));
		assertTrue(pcc.isValid("1234aa"));
	}
	
	@Test
	public void test_punctuation() {
		PasswordComplexityChecker pcc = new PasswordComplexityChecker(6, 0, 0, 0, 4);
		
		assertFalse(pcc.isValid("!!!aaa"));
		assertTrue(pcc.isValid("!!!!aa"));
		assertTrue(pcc.isValid("!@#$aa"));
	}
}
