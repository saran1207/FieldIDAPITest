package com.n4systems.api.validation.validators;
import static org.junit.Assert.*;
import org.junit.Test;

public class AssetViewIdentifierLengthValidatorTest {

	private AssetViewToAssetIdentifierLengthValidator validator = new AssetViewToAssetIdentifierLengthValidator();
	
	@Test
	public void validation_should_pass_with_identifier_smaller_than_50_characters(){
		String identifier = "aaaaaaaaaaaaaaaaaaaaaaaa";
		assertTrue(validator.validate(identifier, null, null, null, null, null).isPassed());
	}
	
	@Test
	public void validation_should_pass_with_identifier_of_exactly_50_characters(){
		String identifier = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
		assertTrue(validator.validate(identifier, null, null, null, null, null).isPassed());
	}
	
	@Test
	public void validation_should_fail_with_identifier_greater_than_50_characters(){
		String identifier = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaazzz";
		assertTrue(validator.validate(identifier, null, null, null, null, null).isFailed());
	}
}
