package com.n4systems.api.validation.validators;

import com.n4systems.api.model.EventView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.model.Asset;
import com.n4systems.model.safetynetwork.AssetsByIdOwnerTypeLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;

public class EventAssetIdentifierValidator extends AssetIdentifierValidator {
	

    @Override
	protected <V extends ExternalModelView> ListLoader<Asset> createLoader(SecurityFilter filter, V view, String identifier) {        
        if (view instanceof EventView) { 
        	EventView eventView = (EventView) view;
           	AssetsByIdOwnerTypeLoader loader = new AssetsByIdOwnerTypeLoader(filter);
           	loader.setIdentifier(identifier);
           	loader.setOwner(eventView.getOrganization(), eventView.getCustomer(), eventView.getDivision());
           	return loader;
        }
        throw new IllegalStateException("this asset id validator should only be used when importing events");
    }
	
}
