package com.n4systems.api.validation.validators;
import static org.junit.Assert.*;
import org.junit.Test;

public class ProductViewSerialLengthValidatorTest {

	private ProductViewToProductSerialLengthValidator validator = new ProductViewToProductSerialLengthValidator();
	
	@Test
	public void validation_should_pass_with_serialnumber_smaller_than_50_characters(){
		String serialNumber = "aaaaaaaaaaaaaaaaaaaaaaaa";
		assertTrue(validator.validate(serialNumber, null, null, null, null).isPassed());
	}
	
	@Test
	public void validation_should_pass_with_serialnumber_of_exactly_50_characters(){
		String serialNumber = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
		assertTrue(validator.validate(serialNumber, null, null, null, null).isPassed());
	}
	
	@Test
	public void validation_should_fail_with_serialnumber_greater_than_50_characters(){
		String serialNumber = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaazzz";
		assertTrue(validator.validate(serialNumber, null, null, null, null).isFailed());
	}
}
