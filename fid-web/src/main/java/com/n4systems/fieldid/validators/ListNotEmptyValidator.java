package com.n4systems.fieldid.validators;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

import java.util.Collection;

public class ListNotEmptyValidator extends FieldValidatorSupport {
	@SuppressWarnings("unchecked")
	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
		Collection<Object> list = (Collection<Object>)this.getFieldValue(fieldName, object);
		
		if( list == null || list.isEmpty() ) {
			addFieldError(fieldName, object);
		}
	}

}
