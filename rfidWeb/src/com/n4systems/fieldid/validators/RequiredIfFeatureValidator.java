package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.ExtendedFeature;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class RequiredIfFeatureValidator extends FieldValidatorSupport {

	private ExtendedFeature extendedFeature;
	
	public void validate( Object action ) throws ValidationException {
		String fieldName = getFieldName();
		AbstractAction abstractAction = (AbstractAction)action;
		
		if( abstractAction.getSecurityGuard().isExtendedFeatureEnabled(extendedFeature)){
			Object field = this.getFieldValue(fieldName, abstractAction);
			if( field == null ) {
				addFieldError(fieldName, action);
				return; 
			}
		}
	}


	public void setExtendedFeature(String extendedFeature) {
		this.extendedFeature = ExtendedFeature.valueOf(extendedFeature);
	}
}
