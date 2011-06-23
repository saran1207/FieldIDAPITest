package com.n4systems.exporting;

import com.n4systems.model.Asset;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.ListLoader;

public class ExporterFactory {
	
	public ExporterFactory() {}
	
	public CustomerExporter createCustomerExporter(ListLoader<CustomerOrg> customerLoader, SecurityFilter filter) {
		return new CustomerExporter(customerLoader, filter);
	}
	
	public AutoAttributeExporter createAutoAttributeExporter(ListLoader<AutoAttributeDefinition> autoAttribLoader) {
		return new AutoAttributeExporter(autoAttribLoader);
	}
	
	public AssetExporter createAssetExporter(ListLoader<Asset> assetLoader) {
		return new AssetExporter(assetLoader);
	}
	
//	public EventExporter createEventExporter(ListLoader<Event> eventLoader, NextEventDateByEventLoader nextDateLoader) {
//		return new EventExporter(eventLoader, nextDateLoader);
//	}

	public Exporter createUserExporter(ListLoader<User> userListLoader) {		 
		return new UserExporter(userListLoader);
	}
	
}
