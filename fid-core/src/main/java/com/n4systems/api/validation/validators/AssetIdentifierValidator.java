package com.n4systems.api.validation.validators;

import java.util.List;
import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.asset.SmartSearchCounter;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.eventtype.AssociatedEventTypeExistsLoader;
import com.n4systems.model.security.SecurityFilter;

public class AssetIdentifierValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
        if (fieldValue == null) {
            return ValidationResult.pass();
        }

        String identifier = (String)fieldValue;

        SmartSearchLoader loader = createSmartSearchLoader(filter);
        loader.setMaxResults(2);
        loader.setSearchText(identifier);

        List<Asset> assets = loader.load();
        if (assets.size() == 1) {
            AssetType assetType = assets.get(0).getType();

            EventType eventType = (EventType) validationContext.get("eventType");
            AssociatedEventTypeExistsLoader existsLoader = createAssociatedEventTypeExistsLoader(filter);

            existsLoader.setAssetType(assetType);
            existsLoader.setEventType(eventType);
            if (existsLoader.load()) {
                return ValidationResult.pass();
            } else {
                return ValidationResult.fail(AssociatedEventTypeValidationFail, eventType.getName(), assetType.getName());
            }
        } else if (assets.size() == 0) {
            return ValidationResult.fail(NoAssetFoundValidationFail, identifier, fieldName);
        } else {
            return ValidationResult.fail(MultipleAssetFoundValidationFail, identifier, fieldName);
        }
	}

    protected SmartSearchLoader createSmartSearchLoader(SecurityFilter filter) {
        SmartSearchLoader loader = new SmartSearchLoader(filter);
        return loader;
    }

    protected AssociatedEventTypeExistsLoader createAssociatedEventTypeExistsLoader(SecurityFilter filter) {
        return new AssociatedEventTypeExistsLoader(filter);
    }
	
}
