package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.ProductView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.ProductType;
import com.n4systems.model.security.SecurityFilter;

public abstract class ProductViewValidator implements FieldValidator {
	public static final String PRODUCT_TYPE_KEY = "productType";
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		return validate(fieldValue, (ProductView)view, fieldName, filter, (ProductType)validationContext.get(PRODUCT_TYPE_KEY));
	}

	abstract public ValidationResult validate(Object fieldValue, ProductView view, String fieldName, SecurityFilter filter, ProductType type);
	
}
