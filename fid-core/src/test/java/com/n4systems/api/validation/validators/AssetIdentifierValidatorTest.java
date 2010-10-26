package com.n4systems.api.validation.validators;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.product.SmartSearchCounter;
import com.n4systems.model.security.SecurityFilter;

public class AssetIdentifierValidatorTest {
	
	@Test
	public void validate_passes_on_null_value() { 
		AssetIdentifierValidator validator = new AssetIdentifierValidator();
		
		assertTrue(validator.validate(null, null, null, null, null).isPassed());
	}
	
	@Test
	public void validate_passes_when_one_user_found() {
		String searchText = "serial number";
		
		final SmartSearchCounter loader = createMock(SmartSearchCounter.class);
		expect(loader.setSearchText(searchText)).andReturn(loader);
		expect(loader.load()).andReturn(1L);
		replay(loader);
		
		AssetIdentifierValidator validator = new AssetIdentifierValidator() {
			protected SmartSearchCounter createSmartSearchCounter(SecurityFilter filter) {
				return loader;
			}
		};
		
		assertTrue(validator.validate(searchText, null, null, null, null).isPassed());
		verify(loader);
	}
	
	@Test
	public void validate_fails_when_no_users_found() {
		String searchText = "serial number";
		
		final SmartSearchCounter loader = createMock(SmartSearchCounter.class);
		expect(loader.setSearchText(searchText)).andReturn(loader);
		expect(loader.load()).andReturn(0L);
		replay(loader);
		
		AssetIdentifierValidator validator = new AssetIdentifierValidator() {
			protected SmartSearchCounter createSmartSearchCounter(SecurityFilter filter) {
				return loader;
			}
		};
		
		assertFalse(validator.validate(searchText, null, null, null, null).isPassed());
		verify(loader);
	}
	
	@Test
	public void validate_fails_when_more_than_one_user_found() {
		String searchText = "serial number";
		
		final SmartSearchCounter loader = createMock(SmartSearchCounter.class);
		expect(loader.setSearchText(searchText)).andReturn(loader);
		expect(loader.load()).andReturn(2L);
		replay(loader);
		
		AssetIdentifierValidator validator = new AssetIdentifierValidator() {
			protected SmartSearchCounter createSmartSearchCounter(SecurityFilter filter) {
				return loader;
			}
		};
		
		assertFalse(validator.validate(searchText, null, null, null, null).isPassed());
		verify(loader);
	}
	
}
