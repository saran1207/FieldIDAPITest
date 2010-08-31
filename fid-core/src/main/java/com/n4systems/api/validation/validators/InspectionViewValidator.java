package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.InspectionView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.InspectionType;
import com.n4systems.model.security.SecurityFilter;

public abstract class InspectionViewValidator implements FieldValidator {
	public static final String INSPECTION_TYPE_KEY = "inspectionType";
	
	protected abstract ValidationResult validate(Object fieldValue, InspectionView view, String fieldName, SecurityFilter filter, InspectionType type);
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		return validate(fieldValue, (InspectionView)view, fieldName, filter, (InspectionType)validationContext.get(INSPECTION_TYPE_KEY));
	}

}
