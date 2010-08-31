package com.n4systems.fieldid.validators;

import java.io.File;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class FileExistsValidator extends FieldValidatorSupport {

	public void validate( Object object ) throws ValidationException {
		String fieldName = getFieldName();
		File value = (File)this.getFieldValue(fieldName, object);
		
		if( value == null || !value.exists() ) {
			addFieldError(fieldName, object);
			return;
		}
	}

}
