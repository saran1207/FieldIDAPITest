package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;
import org.junit.Test;

public class AssetViewRfidLengthTest {

	private AssetViewStringFieldLengthValidator validator = new AssetViewToAssetRfidLengthValidator();
	
	@Test
	public void validation_should_pass_with_rfid_smaller_than_46_characters(){
		String rfidlNumber = "aaaaaaaaaaaaaaaaaaaaaaaa";
		assertTrue(validator.validate(rfidlNumber, null, null, null, null).isPassed());
	}
	
	@Test
	public void validation_should_pass_with_rfid_of_exactly_46_characters(){
		String rfidNumber = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
		assertTrue(validator.validate(rfidNumber, null, null, null, null).isPassed());
	}
	
	@Test
	public void validation_should_fail_with_rfid_greater_than_46_characters(){
		String rfidNumber = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaazzz";
		assertTrue(validator.validate(rfidNumber, null, null, null, null).isFailed());
	}
	
}
