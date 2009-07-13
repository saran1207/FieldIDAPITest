package com.n4systems.fieldid.validators;

import java.util.Collection;
import java.util.List;

import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;



public class InfoOptionValidator extends FieldValidatorSupport {
	@SuppressWarnings("unchecked")
	public void validate(Object object) throws ValidationException {
		
		String fieldName = getFieldName();
				
		Collection<InfoOptionInput> infoOptions = (Collection<InfoOptionInput>)this.getFieldValue(fieldName, object);
		List<InfoFieldInput> infoFields = (List<InfoFieldInput>)this.getFieldValue("infoFields", object);
		if( infoOptions != null ) {
			for (InfoOptionInput infoOption : infoOptions) {
				// only validate if the info field hasn't been deleted.
				if( !infoFields.get( infoOption.getInfoFieldIndex().intValue() ).isDeleted() ) {
					if( infoOption.getName() != null && infoOption.getName().trim().equals("") && !infoOption.isDeleted() ) {
						addFieldError(fieldName, object);
						return;
					}
				}
			}
		}
	}

}
