package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.AssetView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.AssetType;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.infooption.MissingInfoOptionException;
import com.n4systems.model.infooption.StaticOptionResolutionException;
import com.n4systems.model.security.SecurityFilter;

public class AssetViewAttributesValidator extends AssetViewValidator {
	private final InfoOptionMapConverter converter;
	
	public AssetViewAttributesValidator(InfoOptionMapConverter converter) {
		this.converter = converter;
	}
	
	public AssetViewAttributesValidator() {
		this(new InfoOptionMapConverter());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ValidationResult validate(Object fieldValue, AssetView view, String fieldName, SecurityFilter filter, AssetType type) {
		Map<String, String> infoOptionMap = (Map<String, String>)fieldValue;

		try {
			converter.convertAssetAttributes(infoOptionMap, type);
		} catch (MissingInfoOptionException e) {
			return ValidationResult.fail(MissingRequiredAssetAttributeValidatorFail, e.getInfoField().getName());
		} catch (StaticOptionResolutionException e) {
			return ValidationResult.fail(StaticOptionNotFoundValidatorFail, e.getInfoOptionName(), e.getInfoField().getName());
		}
		
		return ValidationResult.pass();
	}

}
