package com.n4systems.fieldid.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.model.AssetType;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class AssetDescriptionTemplateValidator extends FieldValidatorSupport {
	private static final String errorStart = "errors.descriptionTemplateStart";
	private static final String errorEnd = "errors.descriptionTemplateEnd";
	
	
	public void validate(Object object) throws ValidationException {
		
		HasAssetDescriptionTemplateValidator form = (HasAssetDescriptionTemplateValidator)object;
		
		List<String> invalidInfoFields = getInvalidDescriptionTemplateVariables(form.getDescriptionTemplate(), form.getInfoFields());
		
		// if we have more then one invalid field then validation fails
		if(invalidInfoFields.size() > 0) {
			String errorMsg = getValidatorContext().getText(errorStart) + " ";
			
			for(String varName: invalidInfoFields) {
				errorMsg += "\"" + varName + "\", ";
			}
			
			errorMsg += " " + getValidatorContext().getText(errorEnd);
			
			
			this.getValidatorContext().addFieldError(getFieldName(), errorMsg);
		}
	}
	
	private List<String> getInvalidDescriptionTemplateVariables(String descriptionTemplate, Collection<InfoFieldInput> infoInputs) {
		List<String> fieldNames = new ArrayList<String>();
		for(InfoFieldInput infoInput: infoInputs) {
			fieldNames.add(infoInput.getName());
		}
		return AssetType.getInvalidDescriptionTemplateVariables(descriptionTemplate, fieldNames);
	}
}
