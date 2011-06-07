package com.n4systems.api.validation.validators;
import static org.junit.Assert.*;

import org.junit.Test;

public class YNValidatorTest {
	private YNValidator validator = new YNValidator();
	
	@Test
	public void validate_passes_on_null() { 
		assertTrue(validator.validate(null, null, null, null, null, null).isPassed());
	}
	
	@Test
	public void test_pass_conditions() { 
		assertTrue(validator.validate("Y", null, null, null, null, null).isPassed());
		assertTrue(validator.validate("N", null, null, null, null, null).isPassed());
	}
	
	@Test
	public void test_validate_ignores_case() { 
		assertTrue(validator.validate("y", null, null, null, null, null).isPassed());
		assertTrue(validator.validate("n", null, null, null, null, null).isPassed());
	}
	
	@Test
	public void test_fail_conditions() { 
		assertFalse(validator.validate("b", null, null, null, null, null).isPassed());
		assertFalse(validator.validate("", null, null, null, null, null).isPassed());
		assertFalse(validator.validate("yn", null, null, null, null, null).isPassed());
	}
	
}
