package com.n4systems.model.tenant;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.TenantOrganization;
import com.n4systems.persistence.loaders.legacy.EntityLoader;

public class AllTenantsListLoader extends EntityLoader<List<TenantOrganization>> {
	
	public AllTenantsListLoader() {
		super();
	}
	
	public AllTenantsListLoader(PersistenceManager pm) {
		super(pm);
	}

	@Override
	protected List<TenantOrganization> load(PersistenceManager pm) {
		return pm.findAll(TenantOrganization.class);
	}

}
