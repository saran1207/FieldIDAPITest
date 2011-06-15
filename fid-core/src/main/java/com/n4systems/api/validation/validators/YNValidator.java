package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.security.SecurityFilter;

public class YNValidator implements FieldValidator {
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		String ynField = (String)fieldValue;
		
		for (YNField value:YNField.values()) { 
			if (value.toString().compareToIgnoreCase(ynField.trim())==0) { 
				return ValidationResult.pass();				
			}
		}
		return ValidationResult.fail(YNValidatorFail, ynField, fieldName);
	}
	
	public enum YNField { 
		Y,N;
	}
}
