package com.n4systems.api.validation.validators;

import java.util.Date;
import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.security.SecurityFilter;

public class DateValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		if (fieldValue instanceof Date) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(InvalidDateValidatorFail, String.valueOf(fieldValue), fieldName);
		}
	}

}
