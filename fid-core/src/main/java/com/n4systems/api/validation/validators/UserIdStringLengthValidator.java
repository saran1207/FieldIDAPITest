package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.UserView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.security.SecurityFilter;

import java.util.Map;

public class UserIdStringLengthValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {

        UserView userView = (UserView) view;

        if (fieldValue == null || !(fieldValue instanceof String) || userView.isPerson()) {
			return ValidationResult.pass();
		}
		
		if (((String) fieldValue).length() > field.maxLength()) {
			return ValidationResult.fail(StringLengthValidatorFail, fieldName, field.maxLength());
		} else {
			return ValidationResult.pass();
		}
	}

}
