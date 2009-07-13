package com.n4systems.fieldid.validators;

import java.util.Collection;

import com.n4systems.model.api.Retirable;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class RequiredSetValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate( Object action ) throws ValidationException {
		String fieldName = getFieldName();
		
		Collection<?> set = (Collection<?>)this.getFieldValue(fieldName, action);
		
		if( set == null || set.isEmpty() ) {
			addFieldError(fieldName, action);
			return;
		} else {
			if( set.iterator().next() instanceof Retirable ) {
				Collection<Retirable> newSet = (Collection<Retirable>)set;
				for( Retirable element : newSet ) {
					if( element != null && !element.isRetired() ) {
						return; // kick out if you find one element that is not retired.
					}
				}
				addFieldError(fieldName, action);
			}
			
		}
	}	
}
