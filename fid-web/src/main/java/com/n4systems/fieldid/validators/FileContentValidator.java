package com.n4systems.fieldid.validators;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class FileContentValidator extends FieldValidatorSupport {

	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
		
		String contentType = (String)this.getFieldValue(fieldName, object);
		
		if( contentType != null ) {
			if( !contentType.toLowerCase().startsWith("image/")  )  {
				addFieldError(fieldName, object);
				return;
			}
		}
	}

}
