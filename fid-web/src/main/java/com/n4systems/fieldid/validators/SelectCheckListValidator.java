package com.n4systems.fieldid.validators;

import java.util.Map;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class SelectCheckListValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
		Map<String,Boolean> value = (Map<String,Boolean>)this.getFieldValue(fieldName, object);
		
		if( ! value.containsValue( true )) { 
			addFieldError(fieldName, object);
			addActionError( getDefaultMessage() );
			return;
		}
	}

}
