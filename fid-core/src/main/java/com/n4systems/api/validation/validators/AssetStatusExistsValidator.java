package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.assetstatus.AssetStatusForNameExistsLoader;
import com.n4systems.model.security.SecurityFilter;

public class AssetStatusExistsValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		String name = (String)fieldValue;
		AssetStatusForNameExistsLoader statusExistsLoader = createAssetStatusExistsLoader(filter).setName(name);
		
		if (statusExistsLoader.load()) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(NamedFieldNotFoundValidatorFail, fieldName, name);
		}
	}

	protected AssetStatusForNameExistsLoader createAssetStatusExistsLoader(SecurityFilter filter) {
		return new AssetStatusForNameExistsLoader(filter);
	}
}
