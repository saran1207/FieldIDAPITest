package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;

public interface FieldValidator {
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName);
}
