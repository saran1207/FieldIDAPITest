package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.infooption.StaticOptionResolutionException;
import com.n4systems.model.security.SecurityFilter;


public class AutoAttributeOutputsValidator extends AutoAttributeValidator {
	private final InfoOptionMapConverter converter;
	
	public AutoAttributeOutputsValidator() {
		this(new InfoOptionMapConverter());
	}

	public AutoAttributeOutputsValidator(InfoOptionMapConverter converter) {
		this.converter = converter;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ValidationResult validate(Object fieldValue, AutoAttributeView view, String fieldName, SecurityFilter filter, AutoAttributeCriteria criteria) {
		Map<String, String> infoOptionMap = (Map<String, String>)fieldValue;
	
		try {
			converter.convertAutoAttributeOutputs(infoOptionMap, criteria);
		} catch (StaticOptionResolutionException e) {
			return ValidationResult.fail(StaticOptionNotFoundValidatorFail, e.getInfoOptionName(), e.getInfoField().getName());
		}
		
		return ValidationResult.pass();
	}

}
