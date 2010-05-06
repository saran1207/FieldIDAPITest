package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.api.UserDateFormatValidator;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class DateValidator extends FieldValidatorSupport {

	
	private boolean usesTime;
	
	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
		UserDateFormatValidator dateFormatValidator = (UserDateFormatValidator)object;
		String nextDate = (String)this.getFieldValue(fieldName, object);
		
		// blank values are ok 
		if( nextDate != null && nextDate.length() > 0 && !dateFormatValidator.isValidDate(nextDate, usesTime)) {
			addFieldError(fieldName, object);
		}
	}

	

	public void setUsingTime( String usingTime ) {
		usesTime = ( usingTime.equals( "date" ) ) ? false : true; 
	}

}
