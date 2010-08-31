package com.n4systems.fieldid.validators;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class UniqueValueValidator extends FieldValidatorSupport {
	
	public void validate(Object object) throws ValidationException {
		
		String fieldName = getFieldName();
		String value = null;
		try {
			value = (String)this.getFieldValue(fieldName, object);
		} catch ( ClassCastException e ) {
			// do nothing pass the null to the validator.  this way Longs can also use this validator.
		}
		
		HasDuplicateValueValidator form = (HasDuplicateValueValidator)object;
				
		if( form.duplicateValueExists( value ) ) {
			addFieldError(fieldName, object);
			return;
		}
	}

}
