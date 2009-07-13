package com.n4systems.fieldid.validators;

import java.util.Collection;

import com.n4systems.model.api.NamedEntity;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class RequiredNameSetValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate( Object action ) throws ValidationException {
		String fieldName = getFieldName();
		Collection<?> set = (Collection<?>)this.getFieldValue(fieldName, action);
		
		if( set != null && !set.isEmpty() ) {
			Collection<NamedEntity> newSet = (Collection<NamedEntity>)set;
			for( NamedEntity element : newSet ) {
				if( element != null && ( element.getName() == null || element.getName().trim().length() == 0 ) ) {
					addFieldError(fieldName, action);
					return; 
				}
			}
		}
	}

}
