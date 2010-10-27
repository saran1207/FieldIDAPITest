package com.n4systems.api.validation.validators;

import com.n4systems.api.model.InspectionView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.Asset;
import com.n4systems.model.InspectionType;
import com.n4systems.model.inspectiontype.AssociatedInspectionTypeExistsLoader;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.security.SecurityFilter;


/*
 * NOTE: this validation should ALWAYS come after a NotNullValidator and AssetIdentifierValidator
 */
public class AssociatedInspectionTypeValidator extends InspectionViewValidator {

	@Override
	protected ValidationResult validate(Object fieldValue, InspectionView view, String fieldName, SecurityFilter filter, InspectionType inspectionType) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		String identifier = (String)fieldValue;
		
		SmartSearchLoader assetLoader = createSmartSearchLoader(filter).setSearchText(identifier);
		
		// the AssetIdentifierValidator ensures the following is safe
		Asset asset = assetLoader.load().get(0);
		
		AssociatedInspectionTypeExistsLoader assocLoader = createAssociatedInspectionTypeExistsLoader(filter);
		assocLoader.setInspectionType(inspectionType);
		assocLoader.setAssetType(asset.getType());
		
		boolean exists = assocLoader.load();
		if (exists) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(AssociatedInspectionTypeValidationFail, inspectionType.getName(), asset.getType().getName());
		}
	}

	protected SmartSearchLoader createSmartSearchLoader(SecurityFilter filter) {
		return new SmartSearchLoader(filter);
	}
	
	protected AssociatedInspectionTypeExistsLoader createAssociatedInspectionTypeExistsLoader(SecurityFilter filter) {
		return new AssociatedInspectionTypeExistsLoader(filter);
	}
}
