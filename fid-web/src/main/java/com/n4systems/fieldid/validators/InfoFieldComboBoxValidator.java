package com.n4systems.fieldid.validators;

import java.util.List;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class InfoFieldComboBoxValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
		
		List<InfoFieldInput> infoFields = (List<InfoFieldInput>)this.getFieldValue( fieldName, object );
		List<InfoOptionInput> infoOptions = (List<InfoOptionInput>)this.getFieldValue( "editInfoOptions", object );
		if( infoFields != null ) {
			for( InfoFieldInput infoField : infoFields ) {
				
				if( ( infoField != null ) && 
						!( infoField.getFieldType().equals(InfoFieldBean.InfoFieldType.TextField.name() ) 
								||	infoField.getFieldType().equals(InfoFieldBean.InfoFieldType.UnitOfMeasure.name() )
								||	infoField.getFieldType().equals(InfoFieldBean.InfoFieldType.DateField.name() ) ) 
						&& !infoField.isDeleted() ) {
					
					int infoOptionsOnThisInfoField = 0;
					if( infoOptions != null ) {
						for( InfoOptionInput infoOptionInput : infoOptions ) {
							if( infoOptionInput != null && infoOptionInput.getInfoFieldIndex() != null ) {
								if( infoOptionInput.getInfoFieldIndex().intValue() == infoFields.indexOf( infoField )  && !infoOptionInput.isDeleted() ) {
									infoOptionsOnThisInfoField++;
								}
							}
						}
					}
					
					if( infoOptionsOnThisInfoField == 0 ) {
						addFieldError(fieldName, object);
						return;
					}
				}
			}
		}
	}
}
