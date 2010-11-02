package com.n4systems.exporting;

import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Event;
import com.n4systems.model.Asset;
import com.n4systems.model.inspectionschedule.NextInspectionDateByInspectionLoader;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
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
	
	public InspectionExporter createInspectionExporter(ListLoader<Event> inspectionLoader, NextInspectionDateByInspectionLoader nextDateLoader) {
		return new InspectionExporter(inspectionLoader, nextDateLoader);
	}
	
}
