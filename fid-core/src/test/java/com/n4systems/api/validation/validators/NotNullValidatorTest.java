package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;

import org.junit.Test;
public class NotNullValidatorTest {
	private NotNullValidator validator = new NotNullValidator();
	
	@Test
	public void validation_passes_when_value_not_null() {
		assertTrue(validator.validate(new Object(), null, null, null, null, null).isPassed());
	}
	
	@Test
	public void validation_fails_when_value_null() {
		assertTrue(validator.validate(null, null, null, null, null, null).isFailed());
	}
	
}
