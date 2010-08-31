package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.infooption.MissingInfoOptionException;
import com.n4systems.model.infooption.StaticOptionResolutionException;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.StringUtils;


public class AutoAttributeInputsValidator extends AutoAttributeValidator {
	private final InfoOptionMapConverter converter;
	
	public AutoAttributeInputsValidator() {
		this(new InfoOptionMapConverter());
	}
	
	public AutoAttributeInputsValidator(InfoOptionMapConverter converter) {
		this.converter = converter;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ValidationResult validate(Object fieldValue, AutoAttributeView view, String fieldName, SecurityFilter filter, AutoAttributeCriteria criteria) {
		Map<String, String> infoOptionMap = (Map<String, String>)fieldValue;

		try {
			converter.convertAutoAttributeInputs(infoOptionMap, criteria);
		} catch (MissingInfoOptionException e) {
			return ValidationResult.fail(InputInfoFieldNotFoundValidatorFail, e.getInfoField().getName());
		} catch (StaticOptionResolutionException e) {
			if (StringUtils.isEmpty(e.getInfoOptionName())) {
				return ValidationResult.fail(BlankInputOptionValidatorFail, e.getInfoField().getName());
			} else {
				return ValidationResult.fail(StaticOptionNotFoundValidatorFail, e.getInfoOptionName(), e.getInfoField().getName());
			}
		}
		
		return ValidationResult.pass();
	}

}
