package com.n4systems.api.validation.validators;

import java.util.Map;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.StringUtils;


public class AutoAttributeOutputsValidator extends AutoAttributeValidator {

	@SuppressWarnings("unchecked")
	@Override
	public ValidationResult validate(Object fieldValue, AutoAttributeView view, String fieldName, SecurityFilter filter, AutoAttributeCriteria criteria) {
		Map<String, String> infoOptionMap = (Map<String, String>)fieldValue;

		boolean optionFound;
		String optionName;
		for (InfoFieldBean field: criteria.getOutputs()) {
			// it's ok to be missing an output, we'll just consider it blank
			if (!infoOptionMap.containsKey(field.getName())) {
				continue;
			}
			
			// if the info option is static only, then we need to make sure the value is valid
			optionName = infoOptionMap.get(field.getName());
			
			if (StringUtils.isEmpty(optionName)) {
				// blank options are allowed
				continue;
			}
			
			// if the field can't accept dynamic options, than we need to validate
			// we have a valid option
			if (!field.acceptsDyanmicInfoOption()) {
				optionFound = false;
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
		}
		
		return ValidationResult.pass();
	}

}
