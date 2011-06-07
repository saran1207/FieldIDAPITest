package com.n4systems.api.validation.validators;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.n4systems.model.utils.PlainDate;

public class DateValidatorTest {
	private DateValidator validator = new DateValidator();
	
	@Test
	public void validation_passes_when_date_is_null() {
		assertTrue(validator.validate(null, null, "", null, null, null).isPassed());
	}
	
	@Test
	public void validation_passes_when_date_is_a_date() {
		assertTrue(validator.validate(new Date(), null, "", null, null, null).isPassed());
	}
	
	@Test
	public void validation_passes_when_date_is_subclass_of_date() {
		assertTrue(validator.validate(new PlainDate(), null, "", null, null, null).isPassed());
	}
	
	@Test
	public void validation_fails_when_date_is_not_a_date() {
		assertFalse(validator.validate("", null, "", null, null, null).isPassed());
	}
}
