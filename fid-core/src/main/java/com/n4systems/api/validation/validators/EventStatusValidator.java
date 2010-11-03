package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.security.SecurityFilter;

public class EventStatusValidator implements FieldValidator {
	private static final String[] VALID_STATUSES = {"PASS", "FAIL", "NA", "N/A"};
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		String status = (String)fieldValue;
		
		for (String validStatus: VALID_STATUSES) {
			if (status.equalsIgnoreCase(validStatus)) {
				return ValidationResult.pass();
			}
		}
		return ValidationResult.fail(InspectionStatusValidatorFail, status);
	}

}
