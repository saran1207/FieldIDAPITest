package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.viewhelpers.TrimmedString;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

import java.util.Collection;

public class RequiredStringSet extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate( Object action ) throws ValidationException {
		String fieldName = getFieldName();
		
		Collection<TrimmedString> set = (Collection<TrimmedString>)this.getFieldValue(fieldName, action);
		
		if( set == null || set.isEmpty() ) {
			return;
		} else {
			for( TrimmedString string : set ) {
				if( string != null && string.length() == 0 ) {
					addFieldError(fieldName, action);
					return;
				}
			}
		}
	}
}
