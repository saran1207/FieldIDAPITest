package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.asset.SmartSearchCounter;
import com.n4systems.model.security.SecurityFilter;

public class AssetIdentifierValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		String identifier = (String)fieldValue;
		
		SmartSearchCounter assetLoader = createSmartSearchCounter(filter).setSearchText(identifier);
		
		Long assetCount = assetLoader.load();
		if (assetCount == 1) {
			return ValidationResult.pass();
		} else if (assetCount == 0) {
			return ValidationResult.fail(NoAssetFoundValidationFail, identifier, fieldName);
		} else {
			return ValidationResult.fail(MultipleAssetFoundValidationFail, identifier, fieldName);
		}
	}

	protected SmartSearchCounter createSmartSearchCounter(SecurityFilter filter) {
		return new SmartSearchCounter(filter);
	}
	
}
