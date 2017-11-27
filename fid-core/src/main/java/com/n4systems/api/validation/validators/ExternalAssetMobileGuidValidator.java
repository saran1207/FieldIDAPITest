package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.asset.AssetByMobileGuidLoader;
import com.n4systems.model.security.SecurityFilter;

import java.util.Map;

public class ExternalAssetMobileGuidValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object mobileGuid, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		if (mobileGuid == null) {
			// A null mobileGuid just means it's an add
			return ValidationResult.pass();
		}

		AssetByMobileGuidLoader idExistsLoader = getMobileGuidExistsLoader(filter);
		if (idExistsLoader.setMobileGuid((String)mobileGuid).load() != null) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(ExternalAssetMobileGuidValidatorFail, fieldName, mobileGuid);
		}
		
	}

	protected AssetByMobileGuidLoader getMobileGuidExistsLoader(SecurityFilter filter) {
		return new AssetByMobileGuidLoader(filter);
	}
}
