package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.ExportField;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.security.UserType;

public class AccountTypeValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, ExportField field, Map<String, Object> validationContext) {
		String typeString = (String) fieldValue;
		if (typeString==null) { 
			return ValidationResult.pass();
		}
				
		UserType type = UserType.valueFromLabel(typeString);  
		return type==null ? ValidationResult.fail("invalid account type " + fieldValue) : ValidationResult.pass();				
	}

}
