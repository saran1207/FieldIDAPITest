package com.n4systems.model.orgs;

import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;

public class FindOrCreateCustomerOrgHandler extends FindOrCreateExternalOrgHandler<CustomerOrg, PrimaryOrg> {
	public FindOrCreateCustomerOrgHandler(ListLoader<CustomerOrg> loader, Saver<? super CustomerOrg> saver) {
		super(loader, saver);
	}
	
	
	protected CustomerOrg createOrg(PrimaryOrg parent, String name, String code) {
		CustomerOrg customer = new CustomerOrg();
		
		customer.setTenant(parent.getTenant());
		customer.setParent(parent);
		customer.setCode(code);
		customer.setName(name);
		return customer;
	}



	

}
