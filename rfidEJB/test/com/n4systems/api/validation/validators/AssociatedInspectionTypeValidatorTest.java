package com.n4systems.api.validation.validators;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.builders.ProductTypeBuilder;
import com.n4systems.model.inspectiontype.AssociatedInspectionTypeExistsLoader;
import com.n4systems.model.product.SmartSearchLoader;
import com.n4systems.model.security.SecurityFilter;

public class AssociatedInspectionTypeValidatorTest {

	@Test
	public void validate_passes_on_null_value() { 
		AssociatedInspectionTypeValidator validator = new AssociatedInspectionTypeValidator();
		
		assertTrue(validator.validate(null, null, null, null, (InspectionType)null).isPassed());
	}

	@Test
	public void validate_passes_when_associated_inspection_type_found() {
		String searchText = "serial number";
		
		Product product = ProductBuilder.aProduct().build();
		product.setType(ProductTypeBuilder.aProductType().build());
		InspectionType inspType = InspectionTypeBuilder.anInspectionType().build();
		
		final SmartSearchLoader ssLoader = createMock(SmartSearchLoader.class);
		expect(ssLoader.setSearchText(searchText)).andReturn(ssLoader);
		expect(ssLoader.load()).andReturn(Arrays.asList(product));
		replay(ssLoader);
		
		final AssociatedInspectionTypeExistsLoader aitLoader = createMock(AssociatedInspectionTypeExistsLoader.class);
		expect(aitLoader.setInspectionType(inspType)).andReturn(aitLoader);
		expect(aitLoader.setProductType(product.getType())).andReturn(aitLoader);
		expect(aitLoader.load()).andReturn(true);
		replay(aitLoader);
		
		AssociatedInspectionTypeValidator validator = new AssociatedInspectionTypeValidator() {
			protected SmartSearchLoader createSmartSearchLoader(SecurityFilter filter) {
				return ssLoader;
			}
			
			protected AssociatedInspectionTypeExistsLoader createAssociatedInspectionTypeExistsLoader(SecurityFilter filter) {
				return aitLoader;
			}
		};
		
		
		Map<String, Object> context = new HashMap<String, Object>();
		context.put(InspectionViewValidator.INSPECTION_TYPE_KEY, inspType);
		
		assertTrue(validator.validate(searchText, null, null, null, context).isPassed());
		verify(ssLoader);
		verify(aitLoader);
	}
	
	@Test
	public void validate_fails_when_associated_inspection_type_not_found() {
		String searchText = "serial number";
		
		Product product = ProductBuilder.aProduct().build();
		product.setType(ProductTypeBuilder.aProductType().build());
		InspectionType inspType = InspectionTypeBuilder.anInspectionType().build();
		
		final SmartSearchLoader ssLoader = createMock(SmartSearchLoader.class);
		expect(ssLoader.setSearchText(searchText)).andReturn(ssLoader);
		expect(ssLoader.load()).andReturn(Arrays.asList(product));
		replay(ssLoader);
		
		final AssociatedInspectionTypeExistsLoader aitLoader = createMock(AssociatedInspectionTypeExistsLoader.class);
		expect(aitLoader.setInspectionType(inspType)).andReturn(aitLoader);
		expect(aitLoader.setProductType(product.getType())).andReturn(aitLoader);
		expect(aitLoader.load()).andReturn(false);
		replay(aitLoader);
		
		AssociatedInspectionTypeValidator validator = new AssociatedInspectionTypeValidator() {
			protected SmartSearchLoader createSmartSearchLoader(SecurityFilter filter) {
				return ssLoader;
			}
			
			protected AssociatedInspectionTypeExistsLoader createAssociatedInspectionTypeExistsLoader(SecurityFilter filter) {
				return aitLoader;
			}
		};
		
		
		Map<String, Object> context = new HashMap<String, Object>();
		context.put(InspectionViewValidator.INSPECTION_TYPE_KEY, inspType);
		
		assertFalse(validator.validate(searchText, null, null, null, context).isPassed());
		verify(ssLoader);
		verify(aitLoader);
	}
}
