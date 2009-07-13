package com.n4systems.fieldid.validators;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class DuplicateRfidValidator extends FieldValidatorSupport {
	
	public void validate(Object object) throws ValidationException {
		
		String fieldName = getFieldName();
		String value = (String)this.getFieldValue(fieldName, object);
		
		HasDuplicateRfidValidator form = (HasDuplicateRfidValidator)object;
				
		if( form.validateRfid( value ) ) {
			addFieldError(fieldName, object);
			return;
		}
	}

}
