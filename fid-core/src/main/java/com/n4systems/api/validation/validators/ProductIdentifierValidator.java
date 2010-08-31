package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.product.SmartSearchCounter;
import com.n4systems.model.security.SecurityFilter;

public class ProductIdentifierValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		String identifier = (String)fieldValue;
		
		SmartSearchCounter productLoader = createSmartSearchCounter(filter).setSearchText(identifier);
		
		Long products = productLoader.load();
		if (products == 1) {
			return ValidationResult.pass();
		} else if (products == 0) {
			return ValidationResult.fail(NoProductFoundValidationFail, identifier, fieldName);
		} else {
			return ValidationResult.fail(MultipleProductFoundValidationFail, identifier, fieldName);
		}
	}

	protected SmartSearchCounter createSmartSearchCounter(SecurityFilter filter) {
		return new SmartSearchCounter(filter);
	}
	
}
