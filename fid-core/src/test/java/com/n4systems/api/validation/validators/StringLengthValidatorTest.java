package com.n4systems.api.validation.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.exporting.beanutils.ExportField;

public class StringLengthValidatorTest {
	@ExportField(title="1", order = 1, maxLength = 10)
	public final Object dummy1 = null;
	private ExportField dummy1Field; 
	
	private StringLengthValidator validator = new StringLengthValidator();
	
	@Before
	public void setup_export_fields() throws SecurityException, NoSuchFieldException {
		dummy1Field = getClass().getField("dummy1").getAnnotation(ExportField.class);
	}
	
	@Test
	public void validation_passes_when_value_is_null() {
		assertTrue(validator.validate(null, null, "", null, dummy1Field, null).isPassed());
	}
	
	@Test
	public void validation_passes_when_value_is_not_a_string() {
		assertTrue(validator.validate(10L, null, "", null, dummy1Field, null).isPassed());
	}
	
	@Test
	public void validation_passes_string_length_is_less_than_max() {
		assertTrue(validator.validate("123456789", null, "", null, dummy1Field, null).isPassed());
	}
	
	@Test
	public void validation_passes_string_length_is_equal_to_max() {
		assertTrue(validator.validate("1234567890", null, "", null, dummy1Field, null).isPassed());
	}
	
	@Test
	public void validation_fails_string_length_is_greater_than_max() {
		assertFalse(validator.validate("12345678901", null, "", null, dummy1Field, null).isPassed());
	}
}
