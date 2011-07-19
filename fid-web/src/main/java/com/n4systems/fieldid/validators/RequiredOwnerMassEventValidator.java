package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.event.EventCrud;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class RequiredOwnerMassEventValidator extends FieldValidatorSupport {
	
	@Override
	public void validate(Object action) throws ValidationException {
		
		String fieldName = getFieldName();
		EventCrud eventCrud = (EventCrud)action;
		if ( !eventCrud.getModifiableEvent().isOwnerSetFromAsset() ) {
			if ( eventCrud.getModifiableEvent().getOwner()==null ) { 
				addFieldError(fieldName, action);
			}
		}
	}

}
