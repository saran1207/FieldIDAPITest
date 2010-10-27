package com.n4systems.api.validation.validators;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import com.n4systems.model.assettype.AssetTypeByNameExistsLoader;
import org.junit.Test;

import com.n4systems.model.security.SecurityFilter;

public class AssetTypeExistsValidatorTest {

	@Test
	public void validation_passes_when_name_exists() {
		String name = "test name";
		
		final AssetTypeByNameExistsLoader nameExistsLoader = createMock(AssetTypeByNameExistsLoader.class);
		
		AssetTypeExistsValidator validator = new AssetTypeExistsValidator() {
			@Override
			protected AssetTypeByNameExistsLoader createAssetTypeExistsLoader(SecurityFilter filter) {
				return nameExistsLoader;
			}
		};
		
		expect(nameExistsLoader.setName(name)).andReturn(nameExistsLoader);
		expect(nameExistsLoader.load()).andReturn(true);
		
		replay(nameExistsLoader);
		
		assertTrue(validator.validate(name, null, "Field Name", null, null).isPassed());
		
		verify(nameExistsLoader);
	}
	
	@Test
	public void validation_fails_when_name_does_not_exist() {
		String name = "test name";
		
		final AssetTypeByNameExistsLoader nameExistsLoader = createMock(AssetTypeByNameExistsLoader.class);
		
		AssetTypeExistsValidator validator = new AssetTypeExistsValidator() {
			@Override
			protected AssetTypeByNameExistsLoader createAssetTypeExistsLoader(SecurityFilter filter) {
				return nameExistsLoader;
			}
		};
		
		expect(nameExistsLoader.setName(name)).andReturn(nameExistsLoader);
		expect(nameExistsLoader.load()).andReturn(false);
		
		replay(nameExistsLoader);
		
		assertTrue(validator.validate(name, null, "Field Name", null, null).isFailed());
		
		verify(nameExistsLoader);
	}
}
