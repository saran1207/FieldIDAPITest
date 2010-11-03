package com.n4systems.api.validation.validators;
import static org.junit.Assert.*;

import org.junit.Test;

public class EventStatusValidatorTest {
	private EventStatusValidator validator = new EventStatusValidator();
	
	@Test
	public void validate_passes_on_null() { 
		assertTrue(validator.validate(null, null, null, null, null).isPassed());
	}
	
	@Test
	public void test_pass_conditions() { 
		assertTrue(validator.validate("PASS", null, null, null, null).isPassed());
		assertTrue(validator.validate("FAIL", null, null, null, null).isPassed());
		assertTrue(validator.validate("NA", null, null, null, null).isPassed());
		assertTrue(validator.validate("N/A", null, null, null, null).isPassed());
	}
	
	@Test
	public void test_validate_ignores_case() { 
		assertTrue(validator.validate("pASS", null, null, null, null).isPassed());
		assertTrue(validator.validate("FaiL", null, null, null, null).isPassed());
		assertTrue(validator.validate("na", null, null, null, null).isPassed());
	}
	
	@Test
	public void test_fail_conditions() { 
		assertFalse(validator.validate("pas", null, null, null, null).isPassed());
		assertFalse(validator.validate("", null, null, null, null).isPassed());
		assertFalse(validator.validate("pass fail", null, null, null, null).isPassed());
	}
	
}
