package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.assettype.AssetTypeByNameExistsLoader;
import com.n4systems.model.security.SecurityFilter;

import java.util.Map;

public class AssetTypeExistsValidator implements FieldValidator {
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		String name = (String)fieldValue;
		
		AssetTypeByNameExistsLoader typeExistsLoader = createAssetTypeExistsLoader(filter).setName(name);
		
		if (typeExistsLoader.load()) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(AssetTypeExistsValidatorFail, fieldName);
		}
	}
	
	protected AssetTypeByNameExistsLoader createAssetTypeExistsLoader(SecurityFilter filter) {
		return new AssetTypeByNameExistsLoader(filter);
	}
}
