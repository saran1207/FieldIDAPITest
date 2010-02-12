package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.security.SecurityFilter;

public class ExternalOrgTypeValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter) {
		String type = (String)fieldValue;
		if (type.equals("C") || type.equals("D")) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(ExternalOrgTypeValidatorFail, fieldName);
		}
	}

}
