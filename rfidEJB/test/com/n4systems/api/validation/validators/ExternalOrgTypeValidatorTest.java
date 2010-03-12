package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;

import org.junit.Test;
public class ExternalOrgTypeValidatorTest {
	private ExternalOrgTypeValidator validator = new ExternalOrgTypeValidator();
	
	@Test
	public void test_validation_passes_when_type_customer() {
		assertTrue(validator.validate("C", null, null, null, null).isPassed());
	}
	
	@Test
	public void test_validation_passes_when_type_division() {
		assertTrue(validator.validate("D", null, null, null, null).isPassed());
	}
	
	@Test
	public void test_validation_fails_when_type_not_customer_division() {
		assertTrue(validator.validate("A", null, null, null, null).isFailed());
		assertTrue(validator.validate("Z", null, null, null, null).isFailed());
	}
	
	@Test
	public void test_validation_is_case_sensitive() {
		assertTrue(validator.validate("c", null, null, null, null).isFailed());
		assertTrue(validator.validate("d", null, null, null, null).isFailed());
	}
}
