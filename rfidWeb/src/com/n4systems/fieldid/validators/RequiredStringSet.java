package com.n4systems.fieldid.validators;

import java.util.Collection;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class RequiredStringSet extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate( Object action ) throws ValidationException {
		String fieldName = getFieldName();
		
		Collection<String> set = (Collection<String>)this.getFieldValue(fieldName, action);
		
		if( set == null || set.isEmpty() ) {
			return;
		} else {
			for( String string : set ) {
				if( string != null && string.length() == 0 ) {
					addFieldError(fieldName, action);
					return;
				}
			}
		}
	}
}
