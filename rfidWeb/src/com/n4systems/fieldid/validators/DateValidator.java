package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class DateValidator extends FieldValidatorSupport {

	
	private boolean usesTime;
	
	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
		AbstractAction action = (AbstractAction)object;
		String nextDate = (String)this.getFieldValue(fieldName, object);
		
		// blank values are ok 
		if( nextDate != null && nextDate.length() > 0 && !action.isValidDate(nextDate, usesTime)) {
			addFieldError(fieldName, object);
		}
	}

	

	public void setUsingTime( String usingTime ) {
		usesTime = ( usingTime.equals( "date" ) ) ? false : true; 
	}

}
