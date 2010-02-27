package com.n4systems.api.validation.validators;

import java.util.Map;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.StringUtils;


public class AutoAttributeInputsValidator extends AutoAttributeValidator {

	@SuppressWarnings("unchecked")
	@Override
	public ValidationResult validate(Object fieldValue, AutoAttributeView view, String fieldName, SecurityFilter filter, AutoAttributeCriteria criteria) {
		Map<String, String> infoOptionMap = (Map<String, String>)fieldValue;

		boolean optionFound;
		String optionName;
		for (InfoFieldBean field: criteria.getInputs()) {
			// we cannot be missing any input info fields
			if (!infoOptionMap.containsKey(field.getName())) {
				return ValidationResult.fail(InputInfoFieldNotFoundValidatorFail, field.getName());
			}
			
			// make sure that we have a valid info option
			optionFound = false;
			optionName = infoOptionMap.get(field.getName());
			
			// can't hav blank input options
			if (StringUtils.isEmpty(optionName)) {
				return ValidationResult.fail(BlankInputOptionValidatorFail, field.getName());
			}
			
			// make sure we have a matching option
			for (InfoOptionBean option: field.getInfoOptions()) {			
				if (option.getName().equals(optionName)) {
					optionFound = true;
					break;
				}
			}
			
			if (!optionFound) {
				return ValidationResult.fail(StaticOptionNotFoundValidatorFail, optionName, field.getName());
			}
		}
		
		return ValidationResult.pass();
	}

}
