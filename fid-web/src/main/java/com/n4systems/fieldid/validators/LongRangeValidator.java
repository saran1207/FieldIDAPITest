package com.n4systems.fieldid.validators;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class LongRangeValidator extends FieldValidatorSupport {
	
	private Long min = null;
	private Long max = null;

    @Override
    public void validate(Object action) throws ValidationException {
    	Long value = (Long) getFieldValue(getFieldName(), action);
    	if (!isValid(value)) { 
    		addFieldError(getFieldName(), action);
    	}
    }

	private boolean isValid(Long value) {
		return value != null && 
				(min==null || value>=min) && (max==null||value<=max);
	}

	public void setMin(Long min) {
		this.min = min;
	}

	public Long getMin() {
		return min;
	}

	public void setMax(Long max) {
		this.max = max;
	}

	public Long getMax() {
		return max;
	}

}
