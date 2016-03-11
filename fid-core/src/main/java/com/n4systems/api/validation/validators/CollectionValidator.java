package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.security.SecurityFilter;

import java.util.Collection;
import java.util.Map;

public class CollectionValidator<T, C extends Collection<T>> implements FieldValidator {

	@SuppressWarnings("unchecked")
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		return validateCollection((C)fieldValue, view, fieldName, filter, field, validationContext); 
	}

	
	protected <V extends ExternalModelView> ValidationResult validateCollection(C fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		for (T value:fieldValue) {
			ValidationResult result = validateElement(value, view, fieldName, filter, field, validationContext);
			if (result.isFailed()) {
				return result;
			}
		}
		return ValidationResult.pass();		
	}
	
	protected <V extends ExternalModelView> ValidationResult validateElement(T value, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) { 
		return ValidationResult.pass(); 		
	}
	
}
