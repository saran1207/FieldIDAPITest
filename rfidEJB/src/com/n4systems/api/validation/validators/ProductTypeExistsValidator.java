package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.producttype.ProductTypeByNameExistsLoader;
import com.n4systems.model.security.SecurityFilter;

public class ProductTypeExistsValidator implements FieldValidator {
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		String name = (String)fieldValue;
		
		ProductTypeByNameExistsLoader typeExistsLoader = createProductTypeExistsLoader(filter).setName(name);
		
		if (typeExistsLoader.load()) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(ProductTypeExistsValidatorFail, fieldName);
		}
	}
	
	protected ProductTypeByNameExistsLoader createProductTypeExistsLoader(SecurityFilter filter) {
		return new ProductTypeByNameExistsLoader(filter);
	}
}
