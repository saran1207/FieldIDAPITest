package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.security.SecurityFilter;


public abstract class AutoAttributeValidator implements FieldValidator {
	public static final String CRITERIA_KEY = "autoAttributeCriteria";
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		return validate(fieldValue, (AutoAttributeView)view, fieldName, filter, (AutoAttributeCriteria)validationContext.get(CRITERIA_KEY));
	}

	public abstract ValidationResult validate(Object fieldValue, AutoAttributeView view, String fieldName, SecurityFilter filter, AutoAttributeCriteria criteria);
	
}
