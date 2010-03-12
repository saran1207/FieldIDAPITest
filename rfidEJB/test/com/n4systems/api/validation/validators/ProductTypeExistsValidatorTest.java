package com.n4systems.api.validation.validators;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.producttype.ProductTypeByNameExistsLoader;
import com.n4systems.model.security.SecurityFilter;

public class ProductTypeExistsValidatorTest {

	@Test
	public void validation_passes_when_name_exists() {
		String name = "test name";
		
		final ProductTypeByNameExistsLoader nameExistsLoader = createMock(ProductTypeByNameExistsLoader.class);
		
		ProductTypeExistsValidator validator = new ProductTypeExistsValidator() {
			@Override
			protected ProductTypeByNameExistsLoader createProductTypeExistsLoader(SecurityFilter filter) {
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
		
		final ProductTypeByNameExistsLoader nameExistsLoader = createMock(ProductTypeByNameExistsLoader.class);
		
		ProductTypeExistsValidator validator = new ProductTypeExistsValidator() {
			@Override
			protected ProductTypeByNameExistsLoader createProductTypeExistsLoader(SecurityFilter filter) {
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
