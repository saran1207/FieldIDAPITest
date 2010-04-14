package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.security.SecurityFilter;

public class YNValidator implements FieldValidator {
	private static final String[] VALID_VALUES = {"Y", "N"};
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		String ynField = (String)fieldValue;
		
		for (String validValue: VALID_VALUES) {
			if (ynField.equalsIgnoreCase(validValue)) {
				return ValidationResult.pass();
			}
		}
		return ValidationResult.fail(YNValidatorFail, ynField, fieldName);
	}
}
