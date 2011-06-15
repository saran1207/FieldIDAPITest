package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.AssetView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.AssetType;
import com.n4systems.model.security.SecurityFilter;

public abstract class AssetViewValidator implements FieldValidator {
	public static final String ASSET_TYPE_KEY = "assetType";
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		return validate(fieldValue, (AssetView)view, fieldName, filter, (AssetType)validationContext.get(ASSET_TYPE_KEY));
	}

	abstract public ValidationResult validate(Object fieldValue, AssetView view, String fieldName, SecurityFilter filter, AssetType type);
	
}
