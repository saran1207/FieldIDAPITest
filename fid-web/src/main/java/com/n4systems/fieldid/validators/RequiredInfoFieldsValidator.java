package com.n4systems.fieldid.validators;

import java.util.List;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class RequiredInfoFieldsValidator extends FieldValidatorSupport {
	
	@SuppressWarnings("unchecked")
	public void validate(Object object) throws ValidationException {
		
		String fieldName = getFieldName();
		List<InfoOptionInput> value = (List<InfoOptionInput>)this.getFieldValue(fieldName, object);
		List<InfoFieldBean> infoFields = (List<InfoFieldBean>)this.getFieldValue("assetInfoFields", object);
		
		if( infoFields == null ) { return; }
		
		for (InfoFieldBean infoFieldBean : infoFields ) {
			if( !infoFieldBean.isRetired() ) {
				for ( InfoOptionInput infoOptionInput : value ) {
					if( infoOptionInput != null && 
							infoFieldBean.getUniqueID().equals( infoOptionInput.getInfoFieldId() ) &&
							infoFieldBean.isRequired() && infoOptionInput.isBlank() ) {
						
						addFieldError(fieldName, object);
						return;
					}
				}
			}
		}
	}

}
