package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.asset.AssetTypeConfigurationCrud;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

import java.util.List;

public class SubAssetTypeValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate(Object object) throws ValidationException {
		
		String fieldName = getFieldName();
		List<Long> values = (List<Long>)this.getFieldValue(fieldName, object);
		AssetTypeConfigurationCrud action = (AssetTypeConfigurationCrud) object;
		
		for( Long typeId : values ) {
			
			if( typeId != null && action.isParentType( typeId ) ) {
				addFieldError(fieldName, object);
				return;
			}
		}
		
	}
}
