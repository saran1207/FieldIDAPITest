package com.n4systems.exporting;

import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Product;
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
	
	public ProductExporter createProductExporter(ListLoader<Product> productLoader) {
		return new ProductExporter(productLoader);
	}
}
