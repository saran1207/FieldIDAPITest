package com.n4systems.api.validation.validators;

import java.util.List;
import java.util.Map;

import com.n4systems.api.model.EventView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.eventtype.AssociatedEventTypeExistsLoader;
import com.n4systems.model.safetynetwork.AssetsByIdOwnerTypeLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.Loader;

public class EventAssetIdentifierValidator implements FieldValidator {
	
	// TODO DD : refactor this to extend AssetIdentifierValidator.
	// or if i can use composition...only the loader needs to be different.
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
        if (fieldValue == null) {
            return ValidationResult.pass();
        }

        String identifier = (String)fieldValue;

        Loader<List<Asset>> loader = createLoader(filter, view, identifier);
        
        List<Asset> assets = loader.load();
        // FIXME DD : it possible that list > 1.  
        // ...but after eliminating the assets that aren't associated with this event we *must* be down to 1 and only 1.
        if (assets.size() == 1) {
            AssetType assetType = assets.get(0).getType();

            EventType eventType = (EventType) validationContext.get(EventViewValidator.EVENT_TYPE_KEY);
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

    protected <V extends ExternalModelView> Loader<List<Asset>> createLoader(SecurityFilter filter, V view, String identifier) {        
        // TODO DD : uggh.  casting this sucks...hmmm...have a getOwner() method on all extenralModelViews?
        if (view instanceof EventView) { 
        	EventView eventView = (EventView) view;
           	String organization = eventView.getOrganization();
           	String customer = eventView.getCustomer();
           	String division = eventView.getDivision();
           	//assetType = eventView.get   ??? FIXME DD : what to do with asset type? is it in Excel file...where do i get this???
           	AssetsByIdOwnerTypeLoader loader = new AssetsByIdOwnerTypeLoader(filter);
           	loader.setIdentifier(identifier);
           	loader.setOwner(organization, customer, division);
           	return loader;
        }
        throw new IllegalStateException("this asset id validator should only be used when importing events");
    }

    protected AssociatedEventTypeExistsLoader createAssociatedEventTypeExistsLoader(SecurityFilter filter) {
        return new AssociatedEventTypeExistsLoader(filter);
    }
	
}
