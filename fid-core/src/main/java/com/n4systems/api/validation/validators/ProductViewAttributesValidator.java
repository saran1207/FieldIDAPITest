package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ProductView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.AssetType;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.infooption.MissingInfoOptionException;
import com.n4systems.model.infooption.StaticOptionResolutionException;
import com.n4systems.model.security.SecurityFilter;

public class ProductViewAttributesValidator extends ProductViewValidator {
	private final InfoOptionMapConverter converter;
	
	public ProductViewAttributesValidator(InfoOptionMapConverter converter) {
		this.converter = converter;
	}
	
	public ProductViewAttributesValidator() {
		this(new InfoOptionMapConverter());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ValidationResult validate(Object fieldValue, ProductView view, String fieldName, SecurityFilter filter, AssetType type) {
		Map<String, String> infoOptionMap = (Map<String, String>)fieldValue;

		try {
			converter.convertProductAttributes(infoOptionMap, type);
		} catch (MissingInfoOptionException e) {
			return ValidationResult.fail(MissingRequiredProductAttributeValidatorFail, e.getInfoField().getName());
		} catch (StaticOptionResolutionException e) {
			return ValidationResult.fail(StaticOptionNotFoundValidatorFail, e.getInfoOptionName(), e.getInfoField().getName());
		}
		
		return ValidationResult.pass();
	}

}
