package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.productstatus.ProductStatusForNameExistsLoader;
import com.n4systems.model.security.SecurityFilter;

public class AssetStatusExistsValidatorTest {
	private static final String productStatus = "In Service";
	@Test
	public void test_validate_passes_on_null_value() {
		AssetStatusExistsValidator validator = new AssetStatusExistsValidator();
		assertTrue(validator.validate(null, null, null, null, null).isPassed());
	}
	
	@Test
	public void test_validate_passes_when_status_exists() {
		AssetStatusExistsValidator validator = new AssetStatusExistsValidator() {
			@Override
			protected ProductStatusForNameExistsLoader createProductStatusExistsLoader(SecurityFilter filter) {
				return new ProductStatusForNameExistsLoader(filter) {
					@Override
					public Boolean load() {
						assertEquals(productStatus, this.name);
						return true;
					}
				};
			}
		};
		
		assertTrue(validator.validate(productStatus, null, null, null, null).isPassed());
	}
	
	@Test
	public void test_validate_fails_when_status_does_not_exist() {
		AssetStatusExistsValidator validator = new AssetStatusExistsValidator() {
			@Override
			protected ProductStatusForNameExistsLoader createProductStatusExistsLoader(SecurityFilter filter) {
				return new ProductStatusForNameExistsLoader(filter) {
					@Override
					public Boolean load() {
						assertEquals(productStatus, this.name);
						return false;
					}
				};
			}
		};
		
		assertTrue(validator.validate(productStatus, null, null, null, null).isFailed());
	}
}
