package com.n4systems.api.validation.validators;

import com.n4systems.api.model.EventView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.Asset;
import com.n4systems.model.EventType;
import com.n4systems.model.inspectiontype.AssociatedEventTypeExistsLoader;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.security.SecurityFilter;


/*
 * NOTE: this validation should ALWAYS come after a NotNullValidator and AssetIdentifierValidator
 */
public class AssociatedEventTypeValidator extends EventViewValidator {

	@Override
	protected ValidationResult validate(Object fieldValue, EventView view, String fieldName, SecurityFilter filter, EventType eventType) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		String identifier = (String)fieldValue;
		
		SmartSearchLoader assetLoader = createSmartSearchLoader(filter).setSearchText(identifier);
		
		// the AssetIdentifierValidator ensures the following is safe
		Asset asset = assetLoader.load().get(0);
		
		AssociatedEventTypeExistsLoader assocLoader = createAssociatedEventTypeExistsLoader(filter);
		assocLoader.setEventType(eventType);
		assocLoader.setAssetType(asset.getType());
		
		boolean exists = assocLoader.load();
		if (exists) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(AssociatedEventTypeValidationFail, eventType.getName(), asset.getType().getName());
		}
	}

	protected SmartSearchLoader createSmartSearchLoader(SecurityFilter filter) {
		return new SmartSearchLoader(filter);
	}
	
	protected AssociatedEventTypeExistsLoader createAssociatedEventTypeExistsLoader(SecurityFilter filter) {
		return new AssociatedEventTypeExistsLoader(filter);
	}
}
