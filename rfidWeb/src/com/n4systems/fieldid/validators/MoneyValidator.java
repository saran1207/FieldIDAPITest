package com.n4systems.fieldid.validators;

import com.n4systems.tools.MoneyUtils;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class MoneyValidator extends FieldValidatorSupport {

	public void validate( Object action ) throws ValidationException {
		String fieldName = getFieldName();
		String moneyValue = (String)this.getFieldValue(fieldName, action);
		
		MoneyUtils moneyUtil = new MoneyUtils();
		try {
			if( moneyUtil.toCents( moneyValue ) != null ) {
				return;
			}
		} catch (Exception e) {}
		
		addFieldError(fieldName, action);
	}

}
