package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
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
		Asset asset = idExistsLoader.setMobileGuid((String)mobileGuid).load();
		if (asset != null) {
			Object assetType = validationContext.get(AssetViewValidator.ASSET_TYPE_KEY);
			if (assetType != null && assetType instanceof AssetType) {
				if (!((AssetType)assetType).getId().equals(asset.getType().getId())) {
					return ValidationResult.fail(ExternalAssetMobileGuidCorrectAssetTypeFail, fieldName, mobileGuid, asset.getType().getName());
				}
			}
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(ExternalAssetMobileGuidValidatorFail, fieldName, mobileGuid);
		}
		
	}

	protected AssetByMobileGuidLoader getMobileGuidExistsLoader(SecurityFilter filter) {
		return new AssetByMobileGuidLoader(filter);
	}
}
