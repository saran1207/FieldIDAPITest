package com.n4systems.fieldid.validators;

import java.util.List;

import com.n4systems.fieldid.actions.product.ProductTypeConfigurationCrud;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class SubProductTypeValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate(Object object) throws ValidationException {
		
		String fieldName = getFieldName();
		List<Long> values = (List<Long>)this.getFieldValue(fieldName, object);
		ProductTypeConfigurationCrud action = (ProductTypeConfigurationCrud) object;
		
		for( Long typeId : values ) {
			
			if( typeId != null && action.isParentType( typeId ) ) {
				addFieldError(fieldName, object);
				return;
			}
		}
		
	}
}
