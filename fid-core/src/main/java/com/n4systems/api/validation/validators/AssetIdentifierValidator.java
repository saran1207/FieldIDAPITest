package com.n4systems.api.validation.validators;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.eventtype.AssociatedEventTypeExistsLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.loaders.Loader;

public class AssetIdentifierValidator implements FieldValidator {
	
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
        if (fieldValue==null) {
            return ValidationResult.pass();
        }

        String identifier = (String)fieldValue;

        Loader<List<Asset>> loader = createLoader(filter, view, identifier);
        
        List<Asset> assets = loader.load();
        
        // NOTE : it is possible that more than one asset can be found.  
        // ...at this point we must narrow down.
        final AssociatedEventTypeExistsLoader existsLoader = createAssociatedEventTypeExistsLoader(filter, (EventType) validationContext.get(EventViewValidator.EVENT_TYPE_KEY));
        
        Predicate<Asset> predicate = new Predicate<Asset>() {
			@Override public boolean apply(Asset asset) {
	            AssetType assetType = asset.getType();
	            existsLoader.setAssetType(assetType);	// filter out assets that don't have this event type associated with them.
	            Boolean load = existsLoader.load();
	            return load;
			} 
        };
		Collection<Asset> assetsWithThisEventType = Collections2.filter(assets, predicate);

		switch (assetsWithThisEventType.size()) {
		case 0:
			return ValidationResult.fail(NoAssetFoundValidationFail, identifier, fieldName);
		case 1:
			return ValidationResult.pass();
		default:			
			return ValidationResult.fail(MultipleAssetFoundValidationFail, identifier, fieldName);
		}
	}	

    protected <V extends ExternalModelView> ListLoader<Asset> createLoader(SecurityFilter filter, V view, String identifier) {
        SmartSearchLoader loader = new SmartSearchLoader(filter);
        loader.setMaxResults(2);
        loader.setSearchText(identifier);
        return loader;
    }

    protected AssociatedEventTypeExistsLoader createAssociatedEventTypeExistsLoader(SecurityFilter filter, EventType eventType) {
    	AssociatedEventTypeExistsLoader loader = new AssociatedEventTypeExistsLoader(filter);    	
        loader.setEventType(eventType);
        return loader;        
    }
}
