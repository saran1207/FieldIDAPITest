package com.n4systems.fieldid.validators;


import java.util.Collection;

import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class InfoFieldValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
				
		Collection<InfoFieldInput> infoFields = (Collection<InfoFieldInput>)this.getFieldValue(fieldName, object);
				
		if( infoFields != null ) {
			for (InfoFieldInput infoField : infoFields) {
				if( infoField != null && 
						infoField.getName() != null && 
						infoField.getName().trim().equals("") && 
						!infoField.isDeleted() ) {
					
					addFieldError(fieldName, object);
					return;
				}
			}
		}
	}
}
