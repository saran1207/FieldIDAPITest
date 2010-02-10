package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;

public class NotNullValidator implements FieldValidator {

	public NotNullValidator() {}
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName) {		
		if (fieldValue == null) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail("%s must not be empty", fieldName);
		}
	}

}
