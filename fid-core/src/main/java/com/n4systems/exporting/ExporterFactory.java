package com.n4systems.exporting;

import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Inspection;
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
	
	public ProductExporter createProductExporter(ListLoader<Asset> productLoader) {
		return new ProductExporter(productLoader);
	}
	
	public InspectionExporter createInspectionExporter(ListLoader<Inspection> inspectionLoader, NextInspectionDateByInspectionLoader nextDateLoader) {
		return new InspectionExporter(inspectionLoader, nextDateLoader);
	}
	
}
